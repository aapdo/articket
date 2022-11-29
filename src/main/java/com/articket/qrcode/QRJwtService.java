package com.articket.qrcode;

import com.articket.qrcode.jwt.JwtTicketService;
import com.articket.qrcode.ticket.Ticket;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QRJwtService {

    private final JwtTicketService jwtTicketService;

    public byte[] createQr(@RequestParam Ticket ticket) throws WriterException, IOException {
        int width = 200;
        int height = 200;
        String token = jwtTicketService.createLoginToken(ticket);
        log.info("jwt = {}", token);
        String url = getUrl() + token;//qr 코드에 서버 ip + http get 메서드를 사용하기 위한 주소를 담아줌.

        //위에서 생성한 url과 크기에 맞게 생성
        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);//byte[] 로 생성됨.
            //log.info("byte? = {}", out.toByteArray());
            return out.toByteArray();//byte array 로 리턴
            /*
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());

             */
        }
    }

    public Object createQr(@RequestParam List<Ticket> tickets) throws WriterException, IOException {
        int width = 200;
        int height = 200;
        String token = jwtTicketService.createLoginToken(tickets.get(0));
        String url = getUrl() + token;//qr 코드에 서버 ip + http get 메서드를 사용하기 위한 주소를 담아줌.

        //위에서 생성한 url과 크기에 맞게 생성
        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());
        }
    }

    public Ticket readQR(String jwt){
        Ticket inputTicketInfo = jwtTicketService.getUser(jwt);//jwt를 티켓으로 변환
        return inputTicketInfo;
    }

    private String getUrl() {
        String result = null;
        try {
            result = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            result = "알 수 없는 사용자";
        }
        return result+"/admin/readQR/";
    }
}
