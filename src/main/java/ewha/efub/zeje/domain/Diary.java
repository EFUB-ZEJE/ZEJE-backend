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
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "diary")
public class Diary extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @NotNull
    private String name;

    @Column
    private String description;

    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Memory> memories = new ArrayList<>();

    @Builder
    public Diary(User user, String name, String description) {
        this.user = user;
        this.name = name;
        this.description = description;
    }

    public Long getUserId(){
        return this.getUser().getUserId();
    }

    public void updateName(String name){
        this.name = name;
    }
}
