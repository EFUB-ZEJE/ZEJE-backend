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

    @Builder
    public SpotReportRequestDTO(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }
}
