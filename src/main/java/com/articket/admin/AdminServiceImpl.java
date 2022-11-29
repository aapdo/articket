package com.articket.admin;

import com.articket.producer.Performance;
import com.articket.producer.Producer;
import com.articket.producer.repository.PerformanceRepository;
import com.articket.producer.repository.ProducerRepository;
import com.articket.qrcode.FileService;
import com.articket.qrcode.QRJwtService;
import com.articket.qrcode.ticket.Ticket;
import com.articket.qrcode.ticket.TicketData;
import com.articket.qrcode.ticket.TicketRepository;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final ProducerRepository producerRepository;
    private final PerformanceRepository performanceRepository;
    private final TicketRepository ticketRepository;
    private final QRJwtService qrJwtService;
    private final FileService fileService;

    //티켓을 리스트로 받아서 qr로 만듬.
    //문자나 카톡으로 보내고 싶지만 비용 또는 인증이 필요함
    //그래서 다운받고 그냥 보내야할듯.
    public void createTicketByList(List<Ticket> allTicketByPerformanceId) throws Exception {
        for (Ticket ticket : allTicketByPerformanceId) {
            log.debug("ticket = {}", ticket);
            byte[] qrByte = qrJwtService.createQr(ticket);
            fileService.save(qrByte,ticket.getUserName());

            //문자 메세지 로직 들어가야함.

        }
    }

    /**
     *
     * qr 티켓을 다시 만들어줌.
     * @param ticketId
     * @throws IOException
     * @throws WriterException
     */
    @Override
    public Ticket refreshQR(Long ticketId) throws IOException, WriterException {//발송 완료를 초기화해야함
        Ticket ticket = ticketRepository.findById(ticketId);
        String userName = ticket.getUserName();

        //티켓 qr을 새로 발급했으므로 발송완료를 false 로 바꾸고 다시 저장
        ticket.setIsIssued(false);
        ticketRepository.save(ticket);

        byte[] qrByte = qrJwtService.createQr(ticket);
        fileService.save(qrByte, userName);

        return ticket;
    }

    @Override
    public Ticket sentTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId);
        //발급 완료
        ticket.setIsIssued(true);
        ticketRepository.save(ticket);
        return ticket;
    }

    @Override
    public void update(Long ticketId, String userName, String phoneNumber, String residentNumber, Boolean checkin, Boolean isIssued){
        Ticket ticket = ticketRepository.findById(ticketId);
        ticket.setUserName(userName);
        ticket.setPhoneNumber(phoneNumber);
        ticket.setResidentNumber(residentNumber);
        ticket.setCheckin(checkin);
        ticket.setIsIssued(isIssued);

        ticketRepository.save(ticket);
    }



    /**
     * 모든 티켓을 리스트로 받는 함수
     * @param id
     * @return
     */
    public List<Ticket> findAllByPerformanceId(Long id) {
        return ticketRepository.findAllByPerformanceId(id);
    }

    /**
     * 티켓 하나를 아이디로 찾는 함수
     * @param id
     * @return
     */
    public Ticket findTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    /**
     * 티켓 데이터 엑셀을 파싱 후 티켓 테이블로 저장하는 함수 콜
     * @param file
     * @throws IOException
     */
    public void saveTicketInfo(MultipartFile file) throws IOException {

        //
        InputStream fileInputStream = file.getInputStream();

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

        XSSFSheet sheet = workbook.getSheetAt(0);// 시트 번호
        TicketData ticketData;
        int rows = sheet.getPhysicalNumberOfRows();//열들을 다 가져옴
        Long ticketId;
        Long performanceId;
        String userName;
        String phoneNumber;
        String residentNumber;
        String seatPosition;



        for (int rowindex = 1; rowindex < rows; rowindex++) {//열을 하나씩 올리면서 접근함.

            XSSFRow row = sheet.getRow(rowindex);

            if (row == null) {
                continue;
            }

            //ticketId = (Long)
            ticketId = (long)row.getCell(0).getNumericCellValue();
            performanceId = (long)row.getCell(1).getNumericCellValue();
            userName = row.getCell(2).getStringCellValue();
            phoneNumber = row.getCell(3).getStringCellValue();
            residentNumber = row.getCell(4).getStringCellValue();
            seatPosition = row.getCell(5).getStringCellValue();
            ticketData = new TicketData(ticketId, performanceId, userName, phoneNumber, residentNumber, seatPosition);
            saveTicketDataToTicket(ticketData);

            log.debug("ticket data read by excel = {}", ticketData);

        }
    }

    /**
     * 티켓 데이터를 테이블로 저장하는 함수
     * @param ticketData
     */
    private void saveTicketDataToTicket(TicketData ticketData) {
        Performance performance = performanceRepository.findById(ticketData.getPerformanceId());
        ticketRepository.save(new Ticket(null, performance, ticketData.getUserName(), ticketData.getPhoneNumber(), ticketData.getResidentNumber(), ticketData.getSeatPosition(), false, false));
    }

    public void deleteAllTickets(){
        ticketRepository.deleteAll();
    }
    /**
     * qr 코드 읽는 함수 미구현.
     * @param jwt
     * @return
     */
    @Override
    public Ticket readQR(String jwt) {
        Ticket ticket = qrJwtService.readQR(jwt);
        ticket.setCheckin(true);
        return ticket;
    }

    public void init(){
        //현재 시간
        LocalDateTime currentDateTime = LocalDateTime.now();
        //공연 이름
        String performanceName = "테스트 공연";
        //공연 날짜
        LocalDateTime performanceDate = LocalDateTime.of(2022,11,30,15,00,00);
        //예매 시작 끝 시간
        LocalDateTime reservationStartTime = LocalDateTime.of(2022,11,15,00,00,00);
        LocalDateTime reservationEndTime = LocalDateTime.of(2022,11,30,00,00,00);
        int price = 30000;

        Producer producer = new Producer(1L, "jade@naver.com", "1234", "testPD");
        Performance performance = new Performance(2L, producer, performanceName, currentDateTime, performanceDate, 90, reservationStartTime, reservationEndTime, 10000);
        //Ticket ticket = new Ticket(1L, performance, "test", "010-4246-5658", "0008303", "test", false, false);
        log.debug("new suc");

        producerRepository.save(producer);
        log.debug("producer save");
        performanceRepository.save(performance);
        log.debug("performance save");
        //ticketRepository.save(ticket);
        log.debug("ticket save");
        /*ticketRepository.save(new Ticket(1L, "test"+1, "010-4246-5658", "0008303", "testSeatPosition"+1, false, new Performance()));
        for (long i = 1; i < 20; i++) {
            ticketRepository.save(new Ticket(null, performance, "test"+i, "010-4246-5658", "0008303", "testSeatPosition" + i,  false, false));
        }
         */
    }


}
