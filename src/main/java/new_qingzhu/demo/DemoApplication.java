package new_qingzhu.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@MapperScan("new_qingzhu.demo.Mapper")
@SpringBootApplication
public class DemoApplication {

    @Bean
    public BCryptPasswordEncoder encoding(){
        return new BCryptPasswordEncoder();
    }
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
