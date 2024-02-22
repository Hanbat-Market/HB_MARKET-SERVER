package HM.Hanbat_Market.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Result<T> {
    private T data;
}
