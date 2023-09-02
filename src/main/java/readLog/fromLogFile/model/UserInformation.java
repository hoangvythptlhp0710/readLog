package readLog.fromLogFile.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user-information")
public class UserInformation {
    private Long userId;
    private String username;
    private String phoneNumber;
    private Long companyId;
    private String companyName;
}
