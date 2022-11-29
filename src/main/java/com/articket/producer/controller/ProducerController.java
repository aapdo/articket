package com.articket.producer.controller;

import com.articket.producer.Performance;
import com.articket.producer.Producer;
import com.articket.producer.repository.PerformanceRepository;
import com.articket.producer.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/producer")
@RequiredArgsConstructor
@RestController
public class ProducerController {

    //private final ProducerRepository producerRepository;
    //private final PerformanceRepository performanceRepository;
    private final PerformanceRepository performanceRepository;
    private final ProducerRepository producerRepository;

    //로그인 했을 때 유저 본인이 등록한 공연 보여주기
    @GetMapping
    public Performance showPerformanceForm(@RequestParam ("id") Long id){
        //performanceRepository.save(performance);
        log.info("param = {}", id);
        return performanceRepository.findById(id);//타임리프 경로 적어주기.
    }

    //공연 등록하기
    @PostMapping
    public String savePerformance(Performance performance){
        performanceRepository.save(performance);
        return "1";//post redirect get 로 성공 페이지 띄워줘야함.
    }

    //edit 버튼을 눌렀을 때 보여줄 화면
    @GetMapping("/{performId}/edit")
    public String editForm(@PathVariable Long performId, Model model){
        Performance performance = performanceRepository.findById(performId);
        model.addAttribute("performance", performance);//thymeleaf 용
        return "1";
    }

    //edit 완료 버튼 누르면 수정하는 로직.
    @PostMapping("/{performId}/edit")
    public String edit(@PathVariable Long performId, Performance performance, Model model){
        performanceRepository.save(performance);
        model.addAttribute("performance", performance);//thymeleaf 용
        return "1";
    }

    @PostConstruct
    public String init(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Producer producer = new Producer(14L, "jade@naver.com", "1234", "testPD");
        //Performance performance = new Performance(1L, producer, "testPerform", currentDateTime, currentDateTime, currentDateTime, 10000);
        producerRepository.save(producer);
        //performanceRepository.save(performance);
        return "ok";
    }


}
