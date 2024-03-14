package HM.Hanbat_Market.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {

    private int status;

    private String errorCode;

    private String message;
}
