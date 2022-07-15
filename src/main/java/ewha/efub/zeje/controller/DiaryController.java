package ewha.efub.zeje.controller;

import ewha.efub.zeje.config.LoginUser;
import ewha.efub.zeje.dto.diary.MemoryRequestDTO;
import ewha.efub.zeje.dto.diary.MemoryResponseDTO;
import ewha.efub.zeje.dto.security.SessionUserDTO;
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

    @GetMapping("/{diaryId}/memories")
    public List<MemoryResponseDTO> memoryList(@LoginUser SessionUserDTO sessionUser, @PathVariable Long diaryId) {
        Boolean userFlag = memoryService.checkOwner(sessionUser.getUserId(), diaryId);

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
    public MemoryResponseDTO memoryAdd(@LoginUser SessionUserDTO sessionUser, @PathVariable Long diaryId,
                                       @RequestParam (value="title") String title, @RequestParam (value="content") String content,
                                       @RequestParam (value="image", required = false)MultipartFile file) throws IOException {
        Boolean userFlag = memoryService.checkOwner(sessionUser.getUserId(), diaryId);

        if(userFlag) {
            MemoryRequestDTO dto = MemoryRequestDTO.builder()
                                                    .title(title).content(content)
                                                    .build();
            return memoryService.addMemory(diaryId, dto, file);
        } else return null;
    }

    @DeleteMapping("/{diaryId}/memories/{memoryId}")
    public String memoryDelete(@LoginUser SessionUserDTO sessionUser, @PathVariable Long diaryId, @PathVariable Long memoryId) {
        Boolean userFlag = memoryService.checkOwner(sessionUser.getUserId(), diaryId);
        Boolean diaryFlag = memoryService.checkDiary(diaryId, memoryId);

        if(userFlag && diaryFlag) {
            return memoryService.deleteMemory(memoryId);
        } else return "fail";
    }

    @PatchMapping("/{diaryId}/memories/{memoryId}")
    public MemoryResponseDTO memoryModify(@LoginUser SessionUserDTO sessionUser, @PathVariable Long diaryId, @PathVariable Long memoryId,
                                          @RequestBody MemoryRequestDTO requestDTO) {
        Boolean userFlag = memoryService.checkOwner(sessionUser.getUserId(), diaryId);
        Boolean diaryFlag = memoryService.checkDiary(diaryId, memoryId);

        if(userFlag && diaryFlag) {
            return memoryService.modifyMemory(memoryId, requestDTO);
        } else return null;
    }
}
