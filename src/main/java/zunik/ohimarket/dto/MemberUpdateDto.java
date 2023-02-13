package zunik.ohimarket.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class MemberUpdateDto {
    @NotBlank
    private String nickname;
    @Size(max=140)
    private String introduction;
}
