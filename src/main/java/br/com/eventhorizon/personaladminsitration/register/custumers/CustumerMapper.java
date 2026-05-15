package br.com.eventhorizon.personaladminsitration.register.custumers;

import br.com.eventhorizon.personaladminsitration.register.custumers.dto.CustumerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.custumers.dto.CustumerResponseDto;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CustumerMapper {
    public CustumerEntity toEntity(CustumerCreateDto custumerCreateDto) {
        CustumerEntity costumer = new CustumerEntity();
        costumer.setName(custumerCreateDto.name());
        costumer.setType(custumerCreateDto.type());
        costumer.setDocumentCode(custumerCreateDto.documentCode());
        costumer.setEmail(custumerCreateDto.email());
        if(custumerCreateDto.situation() != null) {
            costumer.setSituation(custumerCreateDto.situation());
        }
        return costumer;
    }

    public CustumerResponseDto toResponse(CustumerEntity costumer) {
        return new CustumerResponseDto(
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
