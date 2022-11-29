package com.articket.producer;

import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter @Setter
public class Producer {//극단 회원가입. 나중에 구현할 것임. 동국대에 쓸때는 안쓸듯.
    @Id
    @Column(name="producer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//식별 넘버 PK
    private String email;//유저 아이디 UK
    private String password;
    private String name;//극단 이름
}
