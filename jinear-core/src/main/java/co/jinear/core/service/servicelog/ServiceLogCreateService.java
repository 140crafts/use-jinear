package co.jinear.core.service.servicelog;

import co.jinear.core.model.entity.servicelog.ServiceLog;
import co.jinear.core.repository.ServiceLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class ServiceLogCreateService {

    private final ServiceLogRepository serviceLogRepository;

    public ServiceLogCreateService(ServiceLogRepository serviceLogRepository) {
        this.serviceLogRepository = serviceLogRepository;
    }

    @Async
    public Future<ServiceLog> create(ServiceLog serviceLog) {
        return CompletableFuture.completedFuture(serviceLogRepository.save(serviceLog));
    }
}
