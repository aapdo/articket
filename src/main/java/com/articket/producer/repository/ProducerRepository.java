package com.articket.producer.repository;

import com.articket.producer.Performance;
import com.articket.producer.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, String> {

    @Query(value = "select name, price from producer where id = :id", nativeQuery=true)
    Producer findById(@Param("id") Long id);

    @Query(value = "select name, price from producer where id = :id", nativeQuery=true)
    List<Producer> searchParamRepo(@Param("id") Long id);
}
