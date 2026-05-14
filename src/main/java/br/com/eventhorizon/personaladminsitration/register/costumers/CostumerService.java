package br.com.eventhorizon.personaladminsitration.register.costumers;

import br.com.eventhorizon.personaladminsitration.commom.exception.EmailAlreadyInUseException;
import br.com.eventhorizon.personaladminsitration.register.costumers.dto.CostumerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.costumers.dto.CostumerResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CostumerService {
    private final CostumerRepository costumerRepository;
    private final CostumerMapper costumerMapper;

    public CostumerService(CostumerRepository costumerRepository, CostumerMapper costumerMapper) {
        this.costumerMapper = costumerMapper;
        this.costumerRepository = costumerRepository;
    }

    public List<CostumerResponseDto> getCostumers() {
        return costumerRepository.findAll()
                .stream()
                .map(costumerMapper::toResponse)
                .toList();
    }

    public CostumerResponseDto create(CostumerCreateDto costumerCreateDto) {
        if(costumerRepository.findByEmail(costumerCreateDto.email()).isPresent()) {
            throw new EmailAlreadyInUseException("E-mail informado já está em uso.");
        }

        CostumerEntity costumer = costumerMapper.toEntity(costumerCreateDto);
        CostumerEntity savedUser = costumerRepository.save(costumer);

        return costumerMapper.toResponse(savedUser);
    }
}
