package co.jinear.core.controller;

import co.jinear.core.repository.MediaRepository;
import co.jinear.core.system.gcloud.storage.CloudStorage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/debug")
@RequiredArgsConstructor
public class DebugController {

    private final MediaRepository mediaRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {

//        List<Media> mediaList = mediaRepository.findAll();
//        mediaList.forEach(media -> {
//            try {
//                String newPath = FileStorageUtils.generatePath(media.getMediaOwnerType(), media.getRelatedObjectId(), media.getFileType(), media.getMediaKey());
//                CloudStorage.renameObject(media.getBucketName(), media.getStoragePath(), newPath);
//                CloudStorage.makeObjectPublic(media.getBucketName(),newPath);
//            } catch (Exception e) {
//                log.error("Error on media. mediaId: {}", media.getMediaId());
//            }
//        });
//        mediaList.forEach(media -> {
//            try {
//                String newPath = FileStorageUtils.generatePath(media.getMediaOwnerType(), media.getRelatedObjectId(), media.getFileType(), media.getMediaKey());
//                CloudStorage.makeObjectPublic(media.getBucketName(),newPath);
//            } catch (Exception e) {
//                log.error("Error on media. mediaId: {}", media.getMediaId());
//            }
//        });
//                CloudStorage.makeObjectPublic("jinear-b0",
//                        "FILE_STORAGE/WORKSPACE/01h3je8n45t3qhw6vfp0azctjf/PROFILE_PIC/01h4zdpnskwrc302zret9w7y37");
    }

    @GetMapping
    public void debug2(HttpServletResponse response) throws IOException {
//        String newUrl = "https://storage.googleapis.com/jinear-b0/FILE_STORAGE/WORKSPACE/01h3je8n45t3qhw6vfp0azctjf/PROFILE_PIC/01h4zdpnskwrc302zret9w7y37";
//        response.sendRedirect(newUrl);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(newUrl));
//        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

        String path = "FILE_STORAGE/WORKSPACE/01h3je8n45t3qhw6vfp0azctjf/PROFILE_PIC/Discord.dmg";
        CloudStorage.makeObjectPrivate("jinear-b0", path);
//        CloudStorage.renameObject("jinear-b0", path, newPath);
    }
}
