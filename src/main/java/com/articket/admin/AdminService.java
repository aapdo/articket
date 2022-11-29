package com.articket.admin;

import com.articket.qrcode.ticket.Ticket;
import com.google.zxing.WriterException;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {


    void createTicketByList(List<Ticket> allTicketByPerformanceId) throws Exception;
    List<Ticket> findAllByPerformanceId(Long id);

    void init();

    Ticket refreshQR(Long ticketId) throws IOException, WriterException;

    Ticket readQR(String jwt);

    Ticket findTicketById(Long ticketId);

    public void saveTicketInfo(MultipartFile file) throws IOException;

    Ticket sentTicket(Long ticketId);

    void update(Long ticketId, String userName, String phoneNumber, String residentNumber, Boolean checkin, Boolean isIssued);

    void deleteAllTickets();
}
