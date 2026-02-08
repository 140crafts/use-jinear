# Jinear Shared Libraries

This folder contains shared libraries that can be used across Jinear applications.

## jinear-rate-limiter

A reusable rate limiting library for Spring Boot applications using Redis and Bucket4j.

### Features

- Redis-backed distributed rate limiting
- Configurable rate limit plans (public, authenticated)
- Automatic Spring Boot auto-configuration
- Customizable client IP resolution

### Usage

1. Add dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>co.jinear</groupId>
    <artifactId>jinear-rate-limiter</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. Configure in `application.properties` or `application.yml`:

```yaml
jinear:
  security:
    rate-limit:
      enabled: true
      client-ip-header: X-Forwarded-For
      plans:
        public:
          capacity: 25
          duration-in-minutes: 1
          refill-type: GREEDY
        authenticated:
          capacity: 100
          duration-in-minutes: 1
          refill-type: GREEDY
```

3. The `RateLimitingFilter` is auto-configured and added to your Spring Security filter chain.

### Customization

#### Custom IP Resolver

Implement `ClientIpResolver` interface and register as a Spring bean:

```java
@Component
public class MyIpResolver implements ClientIpResolver {
    @Override
    public String resolveClientIp(HttpServletRequest request) {
        // Custom logic
        return request.getRemoteAddr();
    }
}
```

### Requirements

- Java 17+
- Spring Boot 3.0+
- Redis

