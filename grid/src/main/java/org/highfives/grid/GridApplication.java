package org.highfives.grid;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@SpringBootApplication
@EnableScheduling
public class GridApplication {

	public static void main(String[] args) {
		SpringApplication.run(GridApplication.class, args);
	}

  @Bean
  public ModelMapper modelMapper(){
    return new ModelMapper();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
