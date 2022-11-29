package com.articket.audience;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Audience {//유저 로그인이 생기면 그 때 구현하자.
    @Id
    @GeneratedValue
    private Long id;
    private String phoneNumber;
    private String residentNumber;//주민번호 앞 6자리 + 뒤 1자리 = 7자리
}
