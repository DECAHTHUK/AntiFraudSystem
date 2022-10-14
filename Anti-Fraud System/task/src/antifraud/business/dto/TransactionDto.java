package antifraud.business.dto;

import antifraud.business.response.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    @NotNull
    private long amount;

    @NotEmpty
    @Pattern(regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")
    private String ip;

    @NotEmpty
    private String number;

    @NotEmpty
    private String region;

    @NotEmpty
    private String date;

}
