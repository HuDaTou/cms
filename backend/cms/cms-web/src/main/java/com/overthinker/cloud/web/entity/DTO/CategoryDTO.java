package com.overthinker.cloud.web.entity.DTO;

import com.overthinker.cloud.web.entity.BaseData;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;


/**
 * @author overH
 * <p>
 * 创建时间：2024/1/4 11:07
 */
@Accessors(chain = true)
@Data
public class CategoryDTO implements BaseData {
    //分类id
    private Long id;
    //分类名
    @NotBlank(message = "分类名称不能为空")
    @Length(max = 20, message = "分类名称长度不能超过20")
    private String categoryName;
}
