package HM.Hanbat_Market.service.account.apple.apple.model;

import lombok.Data;

@Data
public class AppsResponse {

    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
