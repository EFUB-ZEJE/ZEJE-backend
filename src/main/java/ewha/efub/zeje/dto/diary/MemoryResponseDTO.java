package ewha.efub.zeje.dto.diary;

import ewha.efub.zeje.domain.Memory;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MemoryResponseDTO {
    private Long memoryId;
    private Long diaryId;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdDate;

    public MemoryResponseDTO(Memory entity) {
        this.memoryId = entity.getMemoryId();
        this.diaryId = entity.getDiary().getDiaryId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imageUrl = entity.getImage();
        this.createdDate = entity.getCreatedDate();
    }
}
