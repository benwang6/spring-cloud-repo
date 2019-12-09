package cn.tedu.sp12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class Sp12ConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sp12ConfigApplication.class, args);
	}

}
