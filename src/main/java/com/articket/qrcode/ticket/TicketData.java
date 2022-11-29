package com.articket.qrcode.ticket;

import com.articket.producer.Performance;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class TicketData {
    private Long id;
    private Long performanceId;

    private String userName;
    private String phoneNumber;
    private String residentNumber;
    //좌석 번호 필드 필요.
    private String seatPosition;


}
