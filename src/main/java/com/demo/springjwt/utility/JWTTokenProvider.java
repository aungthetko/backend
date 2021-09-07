package com.demo.springjwt.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.demo.springjwt.modal.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(UserPrincipal userPrincipal){
        String[] claims =  getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer("Aung Thet Ko")
                .withAudience("atk")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withSubject(userPrincipal.getUser().getId().toString())
                .withArrayClaim("authorities", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 432_000_000))
                .sign(Algorithm.HMAC512(secret.getBytes(StandardCharsets.UTF_8)));
    }

    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    public boolean isTokenValid(String userId, String token){
        JWTVerifier jwtVerifier = getJWTVerifier();
        return StringUtils.isNotEmpty(userId) && !isTokenVerifier(jwtVerifier, token);
    }

    private boolean isTokenVerifier(JWTVerifier jwtVerifier, String token) {
        Date expireDate = jwtVerifier.verify(token).getExpiresAt();
        return expireDate.before(new Date());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim("authorities").asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try{
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer("Aung Thet Ko").build();
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token is valid");
        }
        return verifier;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal){
        return userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }

    public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }
}
