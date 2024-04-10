package hhplus.serverjava.api.util.jwt;

import javax.servlet.http.HttpServletResponse;

public interface JwtService {

    /*
   JWT 생성
   @param userId
   @return String
    */
   String createJwt(Long userId);

    /*
   Header에서 Authorization 으로 JWT 추출
   @return String
    */
    String getJwt();

    /*
    * Header에 JWT 추가
    */
    void sendToken(HttpServletResponse response, String token);

    /*
   JWT에서 userId 추출
   @return Long
   @throws BaseException
    */
    Long getUserId();

    /*
   JWT의 userId 검증
   @return Boolean
   @throws BaseException
    */
    boolean validToken(Long userId);
}
