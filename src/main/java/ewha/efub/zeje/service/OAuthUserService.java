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
        User user = userRepository.findByEmail(attributes.getEmail());
        if(user!=null){
            System.out.println("로그인 유저: " + user.getNickname() + ", " + user.getEmail());
            return user;
        } else{
            User loginUser = userRepository.findByEmail(attributes.getEmail());
            System.out.println("로그인 유저: " + loginUser.getNickname() + ", " + loginUser.getEmail());
            return userRepository.save(attributes.toEntity(registrationId));
        }

    }
}
