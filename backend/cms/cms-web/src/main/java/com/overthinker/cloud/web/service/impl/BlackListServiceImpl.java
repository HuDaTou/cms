package com.overthinker.cloud.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.constants.BlackListConst;
import com.overthinker.cloud.web.entity.constants.RedisConst;
import com.overthinker.cloud.web.entity.DTO.AddBlackListDTO;
import com.overthinker.cloud.web.entity.DTO.SearchBlackListDTO;
import com.overthinker.cloud.web.entity.DTO.UpdateBlackListDTO;
import com.overthinker.cloud.web.entity.PO.BlackList;
import com.overthinker.cloud.web.entity.PO.User;
import com.overthinker.cloud.web.entity.VO.BlackListVO;
import com.overthinker.cloud.web.entity.ip.BlackListIpInfo;
import com.overthinker.cloud.web.mapper.BlackListMapper;
import com.overthinker.cloud.web.mapper.UserMapper;
import com.overthinker.cloud.web.service.BlackListService;
import com.overthinker.cloud.web.service.IpService;
import com.overthinker.cloud.web.utils.IpUtils;
import com.overthinker.cloud.web.utils.MyRedisCache;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (BlackList)表服务实现类
 *
 * @author overH
 * @since 2024-09-05 16:13:21
 */
@Service("blackListService")
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements BlackListService {

    @Resource
    private BlackListMapper blackListMapper;

    @Resource
    private IpService ipService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MyRedisCache myRedisCache;

    @Override
    public ResultData<Void> addBlackList(AddBlackListDTO addBlackListDTO) {
        if (!addBlackListDTO.getUserIds().isEmpty()) {
            Long count = blackListMapper.selectCount(new LambdaQueryWrapper<BlackList>().in(BlackList::getUserId, addBlackListDTO.getUserIds()));
            if (count > 0) {
                return ResultData.failure("用户已存在黑名单中");
            }

            for (int i = 0; i < addBlackListDTO.getUserIds().size(); i++) {
                if (!saveBlackList(addBlackListDTO, i)) {
                    return ResultData.failure("添加黑名单失败");
                }
            }
        } else {
            if (!saveBlackList(addBlackListDTO, null)) {
                return ResultData.failure("添加黑名单失败");
            }
        }

        return ResultData.success();
    }

    protected Boolean saveBlackList(AddBlackListDTO addBlackListDTO, Integer index) {
        BlackList blackList = BlackList.builder()
                .userId(!addBlackListDTO.getUserIds().isEmpty() ? addBlackListDTO.getUserIds().get(index) : null)
                .reason(addBlackListDTO.getReason())
                .type(!addBlackListDTO.getUserIds().isEmpty() ? BlackListConst.BLACK_LIST_TYPE_USER : BlackListConst.BLACK_LIST_TYPE_BOT)
                .expiresTime(addBlackListDTO.getExpiresTime()).build();

        BlackListIpInfo blackListIpInfo = BlackListIpInfo.builder()
                .createIp(!addBlackListDTO.getUserIds().isEmpty() ? null : IpUtils.getIpAddr(SecurityUtils.getCurrentHttpRequest()))
                .build();
        blackList.setIpInfo(blackListIpInfo);
        if (addBlackListDTO.getUserIds().isEmpty()) {
            Long idByIp = blackListMapper.getIdByIp(blackListIpInfo.getCreateIp());
            if (idByIp != null) {
                // 存在
                blackList.setId(idByIp);
            }
        }
        if (null != blackList.getId() ? this.updateById(blackList) : this.save(blackList)) {
            if (blackList.getType() == BlackListConst.BLACK_LIST_TYPE_BOT) {
                ipService.refreshIpDetailAsyncByBid(blackList.getId());
            }
            updateBlackListCache(blackList);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<BlackListVO> getBlackList(SearchBlackListDTO searchBlackListDTO) {
        LambdaQueryWrapper<BlackList> queryWrapper = new LambdaQueryWrapper<>();

        if (null != searchBlackListDTO) {
            // 搜索
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchBlackListDTO.getUserName()));
            if (!users.isEmpty())
                queryWrapper.in(StringUtils.isNotEmpty(searchBlackListDTO.getUserName()), BlackList::getUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                queryWrapper.eq(StringUtils.isNotNull(searchBlackListDTO.getUserName()), BlackList::getUserId, null);

            queryWrapper.like(StrUtil.isNotBlank(searchBlackListDTO.getReason()), BlackList::getReason, searchBlackListDTO.getReason())
                    .eq(null != searchBlackListDTO.getType(), BlackList::getType, searchBlackListDTO.getType())
                    .between(StringUtils.isNotNull(searchBlackListDTO.getStartTime()) && StringUtils.isNotNull(searchBlackListDTO.getEndTime()), BlackList::getBannedTime, searchBlackListDTO.getStartTime(), searchBlackListDTO.getEndTime());
        }
        queryWrapper.orderByDesc(BlackList::getCreateTime);

        return this.list(queryWrapper).stream()
                .map(blackList ->
                        blackList.asViewObject(
                                BlackListVO.class, (black) ->
                                {
                                    if (blackList.getUserId() != null) {
                                        black.setUserName(
                                                userMapper.selectById(blackList.getUserId()).getUsername()
                                        );
                                    }
                                }
                        )
                )
                .toList();
    }

    @Override
    public ResultData<Void> updateBlackList(UpdateBlackListDTO updateBlackListDTO) {
        BlackList blackList = BlackList.builder()
                .id(updateBlackListDTO.getId())
                .reason(updateBlackListDTO.getReason())
                .expiresTime(updateBlackListDTO.getExpiresTime()).build();
        if (this.updateById(blackList)) {
            // 修改缓存
            BlackList black = blackListMapper.selectById(blackList.getId());
            updateBlackListCache(black);
            return ResultData.success();
        }
        return ResultData.failure();
    }

    private void updateBlackListCache(BlackList blackList) {
        if (blackList.getType() == BlackListConst.BLACK_LIST_TYPE_BOT) {
            // 更新redis缓存
            myRedisCache.setCacheMapValue(RedisConst.BLACK_LIST_IP_KEY, blackList.getIpInfo().getCreateIp(), blackList.getExpiresTime());
        } else if (blackList.getType() == BlackListConst.BLACK_LIST_TYPE_USER) {
            myRedisCache.setCacheMapValue(RedisConst.BLACK_LIST_UID_KEY, blackList.getUserId().toString(), blackList.getExpiresTime());
        }
    }

    @Override
    @Transactional
    public ResultData<Void> deleteBlackList(List<Long> ids) {
        // 清除缓存
        blackListMapper.selectBatchIds(ids).forEach(blackList -> {
            if (blackList.getType() == BlackListConst.BLACK_LIST_TYPE_BOT) {
                // 清除缓存
                myRedisCache.deleteCacheMapValue(RedisConst.BLACK_LIST_IP_KEY, blackList.getIpInfo().getCreateIp());
            } else if (blackList.getType() == BlackListConst.BLACK_LIST_TYPE_USER) {
                myRedisCache.deleteCacheMapValue(RedisConst.BLACK_LIST_UID_KEY, blackList.getUserId().toString());
            }
        });
        if (this.removeBatchByIds(ids)) {
            return ResultData.success();
        }
        return ResultData.failure();
    }
}
