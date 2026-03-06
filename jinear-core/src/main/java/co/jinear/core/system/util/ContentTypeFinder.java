package co.jinear.core.system.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@UtilityClass
public class ContentTypeFinder {

    public static UrlContentInfo findUrlContentInfo(URL url) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url.toURI())
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

            String contentType = response.headers().firstValue("Content-Type").orElse(null);
            String fileExtension = getFileExtensionFromContentType(contentType);

            if (!StringUtils.hasText(contentType)) {
                Optional<String> contentDisposition = response.headers().firstValue("Content-Disposition");
                if (contentDisposition.isPresent()) {
                    ContentDisposition disposition = ContentDisposition.parse(contentDisposition.get());
                    String filename = disposition.getFilename();
                    if (StringUtils.hasText(filename)) {
                        Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(filename);
                        if (mediaType.isPresent()) {
                            contentType = mediaType.get().toString();
                            fileExtension = StringUtils.getFilenameExtension(filename);
                        }
                    }
                }
            }

            long contentLength = response.headers().firstValueAsLong("Content-Length").orElse(0L);

            return UrlContentInfo.builder()
                    .contentType(contentType)
                    .fileExtension(fileExtension)
                    .contentLength(contentLength)
                    .build();
        } catch (Exception e) {
            log.error("Find content type has failed.", e);
            return null;
        }
    }

    public static String getFileExtensionFromContentType(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return null;
        }
        return switch (contentType) {
            case "video/mp4" -> "mp4";
            case "video/quicktime" -> "mov";
            case "image/jpeg" -> "jpeg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/webp" -> "webp";
            default -> null;
        };
    }
}
