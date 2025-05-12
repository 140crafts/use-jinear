package co.jinear.core.model.entity.servicelog;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "service_log")
public class ServiceLog extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "service_log_id")
    private String serviceLogId;

    @Column(name = "direction")
    private String direction;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "uri")
    private String uri;

    @Column(name = "request_headers")
    private String requestHeaders;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_time")
    private Date requestTime;

    @Column(name = "request_payload")
    private String requestPayload;

    @Column(name = "response_headers")
    private String responseHeaders;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "response_time")
    private Date responseTime;

    @Column(name = "response_payload")
    private String responsePayload;

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "status_text")
    private String statusText;

    @Column(name = "method")
    private String method;
}
