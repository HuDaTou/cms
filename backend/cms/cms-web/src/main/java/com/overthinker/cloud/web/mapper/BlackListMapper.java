package com.overthinker.cloud.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.overthinker.cloud.web.entity.PO.BlackList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;



/**
 * (BlackList)表数据库访问层
 *
 * @author overH
 * @since 2024-09-05 16:13:19
 */
public interface BlackListMapper extends BaseMapper<BlackList> {

    @Delete("DELETE FROM t_black_list WHERE ip_info -> '$.createIp' = #{ip}")
    Long deleteByIp(String ip);

    // 查询是否存在ip
    @Select("SELECT id FROM t_black_list WHERE ip_info -> '$.createIp' = #{ip}")
    Long getIdByIp(String ip);
}
