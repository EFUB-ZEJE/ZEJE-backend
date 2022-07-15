package ewha.efub.zeje.controller;

import ewha.efub.zeje.domain.Diary;
import ewha.efub.zeje.dto.diary.DiaryRequestDTO;
import ewha.efub.zeje.dto.diary.DiaryResponseDTO;
import ewha.efub.zeje.dto.diary.MemoryRequestDTO;
import ewha.efub.zeje.dto.diary.MemoryResponseDTO;
import ewha.efub.zeje.service.DiaryService;
import ewha.efub.zeje.service.MemoryService;
import ewha.efub.zeje.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {
    private final MemoryService memoryService;
    private final UserService userService;
    private final DiaryService diaryService;

    @PostMapping(value="/diaries")
    public String diaryAdd(@RequestParam String name, @RequestParam String description){
        Long userId = userService.findSessionUser();
        DiaryRequestDTO diaryRequestDTO = DiaryRequestDTO.builder()
                .name(name)
                .description(description)
                .build();
        return diaryService.addDiary(userId, diaryRequestDTO);
    }

    @GetMapping(value="/diaries")
    public List<DiaryResponseDTO> diaryList(){
        Long userId = userService.findSessionUser();
        return diaryService.findDiaryList(userId);
    }

    @DeleteMapping(value="/diaries/{diaryId}")
    public String diaryRemove(@PathVariable Long diaryId){
        Long userId = userService.findSessionUser();
        return diaryService.removeDiary(userId, diaryId);
    }

    @PatchMapping(value="/diaries/{diaryId}")
    public String diaryModify(@PathVariable Long diaryId, @RequestParam String name){
        Long userId = userService.findSessionUser();
        return diaryService.modifyDiaryName(userId, diaryId, name);
    }

    @GetMapping("/{diaryId}/memories")
    public List<MemoryResponseDTO> memoryList(@PathVariable Long diaryId) {
        Long userId = userService.findSessionUser();
        Boolean userFlag = memoryService.checkOwner(userId, diaryId);

        if(userFlag) {
            return memoryService.findMemoryList(diaryId);
        } else return null;
    }

    @GetMapping("/{diaryId}/memories/{memoryId}")
    public MemoryResponseDTO memoryDetails(@PathVariable Long diaryId, @PathVariable Long memoryId) {
        Boolean diaryFlag = memoryService.checkDiary(diaryId, memoryId);

        if(diaryFlag) {
            return memoryService.findMemory(memoryId);
        } else return null;
    }

    @PostMapping("/{diaryId}/memories")
    public MemoryResponseDTO memoryAdd(@PathVariable Long diaryId, @RequestParam (value="title") String title, @RequestParam (value="content") String content,
                                       @RequestParam (value="image", required = false)MultipartFile file) throws IOException {
        Long userId = userService.findSessionUser();
        Boolean userFlag = memoryService.checkOwner(userId, diaryId);

        if(userFlag) {
            MemoryRequestDTO dto = MemoryRequestDTO.builder()
                                                    .title(title).content(content)
                                                    .build();
            return memoryService.addMemory(diaryId, dto, file);
        } else return null;
    }

    @DeleteMapping("/{diaryId}/memories/{memoryId}")
    public String memoryDelete(@PathVariable Long diaryId, @PathVariable Long memoryId) {
        Long userId = userService.findSessionUser();
        Boolean userFlag = memoryService.checkOwner(userId, diaryId);
        Boolean diaryFlag = memoryService.checkDiary(diaryId, memoryId);

        if(userFlag && diaryFlag) {
            return memoryService.deleteMemory(memoryId);
        } else return "fail";
    }

    @PatchMapping("/{diaryId}/memories/{memoryId}")
    public MemoryResponseDTO memoryModify(@PathVariable Long diaryId, @PathVariable Long memoryId,
                                          @RequestBody MemoryRequestDTO requestDTO) {
        Long userId = userService.findSessionUser();
        Boolean userFlag = memoryService.checkOwner(userId, diaryId);
        Boolean diaryFlag = memoryService.checkDiary(diaryId, memoryId);

        if(userFlag && diaryFlag) {
            return memoryService.modifyMemory(memoryId, requestDTO);
        } else return null;
    }
}
