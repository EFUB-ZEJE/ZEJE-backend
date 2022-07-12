package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import ewha.efub.zeje.dto.diary.MemoryRequestDTO;
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
@Table(name = "memory")
public class Memory extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoryId;

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

    @Builder
    public Memory(Diary diary, String title, String content, String image){
        this.diary = diary;
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}