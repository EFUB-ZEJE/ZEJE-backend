package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import ewha.efub.zeje.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

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
}
