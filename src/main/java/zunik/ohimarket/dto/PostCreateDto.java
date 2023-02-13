package zunik.ohimarket.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostCreateDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private String postCategoryName;
}
