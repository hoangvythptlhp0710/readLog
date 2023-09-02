package readLog.fromLogFile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserInformationResponse {
    private Long userId;
    private String username;
    private String phoneNumber;
    private Long companyId;
    private String companyName;
}
