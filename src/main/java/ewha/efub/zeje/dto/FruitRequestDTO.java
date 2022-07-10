package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.Donation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FruitRequestDTO {
    private Integer fruitBox;

    @Builder
    public FruitRequestDTO (Integer fruitBox) {
        this.fruitBox = fruitBox;
    }
}
