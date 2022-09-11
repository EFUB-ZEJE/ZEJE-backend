package ewha.efub.zeje.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpotReportRequestDTO {
    private String name;
    private String type;
    private String description;
    private String mapX;
    private String mapY;
    private String location;

    @Builder
    public SpotReportRequestDTO(String name, String type, String description, String mapX, String mapY, String location) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.mapX = mapX;
        this.mapY = mapY;
        this.location = location;
    }
}
