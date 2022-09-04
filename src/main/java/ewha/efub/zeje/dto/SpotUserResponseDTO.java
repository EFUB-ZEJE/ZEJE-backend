package ewha.efub.zeje.dto;

import com.sun.istack.NotNull;
import ewha.efub.zeje.domain.Spot;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpotUserResponseDTO {
    @NotNull
    private Long spotId;
    @NotNull
    private String name;
    @NotNull
    private String location;
    private String mapX;
    private String mapY;
    private Boolean todayVisit;

    @Builder
    public SpotUserResponseDTO(Long spotId, String name, String location, String mapX, String mapY, Boolean todayVisit) {
        this.spotId = spotId;
        this.name = name;
        this.location = location;
        this.mapX = mapX;
        this.mapY = mapY;
        this.todayVisit = todayVisit;
    }

}
