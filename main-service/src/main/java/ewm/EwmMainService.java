package ewm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "ewm")
@EnableFeignClients(basePackages = "ewm")
public class EwmMainService {
    public static void main(String[] args) {
        SpringApplication.run(EwmMainService.class, args);
    }
}