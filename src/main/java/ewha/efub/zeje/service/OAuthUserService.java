package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import ewha.efub.zeje.dto.OAuthAttributesDTO;
import ewha.efub.zeje.dto.SessionUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 현재 로그인 진행 중인 서비스 구분
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // 로그인 진행 시의 pk

        OAuthAttributesDTO attributes = OAuthAttributesDTO.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes()); //OAuth2User의 attribute 담을 클래스

        User user = saveOrUpdate(registrationId, attributes);

        httpSession.setAttribute("user",new SessionUserDTO(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(String registrationId, OAuthAttributesDTO attributes){
        User user = userRepository.findByKakaoId(attributes.getKakaoId());
        if(user!=null){
            System.out.println("로그인 유저: " + user.getNickname() + ", " + user.getEmail());
            return user;
        } else{
            userRepository.save(attributes.toEntity(registrationId));

            User loginUser = userRepository.findByKakaoId(attributes.getKakaoId());
            System.out.println("로그인 유저: " + loginUser.getNickname() + ", " + loginUser.getEmail()+ ", " + loginUser.getKakaoId());
            return loginUser;
        }

    }

    public Object loadUserPostman(Map<String,Object> attribute) {
        OAuthAttributesDTO attributes = OAuthAttributesDTO.ofPostman(attribute);
        User user = saveOrUpdatePostman(attributes);

        httpSession.setAttribute("user",new SessionUserDTO(user));
        return httpSession.getAttribute("user");
    }

    private User saveOrUpdatePostman(OAuthAttributesDTO attributes){
        User user = userRepository.findByKakaoId(attributes.getKakaoId());
        if(user!=null){
            System.out.println("로그인 유저: " + user.getNickname() + ", " + user.getEmail());
            return user;
        } else{
            userRepository.save(attributes.toEntityPostman());

            User loginUser = userRepository.findByKakaoId(attributes.getKakaoId());
            System.out.println("로그인 유저: " + loginUser.getNickname() + ", " + loginUser.getEmail()+ ", " + loginUser.getKakaoId());
            return loginUser;
        }

    }
}
