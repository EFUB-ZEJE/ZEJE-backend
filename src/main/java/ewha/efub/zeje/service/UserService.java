package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import ewha.efub.zeje.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;

    @Transactional
    public UserResponseDTO findUser(Long userId) {
        User user = userRepository.findByUserIdAndDeleteFlagFalse(userId);
        return new UserResponseDTO(user);
    }

    @Transactional
    public boolean removeUser(Long userId) {

        try{
            User user = userRepository.findByUserIdAndDeleteFlagFalse(userId);
            user.deleteUser();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    @Transactional
    public UserResponseDTO updateProfile(Long userId,  String nickname, MultipartFile file) throws IOException {
        User entity = userRepository.findByUserIdAndDeleteFlagFalse(userId);

        if(file!=null) {
            String currProfile = entity.getProfileUrl();
            String newProfile = imageUploadService.uploadImage(currProfile, file);

            if(newProfile.equals("type error")) {
                return null;
            }
            else if(newProfile.equals("null error")) {
                return null;
            }
            else {
                entity.updateProfile("http://" + imageUploadService.CLOUD_FRONT_DOMAIN_NAME + "/profile/" + newProfile);
            }
        }

        if(nickname!=null) {
            if(!nickname.isBlank()) entity.updateNickname(nickname);
        }

        return new UserResponseDTO(entity);
    }

}
