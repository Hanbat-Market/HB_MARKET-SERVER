package HM.Hanbat_Market.api.member.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ProfileImageResponse {
    private String mail;
    private String imageFileName;
}
