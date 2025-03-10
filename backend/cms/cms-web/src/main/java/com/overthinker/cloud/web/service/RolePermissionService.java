package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.RolePermissionDTO;
import com.overthinker.cloud.web.entity.PO.RolePermission;
import com.overthinker.cloud.web.entity.VO.RoleAllVO;

import java.util.List;


/**
 * (RolePermission)表服务接口
 *
 * @author overH
 * @since 2023-10-13 15:02:40
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 所有使用该权限的角色
     *
     * @param permissionId 权限id
     * @param roleName 角色名称
     * @param roleKey 角色字符
     * @param type 角色类型,0：该角色的使用用户 1：该角色的未使用用户
     * @return 角色列表
     */
    List<RoleAllVO> selectRoleByPermissionId(Long permissionId, String roleName, String roleKey, Integer type);

    /**
     * 给多个角色添加某个权限
     * @param rolePermissionDTO 角色权限数据
     * @return 是否成功
     */
    ResultData<Void> addRolePermission(RolePermissionDTO rolePermissionDTO);

    /**
     * 批量或单个取消授权
     * @param rolePermissionDTO 角色权限数据
     * @return 是否成功
     */
    ResultData<Void> deleteRolePermission(RolePermissionDTO rolePermissionDTO);
}
