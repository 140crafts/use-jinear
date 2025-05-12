package co.jinear.core.system.gcloud.vision;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class CloudVision {

    public static GVisionApiResponse analyzeImage(MultipartFile file) {
        if (file != null) {
            try {
                List<AnnotateImageRequest> requests = new ArrayList<>();

                ByteString imgBytes = ByteString.readFrom(new ByteArrayInputStream(file.getBytes()));
                Image img = Image.newBuilder().setContent(imgBytes).build();

                Feature featSafeSearchDetection = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
                Feature featFaceDetection = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();

                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(featSafeSearchDetection)
                        .addFeatures(featFaceDetection)
                        .setImage(img)
                        .build();

                requests.add(request);

                try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                    BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                    List<AnnotateImageResponse> responses = response.getResponsesList();

                    for (AnnotateImageResponse res : responses) {
                        if (res.hasError()) {
                            log.error("Error: %s%n", res.getError().getMessage());
                            return null;
                        }

                        // For full list of available annotations, see http://g.co/cloud/vision/docs
                        SafeSearchAnnotation annotation = res.getSafeSearchAnnotation();

                        Likelihood adult = annotation.getAdult();
                        Likelihood medical = annotation.getMedical();
                        Likelihood spoof = annotation.getSpoof();
                        Likelihood violence = annotation.getViolence();
                        Likelihood racy = annotation.getRacy();

                        Integer angerLikelihood = Likelihood.UNKNOWN.getNumber();
                        Integer joyLikelihood = Likelihood.UNKNOWN.getNumber();
                        Integer surpiseLikelihood = Likelihood.UNKNOWN.getNumber();

                        List<FaceAnnotation> faceAnnotationList = res.getFaceAnnotationsList();
                        for (FaceAnnotation fa : faceAnnotationList) {
                            angerLikelihood = fa.getAngerLikelihood().getNumber();
                            joyLikelihood = fa.getJoyLikelihood().getNumber();
                            surpiseLikelihood = fa.getSurpriseLikelihood().getNumber();
                            break;
                        }
                        return new GVisionApiResponse(
                                adult.getNumber(),
                                medical.getNumber(),
                                spoof.getNumber(),
                                violence.getNumber(),
                                racy.getNumber(),
                                angerLikelihood.intValue(),
                                joyLikelihood.intValue(),
                                surpiseLikelihood.intValue(),
                                (faceAnnotationList != null && faceAnnotationList.size() > 0));
                    }
                }

                return null;
            } catch (Exception e) {
                log.error("File cannot read", e);
            }
        }
        return null;
    }
}
