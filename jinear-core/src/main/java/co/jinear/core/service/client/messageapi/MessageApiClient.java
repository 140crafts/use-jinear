package co.jinear.core.service.client.messageapi;

import co.jinear.core.service.client.messageapi.model.request.EmitRequest;

public interface MessageApiClient {

    void emit(EmitRequest emitRequest);
}
