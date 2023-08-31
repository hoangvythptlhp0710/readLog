package readLog.fromLogFile.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CountByDay {
    private Date date;
    private Integer userId;
    private String userName;
    private Integer companyId;
    private String companyName;
    private Integer count;
}
