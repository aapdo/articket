package com.articket.producer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter @Setter
public class Performance {
    @Id
    @Column(name = "performance_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer pdId;//극단 테이블과 연결

    private String name;//공연 이름

    //Json에서 LocalDateTime 을 직렬화/역직렬화를 위해 추가해야함.
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)

    @CreatedDate
    private LocalDateTime createTime;//등록 날짜, 시간

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime performanceTime;//공연 날짜, 시간

    private int playTimeMinute;//플레이 타임이 몇분인지

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime reservationStartTime;//예매 날짜, 시간

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime reservationEndTime;//예매 날짜, 시간

    private Integer price;//예매 가격

}
