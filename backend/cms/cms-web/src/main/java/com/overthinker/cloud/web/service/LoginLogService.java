package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.LoginLogDTO;
import com.overthinker.cloud.web.entity.DTO.LoginLogDeleteDTO;
import com.overthinker.cloud.web.entity.PO.LoginLog;
import com.overthinker.cloud.web.entity.VO.LoginLogVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * (LoginLog)表服务接口
 *
 * @author overH
 * @since 2023-12-08 14:38:44
 */
public interface LoginLogService extends IService<LoginLog> {
    /**
     * 登录日志记录
     * @param request 请求
     * @param userName 用户名称
     * @param state 状态（0成功 1失败）
     * @param message 信息
     */
    void loginLog(HttpServletRequest request, String userName, Integer state,String message);

    /**
     * 搜索/显示登录日志
     * @param loginLogDTO 查询条件
     * @return  数据列表
     */
    List<LoginLogVO> searchLoginLog(LoginLogDTO loginLogDTO);

    /**
     * 删除登录日志
     * @param loginLogDeleteDTO 删除条件
     * @return 是否成功
     */
    ResultData<Void> deleteLoginLog(LoginLogDeleteDTO loginLogDeleteDTO);
}
