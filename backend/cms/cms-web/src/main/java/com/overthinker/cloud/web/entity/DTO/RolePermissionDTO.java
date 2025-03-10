package com.overthinker.cloud.web.entity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/7 20:18
 */
@Data
public class RolePermissionDTO {
    @NotNull(message = "权限不能为空")
    private List<Long> permissionId;
    @NotNull(message = "选择的角色不能为空")
    private List<Long> roleId;
}
