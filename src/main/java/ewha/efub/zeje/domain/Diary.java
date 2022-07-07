package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @NotNull
    private String name;

    @Column
    private String description;

    @Column
    @NotNull
    private LocalDateTime createdDate;

    @Column
    @NotNull
    private LocalDateTime updatedDate;
}
