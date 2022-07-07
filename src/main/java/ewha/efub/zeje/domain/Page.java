package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pageId;

    @ManyToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Column
    @NotNull
    private String title;

    @Column
    @NotNull
    private String content;

    @Column
    private String image;

    @Column
    @NotNull
    private LocalDateTime createDate;
}
