package com.example.shoppro.config;

import jakarta.servlet.annotation.WebListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@WebListener
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {

        // 아래코드는 board.setTitle().setContent().setWriter() 와 같은 식으로 작성됨
        httpSecurity

                // 권한, 페이지 접속권환
                .authorizeHttpRequests(
                        authorization -> authorization
                                .requestMatchers("/user/login/**").permitAll()    // 로그인페이지는 누구나 접속이 가능한 권한
                                .requestMatchers("/board/register").authenticated() // 로그인 한 사람만 접속 가능 (세션 처리했던 내용을 이한줄로 가능)
                                .requestMatchers("/item/register").hasRole("ADMIN") // 사장님만 아이템을 개시할 수 있음
                                .requestMatchers("/user/list").hasRole("ADMIN")     // 유저관리는 관리자만 가능
                                .anyRequest().permitAll()                             // 위에 것들 외에 전부 열어둠
//                            .anyRequest().authenticated()                         // 위에 것들 외에는 다 로그인해서 접속해라
                )

                // 위변조 방지 웹에서 form태그 변경 등의 변조를 방지
                // 안할거라서 설정만 할것임
                .csrf( csrf -> csrf.disable() )//안씀

                // 로그인
                .formLogin(
                        formLogin -> formLogin.loginPage("/user/login")               // 기본 로그인 페이지 지정
                                .defaultSuccessUrl("/user/login")                      // 로그인이 성공했다면
                                .usernameParameter("email")                           // 로그인 <input name="email">
                        // 컨트롤러로 보낼때~~
                )

                // 로그아웃
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃 a태그라 생각
                                // <a href="/user/logout">잘가~~</a>
                                .invalidateHttpSession(true)                      // 세션초기화
                                .logoutSuccessUrl("/")//세션날릴수있음               // localhost:8090 으로 간다.
                        // DNS주소일 경우 www.naver.com까지로 간다.
                        // 컨트롤러에서 만들어 줄껄??
                )



        // 예외처리 // 로그인이 되지 않은 사용자, 권한이 없는 사용자 접속시 취할 행동들
//                .exceptionHandling(                                             지금하면 예외처리 되버리니 주석처리
//                        a -> a
//                )

        // 마지막 종결임, 아래에 빼둠
        ;
        return httpSecurity.build();


    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


}
