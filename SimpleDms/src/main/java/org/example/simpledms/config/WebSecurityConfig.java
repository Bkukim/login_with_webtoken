package org.example.simpledms.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * packageName : org.example.loginexam.config
 * fileName : WebSecurityConfig
 * author : PC
 * date : 2024-03-26
 * description : todo 스프링 시큐리티
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-26         PC          최초 생성
 */
@Configuration
public class WebSecurityConfig {
    // todo 1) DB인증을 하는 클래스 (당연히 DB에 있어야 올바른 사람이므로 당연히 있어야한다.) :
    // todo : 2) 패스워드 암호화 함수  :
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // 암호화를 시켜주는 함수들 중 하나이다.
    }
    // todo 2-1) 공통 jsp, img, css, js 등 : 인증 안 받는 것들은 무시하도록 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){ // 이걸 안하면 다 인증에 걸려서 나오지 않음

        return (web) -> web.ignoring().requestMatchers(
                "/img/**",
                "/css/**",
                "/js/**"
        );
    }
    // todo : JWT(웹토큰) 객체 정의


    // todo 3) 스프링 시큐리티 규칙 정의 (권한 관리): 웹토큰 이용
    //   관리자와 일반 유저가 볼 수 있는 페이지를 관리하는 것, 예) 부서는 일반 유저가 볼수 있음, 갤러리는 관리자만 볼수있음
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.cors(Customizer.withDefaults()); // cors 를 사용하는 것을 spring security 에서 사용한다고 알게 해주는것
        http.csrf((csrf)->csrf.disable()); // csrf공격은 쿠키 세션 을 훔치는 해킹이라 웹토큰을 사용하는 여기선 신경안써도 된다.

        // 스프링 시큐리티는 아무 언급도 없으면 쿠키/세션 방식을 디폴트로 사용한다. 그래서 사용하지 않을 경우 안쓴다고 해줘야한다.
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(req -> req.disable()); // form action 을 이용한 로그인을 사용하지 않고 axios 를 사용할것이다.

        http.authorizeHttpRequests(req -> req // 이 함수를 사용하면 버전에 상관없이 사용가능하다. 위의 것은 버전이 바뀌면 사용이 불가능 해질 수 있다.
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers("api/auth/**").permitAll()       // 이 url 은 모든 사용자 접근 허용
                .requestMatchers("api/admin/**").hasRole("ADMIN") // admin 메뉴는 ROLE_ADMIN 만 가능
                .requestMatchers("api/basic/dept/**").permitAll()           // 이 url 은 모든 사용자 접근 허용
                .requestMatchers("api/").permitAll()           // 이 url 은 모든 사용자 접근 허용
                .requestMatchers("api/error").permitAll()
                .anyRequest()
                .authenticated());

        // todo : 웹토큰 클래스를 스프링 시큐리티 설정에 끼워넣기



        return http.build();// 위의 것들을 종합해서 객체 형태로 만들어주는 함수이다.

    }
}