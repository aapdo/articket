package com.articket.producer.repository;

import com.articket.producer.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, String> {
    //Performance savePerformance(Performance performance);

    @Query(value = "select * from performance where performance_id = :performance_id", nativeQuery=true)
    Performance findById(@Param("performance_id") Long performance_id);

}
