package readLog.fromLogFile.dto.response;

import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountByDay {
    private Date date;
    private Long userId;
    private String userName;
    private String phoneNumber;
    private Long companyId;
    private String companyName;
    private Integer count;
}
