package antifraud.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String role;

    @JsonProperty("role")
    private String strRole;

    @JsonIgnore
    private boolean locked;

    public void setRole(String role) {
        this.role = role;
        this.strRole = role.substring(5);
    }
}
