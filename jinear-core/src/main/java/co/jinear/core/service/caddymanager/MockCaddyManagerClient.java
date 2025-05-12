package co.jinear.core.service.caddymanager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mock.caddymanager", havingValue = "true")
public class MockCaddyManagerClient implements CaddyManagerClient {

    @Override
    public boolean updateConfig(List<String> domainNameList) {
        return Boolean.TRUE;
    }
}
