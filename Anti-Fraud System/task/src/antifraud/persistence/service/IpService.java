package antifraud.persistence.service;

import antifraud.business.Ip;
import antifraud.persistence.IpRepository;
import antifraud.business.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IpService {
    private final IpRepository ipRepository;

    @Autowired
    public IpService(IpRepository ipRepository) {
        this.ipRepository = ipRepository;
    }

    public Ip save(Ip ip) {
        Ip check = ipRepository.findByIp(ip.getIp());
        if (check != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        ipRepository.save(ip);
        return ipRepository.findByIp(ip.getIp());
    }

    public Status delete(String ip) {
        Ip out = ipRepository.findByIp(ip);
        if (out == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ipRepository.delete(out);
        return new Status(out);
    }

    public List<Ip> findAll() {
        return ipRepository.findAll();
    }

    public Ip findByIp(String ip) {
        return ipRepository.findByIp(ip);
    }
}
