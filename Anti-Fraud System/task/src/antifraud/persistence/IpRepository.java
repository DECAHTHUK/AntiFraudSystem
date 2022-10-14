package antifraud.persistence;

import antifraud.business.Ip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IpRepository extends JpaRepository<Ip, Long> {
    Ip findByIp(String ip);

    List<Ip> findAll();
}
