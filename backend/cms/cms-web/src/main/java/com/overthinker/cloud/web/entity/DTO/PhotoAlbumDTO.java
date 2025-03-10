package com.overthinker.cloud.web.entity.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PhotoAlbumDTO {

    private Long id;

    private Long parentId;

    @NotEmpty(message = "相册名称不能为空")
    @Size(max = 20, message = "相册名称不能超过10个字符")
    private String name;

    private String description;
}
