package antifraud.business.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Delete {
    String username;

    String status;
}
