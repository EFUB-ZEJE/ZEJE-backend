package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
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

    @NotNull
    @CreatedDate
    private LocalDateTime createDate;

    @Builder
    Page(Diary diary, String title, String content, String image){
        this.diary = diary;
        this.title = title;
        this.content = content;
        this.image = image;
    }
}