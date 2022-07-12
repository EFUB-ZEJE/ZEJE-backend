package ewha.efub.zeje.dto.diary;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryRequestDTO {
    private String title;
    private String content;

    @Builder
    public MemoryRequestDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
