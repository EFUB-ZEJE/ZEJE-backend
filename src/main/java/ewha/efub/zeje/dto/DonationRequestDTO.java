package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.Donation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationRequestDTO {
    private Integer fruit;

    @Builder
    public DonationRequestDTO(Integer fruit) {
        this.fruit = fruit;
    }
}
