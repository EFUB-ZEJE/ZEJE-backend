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
    public User saveUser(String kakaoToken) {
        KakaoProfile profile = findProfile(kakaoToken);

        User user = userRepository.findByKakaoId(profile.getId());

        if(user == null) {
            user = User.builder()
                    .kakaoId(profile.getId())
                    .profileUrl(profile.getKakao_account().getProfile().getProfile_image_url())
                    .email(profile.getKakao_account().getEmail())
                    .nickname(profile.getKakao_account().getProfile().getNickname())
                    .build();
            userRepository.save(user);
        }
        return user;
    }

    public KakaoProfile findProfile(String token) {

        //(1-6)
        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        try{
            RestTemplate rt = new RestTemplate();

            //(1-3)
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token); //(1-4)
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            //(1-5)
            HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                    new HttpEntity<>(headers);

            ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoProfileRequest,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            KakaoProfile kakaoProfile = null;
            try {
                kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return kakaoProfile;
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return null;
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
        try{
            return userRepository.findByUserIdAndDeleteFlagFalse(userId);
        }catch(Exception e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

}
