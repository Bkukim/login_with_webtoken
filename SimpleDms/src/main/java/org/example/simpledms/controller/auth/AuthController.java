package org.example.simpledms.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simpledms.model.dto.UserReq;
import org.example.simpledms.model.dto.UserRes;
import org.example.simpledms.service.auth.MemberService;
import org.example.simpledms.service.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Set;

/**
 * packageName : org.example.simpledms.controller.auth
 * fileName : MemberController
 * author : PC
 * date : 2024-04-15
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-15         PC          최초 생성
 */
@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder; // 암호 인코딩

    private final JwtUtils jwtUtils; // 웹토큰 유틸

    // 인증/권한 체크를 위한 인증관리(쿠키세션에서는 자동으로 해주지만 토큰에선 수동으로 해주어야한다.)
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserReq userReq){
        try {
            // 1) 웹토큰 인증
            Authentication authentication
                    = authenticationManagerBuilder
                    .getObject().authenticate(new UsernamePasswordAuthenticationToken(userReq.getEmail(),userReq.getPassword()));

            // 2) 인증된 객체 관리 => 홀더에 관리
            SecurityContextHolder.getContext().setAuthentication(authentication); // 여기에 set으로 저장됨 UserDetailsServiceImpl 클래스에 정의

            // 3) 웹토큰 발행(생성) => 프론트로 전송
            String jwt = jwtUtils.generateJwtToken(authentication);

            // 4) 권한 정보 : role_user, role_admin => 프론트 전송
            String codeName = new ArrayList<>(authentication.getAuthorities()).get(0).toString();

            // 5) DTO : jwt (웹토큰), email, 권한 정보,(패스워드는 보내면 위험함)
            UserRes userRes = new UserRes(jwt, codeName, userReq.getEmail());
            return new ResponseEntity<>(userRes, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
