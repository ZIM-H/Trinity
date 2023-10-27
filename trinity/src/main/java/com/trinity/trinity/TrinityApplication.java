package com.trinity.trinity;

import com.trinity.trinity.webSocket.DiscardServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrinityApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TrinityApplication.class, args);
		new DiscardServer(8589).run();
	}

}
