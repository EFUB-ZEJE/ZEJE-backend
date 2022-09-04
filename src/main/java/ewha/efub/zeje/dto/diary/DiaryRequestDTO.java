package ewha.efub.zeje.dto.diary;

import com.sun.istack.NotNull;
import ewha.efub.zeje.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequestDTO {
    private String name;
    private String description;
}
