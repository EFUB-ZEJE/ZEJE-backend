package ewha.efub.zeje.config;

import ewha.efub.zeje.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig{
    private final OAuthUserService oAuthUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests() //url별 권한 관리 설정
                .antMatchers("/","/css/**","/images/**","/js/**","/profile").permitAll()
                .anyRequest().permitAll() // 나머지들은, 로그인한 사람들만
                .and()
                .logout()
                .logoutSuccessUrl("/") //로그아웃 성공시 여기로 이동
                .and()
                .oauth2Login() //OAuth2 로그인 기능 설정
                .defaultSuccessUrl("/main")
                .userInfoEndpoint() //로그인 성공 이후 사용자 정보 가져올 때의 설정 담당
                .userService(oAuthUserService);//UserService 인터페이스의 구현체

        return http.build();
    }
}
