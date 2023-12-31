package readLog.fromLogFile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Time;
import java.util.Date;

@Document(collection = "logs-by-company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    private Date dateTime;
    private String phoneNumber;
    private Long companyId;
    private Long userId;
    private String service;
    private String requestId;

}
