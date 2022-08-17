package ewha.efub.zeje.dto;

import com.sun.istack.NotNull;
import ewha.efub.zeje.domain.Spot;
import lombok.Builder;

public class SpotUserResponseDTO {
    @NotNull
    private Long spotId;
    private Long contentId;
    @NotNull
    private String category;
    private String type;
    @NotNull
    private String name;
    @NotNull
    private String location;
    private String description;
    private String link;
    private String mapX;
    private String mapY;
    private Boolean todayVisit;

    @Builder
    public SpotUserResponseDTO(Long contentId, String category, String type, String name, String location, String description, String link, String mapX, String mapY, Boolean todayVisit) {
        this.contentId = contentId;
        this.category = category;
        this.type = type;
        this.name = name;
        this.location = location;
        this.description = description;
        this.link = link;
        this.mapX = mapX;
        this.mapY = mapY;
        this.todayVisit = todayVisit;
    }

}
