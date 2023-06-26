package com.tranhuutruong.BookStoreAPI.Security.JWT;

import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private String jwtSecret = "tranhuutruong290401@gmail.com";

    private int jwtExpiration = 864000;

    public String createToken(UserInfoModel userInfoModel)
    {
        return Jwts.builder().setSubject(userInfoModel.getAccountModel().getUsername()).setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + jwtExpiration + 864000000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .claim("authorities", userInfoModel.getAccountModel().getRoleModel().getName())
                .compact();
    }

    public String refreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        Date issuedAt = claims.getIssuedAt();

        // Tạo mới một token mới
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(new Date().getTime() + jwtExpiration + 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token)
    {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e)
        {
            logger.error("Ivalid JWT signature!", e);
        }
        catch (MalformedJwtException e)
        {
            logger.error("The token ivalid format!", e);
        }
        catch (UnsupportedJwtException e)
        {
            logger.error("Unsupported jwt token", e);
        }
        catch (ExpiredJwtException e)
        {
            logger.error("Expired jwt token", e);
        }
        catch (IllegalArgumentException e)
        {
            logger.error("JWT claims string is empty", e);
        }
        return false;
    }

    public String getUserNameFromToken(String token){
        String userName = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return userName;
    }
}
