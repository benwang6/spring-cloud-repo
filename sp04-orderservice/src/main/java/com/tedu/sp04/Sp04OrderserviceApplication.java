package com.tedu.sp04;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableDiscoveryClient
//@SpringBootApplication

@EnableFeignClients
@SpringCloudApplication
public class Sp04OrderserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sp04OrderserviceApplication.class, args);
	}

}
