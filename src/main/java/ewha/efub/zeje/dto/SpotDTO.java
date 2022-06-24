package ewha.efub.zeje.dto;

import com.sun.istack.NotNull;
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
    @NotNull
    private String name;
    @NotNull
    private String location;
    private String description;
    private String link;

}
