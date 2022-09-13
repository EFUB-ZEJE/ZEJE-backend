package ewha.efub.zeje.dto;

import com.sun.istack.NotNull;
import ewha.efub.zeje.domain.Spot;
import ewha.efub.zeje.domain.WishRepository;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpotSearchDTO {

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
    private String mapX;
    private String mapY;
    private String image;
    private Boolean isWish;
    private Long wishCount;

    @Builder
    public SpotSearchDTO(Long contentId, String category, String type, String name, String location, String mapX, String mapY, String image, Long wishCount) {
        this.contentId = contentId;
        this.category = category;
        this.type = type;
        this.name = name;
        this.location = location;
        this.mapX = mapX;
        this.mapY = mapY;
        this.image = image;
        this.wishCount = wishCount;
    }

    public SpotSearchDTO(Spot spot) {
        this.spotId = spot.getSpotId();
        this.contentId = spot.getContentId();
        this.category = spot.getCategory();
        this.type = spot.getType();
        this.name = spot.getName();
        this.location = spot.getLocation();
        this.mapX = spot.getMapX();
        this.mapY = spot.getMapY();
        this.image = spot.getImage();
        this.wishCount = spot.getWishCount();
    }

    public SpotSearchDTO(Spot spot, Boolean isWish) {
        this.spotId = spot.getSpotId();
        this.contentId = spot.getContentId();
        this.category = spot.getCategory();
        this.type = spot.getType();
        this.name = spot.getName();
        this.location = spot.getLocation();
        this.mapX = spot.getMapX();
        this.mapY = spot.getMapY();
        this.image = spot.getImage();
        this.isWish = isWish;
        this.wishCount = spot.getWishCount();
    }

    public Spot toEntity() {
        return Spot.builder()
                .contentId(contentId)
                .category(category)
                .type(type)
                .name(name)
                .location(location)
                .mapX(mapX)
                .mapY(mapY)
                .image(image)
                .wishCount(wishCount)
                .build();
    }
}
