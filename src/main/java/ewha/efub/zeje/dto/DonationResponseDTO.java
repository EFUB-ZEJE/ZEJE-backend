package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.Donation;
import lombok.Getter;

@Getter
public class DonationResponseDTO {
    private Long donationId;
    private Long userId;
    private Integer fruit;

    public DonationResponseDTO(Donation entity) {
        this.donationId = entity.getDonationId();
        this.userId = entity.getUser().getUserId();
        this.fruit = entity.getFruit();
    }

    @Getter
    public static class DonationTotalResponseDTO {
        private Long userId;
        private Integer fruitTotal;

        public DonationTotalResponseDTO(Long userId, Integer fruitTotal) {
            this.userId = userId;
            this.fruitTotal = fruitTotal;
        }
    }
}
