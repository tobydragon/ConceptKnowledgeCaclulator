package edu.ithaca.dragon.tecmap.ui.springbootui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class TecmapApplication extends WebMvcAutoConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(TecmapApplication.class, args);
    }
}
