package ewha.efub.zeje.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "spot_report")
public class SpotReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotReportId;

    @NotNull
    @Column(length = 45)
    private String name;

    @NotNull
    @Column(length = 45)
    private String type;

    @Column(length = 500)
    private String image;

    @Column(length = 100)
    private String description;

    @NotNull
    @Column(name = "map_x", length = 45)
    private String mapX;

    @NotNull
    @Column(name = "map_y", length = 45)
    private String mapY;

    @NotNull
    @Column
    private String location;

    @Builder
    public SpotReport(String name, String type, String image, String description, String mapX, String mapY, String location) {
        this.name = name;
        this.type = type;
        this.image = image;
        this.description = description;
        this.mapX = mapX;
        this.mapY = mapY;
        this.location = location;
    }
}
