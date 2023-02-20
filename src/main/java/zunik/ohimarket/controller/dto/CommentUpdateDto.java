package zunik.ohimarket.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentUpdateDto {
    @NotBlank
    private String content;
}
