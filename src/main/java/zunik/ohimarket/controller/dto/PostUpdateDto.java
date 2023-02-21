package zunik.ohimarket.controller.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostUpdateDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private String postCategoryName;

    private MultipartFile image;

    private String deleteImg;
}
