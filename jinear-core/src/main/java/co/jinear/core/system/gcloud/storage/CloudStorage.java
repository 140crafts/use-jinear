package co.jinear.core.system.gcloud.storage;

import com.google.cloud.storage.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class CloudStorage {

    private static final String CACHE_CONTROL = "max-age=2629743";
    private static final String DEFAULT_CONTENT_TYPE = "image/jpeg";

    @Getter
    @Setter
    private static String projectId;

    private static Storage __storage;

    private static Storage getStorage() {
        if (__storage == null) {
            __storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        }
        return __storage;
    }

    public static void uploadObject(String bucketName, String objectName, MultipartFile file) throws IOException {
        Storage storage = getStorage();
        BlobId blobId = BlobId.of(bucketName, objectName);
        String contentType = Optional.of(file)
                .map(MultipartFile::getContentType)
                .orElse(DEFAULT_CONTENT_TYPE);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setCacheControl(CACHE_CONTROL)
                .setContentType(contentType)
                .build();
        storage.create(blobInfo, file.getBytes());
    }

    public URL generateV4PutSignedUrl(String bucketName, String objectName, String contentType, Long signedUrlDuration, long fileSizeInBytes) {
        Storage storage = getStorage();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        return storage.signUrl(
                blobInfo,
                signedUrlDuration,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withExtHeaders(Collections.singletonMap("Content-Length", String.valueOf(fileSizeInBytes))),
                Storage.SignUrlOption.withV4Signature()
        );
    }

    public URL generateV4GetSignedUrl(String bucketName, String objectName, Long signedUrlDuration) {
        Storage storage = getStorage();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();
        return storage.signUrl(
                blobInfo,
                signedUrlDuration,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature()
        );
    }

    public static void makeObjectPublic(String bucketName, String objectName) {
        Storage storage = getStorage();
        BlobId blobId = BlobId.of(bucketName, objectName);
        storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
    }

    public static void makeObjectPrivate(String bucketName, String objectName) {
        Storage storage = getStorage();
        BlobId blobId = BlobId.of(bucketName, objectName);
        Blob blob = storage.get(blobId);
        blob.deleteAcl(blob.getAcl(Acl.User.ofAllUsers()).getEntity());
    }

    public static void deleteObject(String bucketName, String objectName) {
        Storage storage = getStorage();
        storage.delete(bucketName, objectName);
    }

    public static void renameObject(String bucketName, String objectName, String newObjectName) {
        Storage storage = getStorage();
        BlobId source = BlobId.of(bucketName, objectName);
        BlobId target = BlobId.of(bucketName, newObjectName);
        Storage.BlobTargetOption precondition = Storage.BlobTargetOption.doesNotExist();
        storage.copy(Storage.CopyRequest.newBuilder().setSource(source).setTarget(target, precondition).build());
        storage.get(target);
        storage.get(source).delete();
    }

    public boolean doesObjectExists(String bucketName, String objectName) {
        Storage storage = getStorage();
        Blob blob = storage.get(bucketName, objectName, Storage.BlobGetOption.fields());
        return Objects.nonNull(blob);
    }
}
