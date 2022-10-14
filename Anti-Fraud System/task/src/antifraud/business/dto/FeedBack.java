package antifraud.business.dto;

import antifraud.business.response.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBack {
    @NotNull
    private long transactionId;

    @NotEmpty
    private String feedback;

    public static Answer toAnswer(String notAnswer) {
        switch (notAnswer) {
            case "ALLOWED":
                return Answer.ALLOWED;
            case "MANUAL_PROCESSING":
                return Answer.MANUAL_PROCESSING;
            case "PROHIBITED":
                return Answer.PROHIBITED;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
