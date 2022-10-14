package antifraud.business;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ips")
public class Ip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String ip;
}
