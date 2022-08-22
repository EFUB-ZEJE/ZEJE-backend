package ewha.efub.zeje.dto;

import com.sun.istack.NotNull;
import ewha.efub.zeje.domain.Spot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotDTO {

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
    private String image;

    @Builder
    public SpotDTO(Long contentId, String category, String type, String name, String location, String description, String link, String mapX, String mapY, String image) {
        this.contentId = contentId;
        this.category = category;
        this.type = type;
        this.name = name;
        this.location = location;
        this.description = description;
        this.link = link;
        this.mapX = mapX;
        this.mapY = mapY;
        this.image = image;
    }

    public SpotDTO(Spot spot) {
        this.spotId = spot.getSpotId();
        this.contentId = spot.getContentId();
        this.category = spot.getCategory();
        this.type = spot.getType();
        this.name = spot.getName();
        this.location = spot.getLocation();
        this.description = spot.getDescription();
        this.link = spot.getLink();
        this.mapX = spot.getMapX();
        this.mapY = spot.getMapY();
        this.image = spot.getImage();
    }

    public Spot toEntity() {
        return Spot.builder()
                .contentId(contentId)
                .category(category)
                .type(type)
                .name(name)
                .location(location)
                .description(description)
                .link(link)
                .mapX(mapX)
                .mapY(mapY)
                .image(image)
                .build();
    }
}
