package co.jinear.core.service.caddymanager;

import java.util.List;

public interface CaddyManagerClient {

    boolean updateConfig(List<String> domainNameList);
}
