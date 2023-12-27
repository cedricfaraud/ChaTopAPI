package com.openclassrooms.ChaTopAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class ChaTopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaTopApiApplication.class, args);
	}

}
