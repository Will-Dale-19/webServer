package com.example.webServer;

import com.example.webServer.data.entities.ServerEntity;
import com.example.webServer.data.repositories.ServerRepositoryCustomImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WebServerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void loadServers() {

		ServerRepositoryCustomImpl customImplementation = new ServerRepositoryCustomImpl();

		List<ServerEntity> entityList = customImplementation.findAllServers();

		for(ServerEntity entity : entityList){
			System.out.println(entity.getId());
			System.out.println(entity.getServerName());
			System.out.println(entity.getServerLocation());
		}
	}

}
