package antifraud.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernameStatus {
    @NotBlank
    private String username;

    @NotBlank
    @Pattern(regexp = "LOCK|UNLOCK")
    private String operation;
}
