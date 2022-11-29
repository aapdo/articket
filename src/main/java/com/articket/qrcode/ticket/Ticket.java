package com.articket.qrcode.ticket;

import com.articket.producer.Performance;
import lombok.*;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Entity
@Repository

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ticket{//나중에 유저 로그인 생기면 그 때 번호나 정보를 받아오고 지금은 그냥 입력 받는걸로.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "performance_id")
    private Performance performanceId;

    private String userName;
    private String phoneNumber;
    private String residentNumber;
    //좌석 번호 필드 필요.
    private String seatPosition;
    private Boolean checkin;//checkin false = 아직 안들어옴.
    private Boolean isIssued;//false = 아직 발급 안함


}
