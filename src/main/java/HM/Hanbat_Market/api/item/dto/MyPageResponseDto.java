package HM.Hanbat_Market.api.item.dto;

import HM.Hanbat_Market.domain.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MyPageResponseDto {
    private int preemptionSize;
    private List<ReservedDto> reservedDtos;
    private List<CompletedDto> completedDtos;
}
