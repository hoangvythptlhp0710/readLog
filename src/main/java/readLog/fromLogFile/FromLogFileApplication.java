package readLog.fromLogFile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "readLog.fromLogFile" })
//@MapperScan(basePackages = { "readLog.fromLogFile.mapper" })
public class FromLogFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(FromLogFileApplication.class, args);
	}

}
