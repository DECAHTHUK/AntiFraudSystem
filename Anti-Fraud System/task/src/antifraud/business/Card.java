package antifraud.business;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String number;

    public Card(String number) {
        if (!checkNumber(number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        this.number = number;
    }

    public static boolean checkNumber(String number) {
        if (number.length() != 16) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String payload = number.substring(0, number.length() - 1);
        int cnt = 0;
        int sum = 0;
        for (int i = payload.length() - 1; i >= 0; i--) {
            int num = Integer.parseInt(String.valueOf(payload.charAt(i)));
            if (cnt % 2 == 0) {
                if (num * 2 >= 10) {
                    char[] a = String.valueOf(num * 2).toCharArray();
                    sum += Integer.parseInt(String.valueOf(a[0])) + Integer.parseInt(String.valueOf(a[1]));
                } else {
                    sum += num * 2;
                }
            } else {
                sum += num;
            }
            cnt++;
        }
        int check = (10 - sum % 10) % 10;
        return check == Integer.parseInt(String.valueOf(number.charAt(number.length() - 1)));
    }
}
