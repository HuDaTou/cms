package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.LeaveWordIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchLeaveWordDTO;
import com.overthinker.cloud.web.entity.PO.LeaveWord;
import com.overthinker.cloud.web.entity.VO.LeaveWordListVO;
import com.overthinker.cloud.web.entity.VO.LeaveWordVO;

import java.util.List;


/**
 * (LeaveWord)表服务接口
 *
 * @author overH
 * @since 2023-11-03 15:01:11
 */
public interface LeaveWordService extends IService<LeaveWord> {

    /**
     * 查询留言板
     * @return 留言板列表
     */
    List<LeaveWordVO> getLeaveWordList(String id);

    /**
     * 添加留言板
     *
     * @param content 留言内容
     * @return 是否成功
     */
    ResultData<Void> userLeaveWord(String content);

    /**
     * 后台留言列表
     * @return 结果
     */
    List<LeaveWordListVO> getBackLeaveWordList(SearchLeaveWordDTO searchDTO);

    /**
     * 是否通过留言
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResultData<Void> isCheckLeaveWord(LeaveWordIsCheckDTO isCheckDTO);

    /**
     * 删除留言
     * @param ids id 列表
     * @return 是否成功
     */
    ResultData<Void> deleteLeaveWord(List<Long> ids);
}
