package readLog.fromLogFile.model;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {
    private Integer userId;
    private String username;
    private Integer companyId;
    private String companyName;
}
