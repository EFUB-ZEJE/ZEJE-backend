package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.Wish;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WishDTO {
    private Long wishId;
    private Long userId;
    private SpotDTO spotDTO;

    public WishDTO(Wish wish){
        this.wishId = wish.getWishId();
        this.userId = wish.getUser().getUserId();
        this.spotDTO = new SpotDTO(wish.getSpot());
    }
}
