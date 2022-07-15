package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.Diary;
import ewha.efub.zeje.domain.DiaryRepository;
import ewha.efub.zeje.domain.MemoryRepository;
import ewha.efub.zeje.domain.UserRepository;
import ewha.efub.zeje.dto.diary.DiaryRequestDTO;
import ewha.efub.zeje.dto.diary.DiaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public String addDiary(Long userId, DiaryRequestDTO diaryRequestDTO){

        Diary diary = Diary.builder()
                .name(diaryRequestDTO.getName())
                .user(userRepository.findById(userId).get())
                .description(diaryRequestDTO.getDescription())
                .build();
        diaryRepository.save(diary);

        return diary.getDiaryId().toString();
    }

    public List<DiaryResponseDTO> findDiaryList(Long userId){
        modelMapper.typeMap(Diary.class, DiaryResponseDTO.class).addMappings(mapper -> {
            mapper.map(Diary::getUserId, DiaryResponseDTO::setUserId);
        });

        return diaryRepository.findByUser_UserId(userId)
                .stream()
                .map(diary -> modelMapper.map(diary, DiaryResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public String modifyDiaryName(Long userId, Long diaryId, String name){
        Optional<Diary> diaryOptional = diaryRepository.findByUser_userIdAndDiaryId(userId, diaryId);
        Diary diary = diaryOptional.get();
        if(diary == null) return "유효하지 않은 다이어리입니다.";

        diary.updateName(name);
        return diary.getDiaryId().toString();
    }

    @Transactional
    public String removeDiary(Long userId, Long diaryId){
        if(diaryRepository.existsByUser_UserIdAndDiaryId(userId, diaryId)){
            diaryRepository.deleteById(diaryId);
            return diaryId.toString()+"번 다이어리 삭제 완료";
        } else {
            return "유효하지 않은 다이어리입니다.";
        }
    }
}
