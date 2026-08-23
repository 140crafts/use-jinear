package co.jinear.core.config.startup;

import co.jinear.core.config.properties.GenericJinearProperties;
import co.jinear.core.service.account.ManagementAccountOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementStartupRunner implements ApplicationRunner {

    private final ManagementAccountOperationService managementAccountOperationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        managementAccountOperationService.syncAdminAccount();
    }
}
