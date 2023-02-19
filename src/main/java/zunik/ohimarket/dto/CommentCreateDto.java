package zunik.ohimarket.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentCreateDto {
    @NotNull
    private Long postId;

    @NotBlank
    private String content;
}
