package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivablePendingCollectionDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableResponseDto;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ReceivableMapper {
    public ReceivableEntity toEntity(ReceivableCreateDto  receivableCreateDto) {
        if (receivableCreateDto == null) {
            return null;
        }
        ReceivableEntity receivableEntity = new ReceivableEntity();

        ReceivableId id = new ReceivableId(receivableCreateDto.customerCode(),receivableCreateDto.document());
        receivableEntity.setId(id);
        receivableEntity.setDestit(receivableCreateDto.destit());
        receivableEntity.setOriginalValue(receivableCreateDto.originalValue());
        receivableEntity.setOriginalDueDate(receivableCreateDto.originalDueDate());

        return receivableEntity;
    }

    public ReceivableResponseDto toResponse(ReceivableEntity receivableEntity) {
        return new ReceivableResponseDto(
                receivableEntity.getId(),
                receivableEntity.getOriginalValue(),
                receivableEntity.getDestit(),
                receivableEntity.getFinancialStatus(),
                mapUser(receivableEntity.getCreatedBy()),
                receivableEntity.getCreatedAt()
        );
    }

    public ReceivablePendingCollectionDto toPendingCollectionDto(ReceivableEntity entity) {
        if (entity == null) return null;

        return new ReceivablePendingCollectionDto(
                entity.getId(),
                entity.getCustomer().getName(),
                entity.getOriginalValue(),
                entity.getRemainingValue(),
                entity.getDestit(),
                entity.getFinancialStatus(),
                entity.getOriginalDueDate(),
                mapUser(entity.getCreatedBy()),
                entity.getCreatedAt()
        );
    }

    private UserResponseDto mapUser(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new UserResponseDto(
                userEntity.getUserId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.isActive()
        );
    }
}
