package ewha.efub.zeje.dto.diary;

import com.sun.istack.NotNull;
import ewha.efub.zeje.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequestDTO {
    private String name;
    private String description;
}
