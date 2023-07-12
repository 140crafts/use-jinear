package co.jinear.core.system.gcloud.storage;

import com.google.cloud.storage.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@UtilityClass
public class CloudStorage {

    private static final String CACHE_CONTROL = "max-age=2629743";
    private static final String DEFAULT_CONTENT_TYPE = "image/jpeg";

    @Getter
    @Setter
    private static String projectId;

    private static Storage storage;


    private static Storage getStorage() {
        if (storage == null) {
            storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        }
        return storage;
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
}
