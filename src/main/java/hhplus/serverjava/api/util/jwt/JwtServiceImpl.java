package hhplus.serverjava.api.util.jwt;

import hhplus.serverjava.api.util.exceptions.BaseException;
import hhplus.serverjava.domain.user.componenets.UserReader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static hhplus.serverjava.api.util.response.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    private final UserReader userReader;

    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY;

    // JWT 생성
    @Override
    public String createJwt(Long userId){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    @Override
    public void sendToken(HttpServletResponse response, String token) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader("Authorization", token);
        log.info("헤더 설정 완료 token : {}", token);
    }

    // Header에서 Authorization으로 JWT 추출
    @Override
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    // JWT에서 userId 추출
    @Override
    public Long getUserId() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        log.info("claims.getBody : {}", claims.getBody());
        log.info("claims.getHeader : {}", claims.getHeader());

        // 3. userId 추출
        return claims.getBody().get("userId",Long.class);
    }

    @Override
    public boolean validToken(Long userId) {
        try {
            userReader.findUser(userId);
        } catch (BaseException e) {
            throw new BaseException(NOT_FIND_USER);
        }
        return true;
    }

}
