package org.example.simpledms.model.dto;

import lombok.*;

/**
 * packageName : org.example.simpledms.model.dto
 * fileName : UserRes
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
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {

    // 프론트에 전송할 정보를 담은 DTO
    private String accessToken; // 웹토큰

    private String tokenType = "Bearer"; // 토큰 타입

    private String codeName; // 권한

    private String email;

    public UserRes(String accessToken, String codeName, String email) {
        this.accessToken = accessToken;
        this.codeName = codeName;
        this.email = email;
    }
}
