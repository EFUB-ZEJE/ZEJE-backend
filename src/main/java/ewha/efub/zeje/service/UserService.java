package ewha.efub.zeje.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import ewha.efub.zeje.dto.FruitRequestDTO;
import ewha.efub.zeje.dto.security.SessionUserDTO;
import ewha.efub.zeje.dto.user.KakaoProfile;
import ewha.efub.zeje.dto.user.UserRequestDTO;
import ewha.efub.zeje.dto.user.UserResponseDTO;
import ewha.efub.zeje.util.errors.CustomException;
import ewha.efub.zeje.util.errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;

import static ewha.efub.zeje.dto.user.UserResponseDTO.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;

    @Transactional
    public User saveUser(KakaoProfile kakaoProfile) {
        User user = userRepository.findByKakaoId(kakaoProfile.getId());

        if(user == null) {
            user = kakaoProfile.toEntity();
            userRepository.save(user);
        }

        if(user.getDeleteFlag()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public UserResponseDTO findUser(Long userId) {
        User user = getUserEntity(userId);
        return new UserResponseDTO(user);
    }

    public UserFruitResponseDTO findFruitBox(Long userId) {
        User user = getUserEntity(userId);
        return new UserFruitResponseDTO(user);
    }

    @Transactional
    public UserFruitResponseDTO modifyFruitBoxAdd(Long userId, FruitRequestDTO fruitRequestDTO) {
        User user = getUserEntity(userId);
        user.updateFruitBox(true, fruitRequestDTO.getFruitBox());
        return new UserFruitResponseDTO(user);
    }

    @Transactional
    public UserFruitResponseDTO modifyFruitBoxSub(Long userId, FruitRequestDTO fruitRequestDTO) {
        User user = getUserEntity(userId);
        user.updateFruitBox(false, fruitRequestDTO.getFruitBox());
        return new UserFruitResponseDTO(user);
    }

    @Transactional
    public boolean removeUser(Long userId) {
        User user = getUserEntity(userId);
        user.deleteUser();
        return true;
    }

    @Transactional
    public UserResponseDTO updateProfile(Long userId,  String nickname, MultipartFile file) throws IOException {
        User entity = getUserEntity(userId);

        if(file!=null) {
            String currProfile = entity.getProfileUrl();
            String newProfile = imageUploadService.uploadImage(currProfile, file);
            entity.updateProfile(newProfile);
        }
        if(nickname!=null) {
            if(!nickname.isBlank()) entity.updateNickname(nickname);
        }
        return new UserResponseDTO(entity);
    }

    public User getUserEntity(Long userId) {
        User user;
        user = userRepository.findByUserIdAndDeleteFlagFalse(userId);
        if(user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

}
