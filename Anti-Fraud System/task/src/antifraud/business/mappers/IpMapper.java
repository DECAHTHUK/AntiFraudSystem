package antifraud.business.mappers;

import antifraud.business.Ip;
import antifraud.business.dto.IpDto;
import org.springframework.stereotype.Component;

@Component
public class IpMapper {
    public Ip toIp(IpDto ipDto) {
        Ip ip = new Ip();
        ip.setIp(ipDto.getIp());
        return ip;
    }
}
