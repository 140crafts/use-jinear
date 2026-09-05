package co.jinear.core.model.vo.auth;

public interface SessionCarrier {

    String sessionInfoId();

    default String localeName() {
        return null;
    }
}
