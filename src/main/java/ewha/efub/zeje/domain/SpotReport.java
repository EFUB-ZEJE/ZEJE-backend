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

    @Column(length = 300)
    private String image;

    @Column(length = 100)
    private String description;

    @Builder
    public SpotReport(String name, String type, String image, String description) {
        this.name = name;
        this.type = type;
        this.image = image;
        this.description = description;
    }
}
