package ewha.efub.zeje.dto.diary;

import com.sun.istack.NotNull;
import ewha.efub.zeje.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryResponseDTO {

    private Long diaryId;
    private Long userId;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private LocalDateTime createdDate;
    @NotNull
    private LocalDateTime updatedDate;

}
