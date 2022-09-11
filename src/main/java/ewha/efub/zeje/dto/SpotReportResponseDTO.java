package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.SpotReport;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpotReportResponseDTO {
    private Long spotReportId;
    private String name;
    private String type;
    private String image;
    private String description;

    public SpotReportResponseDTO(SpotReport spotReport) {
        this.spotReportId = spotReport.getSpotReportId();
        this.name = spotReport.getName();
        this.type = spotReport.getType();
        this.image = spotReport.getImage();
        this.description = spotReport.getDescription();
    }
}
