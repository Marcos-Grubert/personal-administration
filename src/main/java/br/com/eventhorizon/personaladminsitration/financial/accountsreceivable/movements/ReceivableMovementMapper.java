package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementResponseDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.ReceivableId;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableResponseDto;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ReceivableMovementMapper {
    ReceivableMovementEntity toEntity(ReceivableMovementCreateDto receivableMovementCreateDto) {
        if (receivableMovementCreateDto == null) {
            return null;
        }

        ReceivableMovementEntity receivableMovementEntity = new ReceivableMovementEntity();

        ReceivableId receivableId = new ReceivableId(
                receivableMovementCreateDto.customerId(),
                receivableMovementCreateDto.document());

        ReceivableMovementId receivableMovementId = new ReceivableMovementId(receivableId,null);

        receivableMovementEntity.setId(receivableMovementId);
        receivableMovementEntity.setMovementValue(receivableMovementCreateDto.movementValue());
        receivableMovementEntity.setInterestValue(receivableMovementCreateDto.interestValue());
        receivableMovementEntity.setDiscountValue(receivableMovementCreateDto.discountValue());
        receivableMovementEntity.setMovementType(receivableMovementCreateDto.movementType());
        receivableMovementEntity.setMovementObservation(receivableMovementCreateDto.movementObservation());

        return receivableMovementEntity;
    }

    ReceivableMovementResponseDto toResponseDto(ReceivableMovementEntity receivableMovementEntity) {
        if (receivableMovementEntity == null) {return null;}

        return new ReceivableMovementResponseDto(
                receivableMovementEntity.getId(),
                receivableMovementEntity.getMovementValue(),
                receivableMovementEntity.getInterestValue(),
                receivableMovementEntity.getDiscountValue(),
                receivableMovementEntity.getMovementType(),
                receivableMovementEntity.getMovementObservation(),
                mapUser(receivableMovementEntity.getCreatedBy()),
                receivableMovementEntity.getCreatedAt()
        );
    }

    private UserResponseDto mapUser(UserEntity userEntity) {
        if (userEntity == null) {return null;}

        return new UserResponseDto(
                userEntity.getUserId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.isActive()
        );
    }
}
