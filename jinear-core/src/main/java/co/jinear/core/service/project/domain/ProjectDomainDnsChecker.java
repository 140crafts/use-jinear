package co.jinear.core.service.project.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDomainDnsChecker {

    private static final String JINEAR_CNAME_ADDR = "cname.jinear.co";

    public boolean matchesCname(String domainName) {
        try {
            InetAddress enquired = InetAddress.getByName(domainName);
            InetAddress cname = InetAddress.getByName(JINEAR_CNAME_ADDR);
            return Arrays.equals(cname.getAddress(), enquired.getAddress());
        } catch (Exception e) {
            log.error("Cname address match validation failed.", e);
            return false;
        }
    }
}
