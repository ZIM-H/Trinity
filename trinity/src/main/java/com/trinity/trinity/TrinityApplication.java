package com.trinity.trinity;


import com.trinity.trinity.global.webSocket.WebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TrinityApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = SpringApplication.run(TrinityApplication.class, args);
		WebSocketServer server = context.getBean(WebSocketServer.class);
		server.start();

	}
}