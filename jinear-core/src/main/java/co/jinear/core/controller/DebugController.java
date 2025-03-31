package co.jinear.core.controller;

import co.jinear.core.config.properties.MinIoProperties;
import co.jinear.core.repository.project.ProjectRepository;
import co.jinear.core.service.project.ProjectFeedSettingsOperationService;
import io.minio.DeleteBucketPolicyArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/v1/debug")
@RequiredArgsConstructor
public class DebugController {

    private final ProjectRepository projectRepository;
    private final MinIoProperties minIoProperties;
    private final ProjectFeedSettingsOperationService projectFeedSettingsOperationService;


    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity, @RequestParam("file") MultipartFile file) throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("vCsmVvttu1rMyezKMOW7", "4GhpyTb1zMRwXCFRTcbbrJrmNH45HXxxEoFXtCvI")
                .build();

        String path = "FILE_STORAGE/USER/01gqanwmvm2anvf2zh8v1zamkx/PROFILE_PIC/01h167rtyyx8t66xwnqx2m179p/4c378a4c-ec23-4b3e-be0b-542436e8c189/" + file.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("fs1")
                        .object(path)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        String policy = String.format("""
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": {"AWS": ["*"]},
                            "Action": ["s3:GetObject"],
                            "Resource": ["arn:aws:s3:::%s/%s"]
                        }
                    ]
                }
                """, "fs1", path);

        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket("fs1")
                        .config(policy)
                        .build()
        );

        minioClient.deleteBucketPolicy(DeleteBucketPolicyArgs.builder()
                .bucket("fs1")

                .build());
    }

    @GetMapping
    public Object debug2(HttpServletResponse response) {
        return null;
    }
}
