package br.com.eventhorizon.personaladminsitration.register.customers;

import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerResponseDto;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerEntity toEntity(CustomerCreateDto customerCreateDto) {
        CustomerEntity costumer = new CustomerEntity();
        costumer.setName(customerCreateDto.name());
        costumer.setType(customerCreateDto.type());
        costumer.setDocumentCode(customerCreateDto.documentCode());
        costumer.setEmail(customerCreateDto.email());
        if(customerCreateDto.situation() != null) {
            costumer.setSituation(customerCreateDto.situation());
        }
        return costumer;
    }

    public CustomerResponseDto toResponse(CustomerEntity costumer) {
        return new CustomerResponseDto(
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
