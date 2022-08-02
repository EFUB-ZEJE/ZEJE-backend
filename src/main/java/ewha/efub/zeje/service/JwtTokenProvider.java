package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import ewha.efub.zeje.dto.security.SessionUserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final UserRepository userRepository;
    private String secretKey = "CakerBakery1886!";

    private final long ACCESS_TOKEN_VALID_TIME = 3 * 60 * 60 * 1000L; //3시간
    //private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT token 생성
    public String createJwtAccessToken(String userPk, String nickname) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("nickname", nickname);
        Date now = new Date(); // 현재 시간 -> 유효기간 확인을 위함
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    // token 가공해서 정보 추출
    public Authentication getAuthentication(String token) {
        User user = userRepository.findByKakaoId(Long.parseLong(getUserPk(token)));
        UserDetails sessionUserDTO = SessionUserDTO.builder().user(user).build();
        return new UsernamePasswordAuthenticationToken(sessionUserDTO, "", sessionUserDTO.getAuthorities());
    }


    public String getUserPk(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // HTTP 요청 안에서 헤더 찾아서 토큰 가져옴
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date()); // 유효하면 return
        } catch (Exception e){
            return false; //유효하지 않은 경우
        }
    }

    public SessionUserDTO getUserInfoByToken(HttpServletRequest request) {
        String token = resolveToken(request);
        if(validateToken(token)) {
            User user = userRepository.findByKakaoId(Long.parseLong(getUserPk(token)));
            return new SessionUserDTO(user);
        }
        else {
            return null;
        }
    }
}