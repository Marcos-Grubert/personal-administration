package br.com.eventhorizon.personaladminsitration.register.costumers;

import br.com.eventhorizon.personaladminsitration.register.costumers.dto.CostumerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.costumers.dto.CostumerResponseDto;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CostumerMapper {
    public CostumerEntity toEntity(CostumerCreateDto costumerCreateDto) {
        CostumerEntity costumer = new CostumerEntity();
        costumer.setName(costumerCreateDto.name());
        costumer.setType(costumerCreateDto.type());
        costumer.setDocumentCode(costumerCreateDto.documentCode());
        costumer.setEmail(costumerCreateDto.email());
        if(costumerCreateDto.situation() != null) {
            costumer.setSituation(costumerCreateDto.situation());
        }
        return costumer;
    }

    public CostumerResponseDto toResponse(CostumerEntity costumer) {
        return new CostumerResponseDto(
                costumer.getId(),
                costumer.getName(),
                costumer.getType(),
                costumer.getDocumentCode(),
                costumer.getEmail(),
                costumer.isSituation(),
                mapUser(costumer.getUserRegister()),
                costumer.getCreatedAt()
        );
    }

    private UserResponseDto mapUser(UserEntity user) {
        if (user == null) return null;
        return new UserResponseDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.isActive()
        );
    }
}
