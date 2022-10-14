package antifraud.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
