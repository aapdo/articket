package com.articket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class ArticketApplication {

	public static void main(String[] args) {
		String result;
		SpringApplication.run(ArticketApplication.class, args);
		try {
			result = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			result = "알 수 없는 사용자";
		}
		log.info("url = {}", result);
	}

}
