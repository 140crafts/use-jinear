package co.jinear.core.system.gcloud.security;

import com.google.cloud.secretmanager.v1.*;
import com.google.protobuf.Timestamp;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@UtilityClass
public class SecretManager {

    private static final String PROJECT_ID = "selam-255606";

    private static SecretManagerServiceClient _c;

    private static SecretManagerServiceClient getClient() throws IOException {
        if (_c == null) {
            _c = SecretManagerServiceClient.create();
        }
        return _c;
    }

    public static Secret getSecret(String secretId) throws IOException {
        SecretManagerServiceClient client = getClient();
        SecretName secretName = SecretName.of(PROJECT_ID, secretId);
        return client.getSecret(secretName);
    }

    public static String getLastSecretVersionValue(String secretId) throws IOException {
        SecretManagerServiceClient client = getClient();
        SecretName projectName = SecretName.of(PROJECT_ID, secretId);
        SecretManagerServiceClient.ListSecretVersionsPagedResponse pagedResponse = client.listSecretVersions(projectName);

        Long _t = 0L;
        SecretVersion lastVersion = null;

        Iterable<SecretVersion> versions = pagedResponse.iterateAll();
        for (SecretVersion v : versions) {
            if (v.getState() != SecretVersion.State.ENABLED) {
                continue;
            }
            Timestamp ts = v.getCreateTime();
            if (_t < ts.getSeconds()) {
                _t = ts.getSeconds();
                lastVersion = v;
            }
        }
        AccessSecretVersionResponse response = client.accessSecretVersion(lastVersion.getName());
        return response.getPayload().getData().toStringUtf8();
    }
}
