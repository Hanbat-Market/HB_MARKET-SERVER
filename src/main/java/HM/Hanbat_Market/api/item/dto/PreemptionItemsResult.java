package HM.Hanbat_Market.api.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PreemptionItemsResult {
    private int preemptionItemSize;
    private List<PreemptionItemDto> preemptionItemDtos;
}
