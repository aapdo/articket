package com.articket.admin;

import com.articket.qrcode.ticket.Ticket;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    //어드민 권한을 가진 사람만 들어올 수 있다.

    /**
     * 메인 페이지 티켓 리스트가 보임
     * @param model
     * @return
     */
    @GetMapping("/tickets")
    public String viewTickets(Model model){
        List<Ticket> allTicketByPerformanceId = adminService.findAllByPerformanceId(2L);
        model.addAttribute("tickets",allTicketByPerformanceId);
        //log.debug("ticket list = {}", allTicketByPerformanceId);
        return "admin/tickets";
    }

    @GetMapping("/ticket/{ticketId}")
    public String viewTicket(@PathVariable Long ticketId, Model model) {
        Ticket ticket = adminService.findTicketById(ticketId);
        model.addAttribute("ticket", ticket);
        return "admin/ticket";
    }

    //create 버튼을 눌렀을 때.
    //모든 티켓을 전부 다 생성 후 문자 메세지로 발송할 메서드
    //문자 또는 카톡으로 보내는 것은 비용이 들거나 심사가 필요함
    //따라서 파일로 저장하고 보내는 방식을 채택해야함.

    /**
     * 모든 티켓 테이블 데이터로 QR 코드를 만들고 로컬에 저장함.
     * @return
     * @throws Exception
     */
    @GetMapping("/createQR/all")
    public String createQR() throws Exception {
        List<Ticket> allTicketByPerformanceId = adminService.findAllByPerformanceId(2L);
        adminService.createTicketByList(allTicketByPerformanceId);
        //log.info("create QR all success");

        return "redirect:/admin/tickets";
    }

    /**
     * qr 코드를 재발급하는 메서드
     * @param ticketId
     * @param model
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @GetMapping("/refreshQR/{ticketId}")
    public String refreshQR(@PathVariable Long ticketId, Model model) throws IOException, WriterException {
        Ticket ticket = adminService.refreshQR(ticketId);
        model.addAttribute("ticket", ticket);
        return "admin/ticket";
    }

    /**
     * qr 코드를 읽는 메서드
     * @param jwt
     * @param
     * @return
     */
    @GetMapping("/readQR/{jwt}")
    public String readQR(@PathVariable String jwt, RedirectAttributes redirectAttributes) {
        Ticket ticket = adminService.readQR(jwt);
        redirectAttributes.addAttribute("checkIn", true);
        log.info("ticket = {}", ticket);

        return "redirect:/admin/ticket/"+ticket.getId();
    }

    @GetMapping("/sent/{ticketId}")
    public String sentTicket(@PathVariable Long ticketId, Model model) {
        Ticket ticket = adminService.sentTicket(ticketId);
        model.addAttribute("ticket", ticket);
        return "admin/ticket";
    }

    @GetMapping("/ticket/{ticketId}/edit")
    public String viewTicketEditForm(@PathVariable Long ticketId, Model model){
        Ticket ticket = adminService.findTicketById(ticketId);
        model.addAttribute("ticket", ticket);
        return "admin/ticketEditForm";
    }
    @PostMapping("/ticket/{ticketId}/edit")
    public String editTicket(@PathVariable Long ticketId,
                             @RequestParam("userName") String userName,
                             @RequestParam("phoneNumber") String phoneNumber,
                             @RequestParam("residentNumber") String residentNumber,
                             @RequestParam("checkin") Boolean checkin,
                             @RequestParam("isIssued") Boolean isIssued
    ){
        adminService.update(ticketId, userName, phoneNumber, residentNumber, checkin, isIssued);

        return "redirect:/admin/ticket/{ticketId}";
    }



    /**
     * 티켓 데이터 엑셀을 업로드하는 곳.
     * @return
     */
    @GetMapping("/upload/ticketInfo")
    public String uploadTicketDataForm() {
        return "/admin/uploadTicketInfoForm";
    }

    /**
     * 티켓 데이터 엑셀을 받아서 처리하는 곳.
     * @param file
     * @throws IOException
     */
    @PostMapping("/read/ticketInfo")
    public String readTicketInfo(@RequestParam("file") MultipartFile file) throws IOException {
        adminService.saveTicketInfo(file);
        return "redirect:/admin/tickets";
    }

    /**
     * 영업용 티켓 모두 지우는 url
     * @return
     */
    @GetMapping("/deleteAll")
    public String deleteTicketsForm(){
        return "admin/deleteTicketForm";
    }

    @PostMapping("/deleteAll")
    public String deleteTickets(){
        adminService.deleteAllTickets();
        return "redirect:/admin/tickets";
    }

    @PostConstruct
    //프로듀서 임의 생성
    //퍼포먼스 임의 생성 => Mvp 완성 전에 공연 데이터 넣을 것.
    //ticket 20개 임의 생성
    public void init() {
        adminService.init();
    }




    /*
    //어드민 페이지에서 특정 공연을 선택했을 때 보여줄 페이지
    @GetMapping("/performance/{performanceId}")
    public String viewPerformance(@PathVariable Long performanceId) {
        //performance 테이블에서 findById
        Performance performance = performanceRepository.findById(performanceId);
        log.info("어드민에서 보여질 데이터 = {}", performance);
        return "id: " + performance;
    }

    @GetMapping("/performance/{performanceId}/createQR")
    public String createQr(@PathVariable Long performanceId){
        //티켓에서 performance 아이디로 서치해서 전부 qr 생성.
        List<Ticket> data = ticketRepository.findAllByPerformanceId(performanceId);
        log.info("data = {} ", data);

        return "ok";
    }

     */
}
