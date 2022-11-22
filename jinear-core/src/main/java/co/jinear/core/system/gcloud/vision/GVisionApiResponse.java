package co.jinear.core.system.gcloud.vision;

import com.google.cloud.vision.v1.Likelihood;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class GVisionApiResponse {

    @Getter
    @Setter
    private Boolean isSafe;

    @Getter
    private Boolean containsFace;

    /*
     * UNKNOWN       ( 0 )
     * VERY_UNLIKELY ( 1 )
     * UNLIKELY      ( 2 )
     * POSSIBLE      ( 3 )
     * LIKELY        ( 4 )
     * VERY_LIKELY   ( 5 )
     * UNRECOGNIZED  (-1 )
     * */
    @Getter
    private Integer adult;
    @Getter
    private Integer medical;
    @Getter
    private Integer spoof;
    @Getter
    private Integer violence;
    @Getter
    private Integer racy;
    @Getter
    private Integer angerLikelihood;
    @Getter
    private Integer joyLikelihood;
    @Getter
    private Integer surpriseLikelihood;

    public GVisionApiResponse(int adult, int medical, int spoof, int violence, int racy, int angerLikelihood, int joyLikelihood, int surpriseLikelihood, boolean containsFace) {
        this.adult = adult;
        this.medical = medical;
        this.spoof = spoof;
        this.violence = violence;
        this.racy = racy;

        this.angerLikelihood = angerLikelihood;
        this.joyLikelihood = joyLikelihood;
        this.surpriseLikelihood = surpriseLikelihood;

        this.containsFace = containsFace;

        this.isSafe = !((adult == Likelihood.LIKELY.getNumber() || adult == Likelihood.VERY_LIKELY.getNumber()) ||
                (medical == Likelihood.LIKELY.getNumber() || medical == Likelihood.VERY_LIKELY.getNumber()) ||
                (spoof == Likelihood.LIKELY.getNumber() || spoof == Likelihood.VERY_LIKELY.getNumber()) ||
                (violence == Likelihood.LIKELY.getNumber() || violence == Likelihood.VERY_LIKELY.getNumber()));
        //(racy.getNumber() == Likelihood.LIKELY.getNumber() || racy.getNumber() == Likelihood.VERY_LIKELY.getNumber())
    }
}
