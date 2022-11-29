package com.articket.qrcode.jwt;

import com.articket.qrcode.ticket.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtTicketService {

    //비밀키
    private static final String ENCRYPT_STRING = "Test";
    //key
    private static final String DATA_KEY = "ticket";
    private final ObjectMapper objectMapper;


    //[0] User 정보를 이용해서 - JWT 생성
    public String createLoginToken(Ticket ticket) {

        //현재시간
        long curTime = System.currentTimeMillis();

        //[1] Jwts 라이브러리로부터 jwt 생성 - builder 패턴

        return  Jwts.builder()

                .setSubject("Test JWT")

                //[2]
                //setHeaderParam 메소드를 통해 JWT 헤더가 지닐 정보들을 담는다.
                //alg 의 경우는 default 값이 SHA256이므로 따로 설정할 필요는 없다.
                //typ 를 셋팅 안해주면 오류 발생한다.

                //.setHeaderParam("typ", "JWT")
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("ticket through")

                //[3] 만료 시간
                .setExpiration(new Date(curTime + 3600000))

                //[4] 발급 시간
                .setIssuedAt(new Date(curTime))


                //[5] Payload 에 Private Claims 를 담기 위해 claim 메소드를 이용한다.
                // private claim으로 VO객체를 추가할 수 있음
                .claim(DATA_KEY, ticket)


                //[6] 복호화 할때 사용하는 시그니처 설정.
                // header의 인코딩값 + payload의 인코딩값 + 비밀키 = 시그니처
                // signWith api는 해싱알고리즘과 비밀키가 필요하다.
                .signWith(SignatureAlgorithm.HS256, this.generateKey())

                //생성
                .compact();


        //"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTQ5MzMwMzcsImlhdCI6MTYxNDkyOTQzNywidXNlciI6eyJ1c2VySWQiOiJzdWppbiIsInBhc3N3b3JkIjpudWxsLCJhdXRob3JpdHkiOlsiVVNFUiJdLCJlbmFibGVkIjpmYWxzZSwibmFtZSI6IuydtOyImOynhCJ9fQ.FsP6XGQ2tLJ9kO8NZMOP3OtZu69YK1vxWhNK4XGyEmU";


    }


    //비밀키 생성 메소드
    private byte[] generateKey(){
        byte[] key = null;
        try {


            //ENCRYPT_STRING =  pretty
            key = ENCRYPT_STRING.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.info("Making secret Key Error = {}", e);
        }


        System.out.println("비밀 key : " + key); //[B@59252cb6


        return key;
    }


    //JWT 복호화
    public Ticket getUser(String jwt) {

        //결과값 = Claims
        Jws<Claims> claims = null;

        try {

            //비밀키를 이용해서 복호화 하는 작업
            //jwt가 유효한지, 위변조 되지 않았는지 판단한다.
            //이 비밀키는 서버에만 존재해야되고, 유출되어서는 안된다.
            claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(jwt);


        } catch (Exception e) {
            log.info("{} = {}", e.getMessage(), e);
            throw new JWTException("decodeing failed");
        }

        //반환 타입은 LinkedHashMap 이다. 이를 User 타입으로 변환하기 위해 ObjectMapper 사용
        return objectMapper.convertValue(claims.getBody().get(DATA_KEY), Ticket.class);
    }
}
