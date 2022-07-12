package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.Diary;
import ewha.efub.zeje.domain.DiaryRepository;
import ewha.efub.zeje.domain.Memory;
import ewha.efub.zeje.domain.MemoryRepository;
import ewha.efub.zeje.dto.diary.MemoryRequestDTO;
import ewha.efub.zeje.dto.diary.MemoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoryService {
    private final MemoryRepository memoryRepository;
    private final DiaryRepository diaryRepository;
    private final ImageUploadService imageUploadService;

    @Transactional
    public MemoryResponseDTO addMemory(Long diaryId, MemoryRequestDTO requestDTO, MultipartFile file) throws IOException {
        Diary diary = diaryRepository.findByDiaryId(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("다이어리가 없습니다. id= "+diaryId));

        if(!file.isEmpty()) {
            String fileUrl = imageUploadService.uploadImage(1, file);
            if(fileUrl.equals("type error")) return null;
            else if(fileUrl.equals("null error")) return null;

            return new MemoryResponseDTO(saveMemory(requestDTO, fileUrl, diary));
        }
        else {
            return new MemoryResponseDTO(saveMemory(requestDTO, diary));
        }
    }

    @Transactional
    public String deleteMemory(Long memoryId) {
        try{
            memoryRepository.deleteById(memoryId);
            return memoryId.toString() + "번 일기 삭제 완료";
        } catch (Exception e) {
            return e.toString();
        }
    }

    @Transactional
    public MemoryResponseDTO modifyMemory(Long memoryId, MemoryRequestDTO requestDTO) {
        Memory memory = memoryRepository.findMemoryByMemoryId(memoryId)
                .orElseThrow(() -> new IllegalArgumentException("일기가 없습니다. id= "+memoryId));

        memory.updateMemory(requestDTO);
        return new MemoryResponseDTO(memory);
    }

    @Transactional
    public MemoryResponseDTO findMemory(Long memoryId) {
        Memory memory = memoryRepository.findMemoryByMemoryId(memoryId)
                .orElseThrow(()-> new IllegalArgumentException("일기가 없습니다. id= "+memoryId));

        return new MemoryResponseDTO(memory);
    }

    @Transactional
    public List<MemoryResponseDTO> findMemoryList(Long diaryId) {
        return memoryRepository.findMemoriesByDiary_DiaryId(diaryId).stream()
                .map(MemoryResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Memory saveMemory(MemoryRequestDTO requestDTO, String fileUrl, Diary diary) {
        Memory memory = Memory.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .image(fileUrl)
                .diary(diary)
                .build();

        memoryRepository.save(memory);
        return memory;
    }

    @Transactional
    public Memory saveMemory(MemoryRequestDTO requestDTO, Diary diary) {
        Memory memory = Memory.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .diary(diary)
                .build();

        memoryRepository.save(memory);
        return memory;
    }

    public Boolean checkOwner(Long userId, Long diaryId) {
        return diaryRepository.existsByUser_UserIdAndDiaryId(userId, diaryId);
    }

    public Boolean checkDiary(Long diaryId, Long memoryId) {
        return memoryRepository.existsMemoryByDiary_DiaryIdAndMemoryId(diaryId, memoryId);
    }
}
