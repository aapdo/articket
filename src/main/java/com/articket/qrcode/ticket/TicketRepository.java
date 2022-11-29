package com.articket.qrcode.ticket;

import com.articket.producer.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {//type, PK의 타입

    @Query(value = "select * from ticket where id = :id", nativeQuery=true)
    Ticket findById(@Param("id") Long id);

    @Query(value = "select * from ticket where performance_id = :performanceId", nativeQuery=true)
    List<Ticket> findAllByPerformanceId(@Param("performanceId") Long performanceId);

    @Override
    <S extends Ticket> S save(S entity);
}
