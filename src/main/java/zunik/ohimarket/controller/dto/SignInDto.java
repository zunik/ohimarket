package zunik.ohimarket.controller.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
public class SignInDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
