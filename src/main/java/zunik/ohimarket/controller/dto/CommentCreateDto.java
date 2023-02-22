package zunik.ohimarket.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentCreateDto {
    @NotNull
    private String postToken;

    @NotBlank
    private String content;
}
