package org.example.simpledms.service.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.example.simpledms.security.dto.MemberDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * packageName : org.example.simpledms.service.jwt
 * fileName : JwtUtils
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
@Component
@Slf4j
public class JwtUtils {

    // 정의해놓은 비밀키를 속성으로 정의해준다.
    @Value("${simpleDms.app.jwtSecret}") // 속성에서 정의해준 값을 들고와주는 어노테이션
    private String jwtSecret;

    @Value("${simpleDms.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    //    함수 정의
//    1) JWT(웹토큰) 생성 함수
    public String generateJwtToken(Authentication authentication) {
//        1) id : email 사용
        MemberDto memberDto = (MemberDto) authentication.getPrincipal();

        //    Json Web Token 구조 : 헤더(header).내용(payload).서명(signature)
//    헤더 : 토큰타입, 알고리즘
//    내용 : 데이터(subject(주체(이름))), 토큰발급대상(issuedAt), 만료기간(expiration), 토큰수령자
//    서명 : Jwts.builder().signWith(암호화알고리즘, 비밀키값)
//    생성 : Jwts.builder().compact()
        return Jwts.builder()
                // 헤더를 생략해서 기본으로 생성됨
                .setSubject((memberDto.getEmail()))// 주제는 email로 넣어줬다
                .setIssuedAt(new Date()) // 토큰 발급대상
//            만료일자 적용
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // 토큰 생성 시간에서 10분 뒤에 만료되게 만듬
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 암호화 적용 서명(HS512 사용, 비밀키 넣기)
                .compact(); // 토큰 생성
    }

    //    2) JWT(웹토큰) 에서 유저ID(이메일) 꺼내기 함수
    public String getUserNameFromJwtToken(String token) {
    //    웹토큰의 비밀키 + 토큰명을 적용해 body 안의 subject(주체(이름))에 접근해서 꺼냄
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //    3) JWT(웹토큰) 이 유효한지 검증하는 함수
    public boolean validateJwtToken(String authToken) {
        try {
    //      setSigningKey(jwtSecret) : 비밀키를 넣어 웹토큰 디코딩하기(해석)
    //      parseClaimsJws : 웹토큰을 분리하여 유효성 점검하는 함수
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException  e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException  e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
