package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.annotation.AccessLimit;
import com.overthinker.cloud.web.annotation.LogAnnotation;
import com.overthinker.cloud.web.controller.base.BaseController;
import com.overthinker.cloud.web.entity.DTO.RoleUserDTO;
import com.overthinker.cloud.web.entity.DTO.UserRoleDTO;
import com.overthinker.cloud.web.entity.VO.RoleAllVO;
import com.overthinker.cloud.web.entity.VO.RoleUserVO;
import com.overthinker.cloud.web.entity.constants.LogConst;
import com.overthinker.cloud.web.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/4 17:25
 */
@RestController
@Tag(name = "用户角色相关接口")
@RequestMapping("user/role")
@Validated
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;

    @PreAuthorize("hasAnyAuthority('system:user:role:list')")
    @Operation(summary = "查询拥有角色的用户列表")
    @Parameters({
            @Parameter(name = "roleId", description = "角色id"),
            @Parameter(name = "username", description = "用户名"),
            @Parameter(name = "email", description = "邮箱")
    })
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/user/list")
    public ResultData<List<RoleUserVO>> selectUser(
            @NotNull(message = "角色id不能为空")@RequestParam(name = "roleId") Long roleId,
            @RequestParam(required = false,name = "username") String username,
            @RequestParam(required = false,name = "email") String email
    ) {
        return messageHandler(() -> userRoleService.selectRoleUser(roleId,username,email,0));
    }

    @PreAuthorize("hasAnyAuthority('system:not:role:user:list')")
    @Operation(summary = "查询未拥有角色的用户列表")
    @Parameters({
            @Parameter(name = "roleId", description = "角色id"),
            @Parameter(name = "username", description = "用户名"),
            @Parameter(name = "email", description = "邮箱")
    })
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/not/user/list")
    public ResultData<List<RoleUserVO>> selectNotUserByRole(
            @NotNull(message = "角色id不能为空")@RequestParam(name = "roleId") Long roleId,
            @RequestParam(required = false,name = "username") String username,
            @RequestParam(required = false,name = "email") String email
    ) {
        return messageHandler(() -> userRoleService.selectRoleUser(roleId,username,email,1));
    }

    @PreAuthorize("hasAnyAuthority('system:user:role:add')")
    @Operation(summary = "添加用户角色关系")
    @Parameter(name = "userRoleDTO", description = "添加的数据")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="用户角色",operation= LogConst.GRANT)
    @PostMapping("/add")
    public ResultData<Void> addUserRole(@Valid @RequestBody UserRoleDTO userRoleDTO) {
        return userRoleService.addUserRole(userRoleDTO);
    }

    @PreAuthorize("hasAnyAuthority('system:user:role:delete')")
    @Operation(summary = "删除用户角色关系")
    @Parameter(name = "userRoleDTO", description = "删除的所需数据")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="用户角色",operation= LogConst.DELETE)
    @DeleteMapping("/delete")
    public ResultData<Void> deleteUserRole(@Valid @RequestBody UserRoleDTO userRoleDTO) {
        return userRoleService.deleteUserRole(userRoleDTO);
    }
    @PreAuthorize("hasAnyAuthority('system:role:user:list')")
    @Parameters({
            @Parameter(name = "permissionId", description = "权限id"),
            @Parameter(name = "roleName", description = "角色名称"),
            @Parameter(name = "roleKey", description = "角色键")
    })
    @AccessLimit(seconds = 60, maxCount = 30)
    @Operation(summary = "查询拥有用户的角色列表")
    @GetMapping("/role/list")
    public ResultData<List<RoleAllVO>> selectPermissionIdRole(
            @NotNull(message = "用户id不能为空") @RequestParam(name = "userId")  Long userId,
            @RequestParam(required = false, name = "roleName") String roleName,
            @RequestParam(required = false, name = "roleKey") String roleKey
    ) {
        return messageHandler(() -> userRoleService.selectRoleByUserId(userId,roleName,roleKey,0));
    }

    @PreAuthorize("hasAnyAuthority('system:user:role:not:list')")
    @Parameters({
            @Parameter(name = "userId", description = "用户id"),
            @Parameter(name = "roleName", description = "角色名称"),
            @Parameter(name = "roleKey", description = "角色键")
    })
    @AccessLimit(seconds = 60, maxCount = 30)
    @Operation(summary = "查询没有该用户的角色列表")
    @GetMapping("/not/role/list")
    public ResultData<List<RoleAllVO>> selectUserNotRole(
            @NotNull(message = "用户id不能为空")@RequestParam(name = "userId") Long userId,
            @RequestParam(required = false, name = "roleName") String roleName,
            @RequestParam(required = false, name = "roleKey") String roleKey
    ) {
        return messageHandler(() -> userRoleService.selectRoleByUserId(userId,roleName,roleKey,1));
    }

    @Operation(summary = "添加角色用户关系")
    @PreAuthorize("hasAnyAuthority('system:user:role:add')")
    @AccessLimit(seconds = 60, maxCount = 30)
    @Parameters({
            @Parameter(name = "roleUserDTO", description = "添加的数据")
    })
    @LogAnnotation(module="角色用户",operation= LogConst.GRANT)
    @PostMapping("/user/add")
    public ResultData<Void> addRoleUser(@Valid @RequestBody RoleUserDTO roleUserDTO) {
        return userRoleService.addRoleUser(roleUserDTO);
    }

    @Operation(summary = "删除角色用户关系")
    @PreAuthorize("hasAnyAuthority('system:user:role:delete')")
    @AccessLimit(seconds = 60, maxCount = 30)
    @Parameters({
            @Parameter(name = "roleUserDTO", description = "删除的所需数据")
    })
    @LogAnnotation(module="角色用户",operation= LogConst.DELETE)
    @DeleteMapping("/user/delete")
    public ResultData<Void> deleteRoleUser(@Valid @RequestBody RoleUserDTO roleUserDTO) {
        return userRoleService.deleteRoleUser(roleUserDTO);
    }
}
