package HM.Hanbat_Market.service.account.apple.apple.model;

import java.util.List;
import lombok.Data;

@Data
public class Keys {

    private List<Key> keys;

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

}
