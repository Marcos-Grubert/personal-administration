package br.com.eventhorizon.personaladminsitration.register.custumers;

import br.com.eventhorizon.personaladminsitration.register.custumers.dto.CustumerUpdateDto;
import br.com.eventhorizon.personaladminsitration.register.custumers.exception.EmailAlreadyInUseException;
import br.com.eventhorizon.personaladminsitration.register.custumers.exception.ResourceNotFoundException;
import br.com.eventhorizon.personaladminsitration.register.custumers.dto.CustumerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.custumers.dto.CustumerResponseDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustumerService {
    private final CustumerRepository custumerRepository;
    private final CustumerMapper custumerMapper;

    public CustumerService(CustumerRepository custumerRepository, CustumerMapper custumerMapper) {
        this.custumerRepository = custumerRepository;
        this.custumerMapper = custumerMapper;
    }

    @Transactional
    public CustumerResponseDto create(CustumerCreateDto custumerCreateDto) {
        if(custumerRepository.existsByEmail(custumerCreateDto.email())) {
            throw new EmailAlreadyInUseException("E-mail informado já está em uso.");
        }
        CustumerEntity costumer = custumerMapper.toEntity(custumerCreateDto);
        CustumerEntity savedUser = custumerRepository.save(costumer);
        return custumerMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<CustumerResponseDto> readAll() {
        return custumerRepository.findAll()
                .stream()
                .map(custumerMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustumerResponseDto readById(Long id) {
        return custumerRepository.findById(id)
                .map(custumerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
    }

    @Transactional
    public CustumerResponseDto update(Long id, CustumerUpdateDto custumerUpdateDto) {
        CustumerEntity custumer = custumerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        if(custumerUpdateDto.email() != null && !custumerUpdateDto.email().equals(custumer.getEmail())) {
            if(custumerRepository.existsByEmail(custumerUpdateDto.email())) {
                throw new EmailAlreadyInUseException("O novo e-mail já está em uso por outro usuário");
            }
        }

        updateData(custumer, custumerUpdateDto);

        return custumerMapper.toResponse(custumer);
    }

    private void updateData(CustumerEntity custumerEntity, CustumerUpdateDto custumerUpdateDto) {
        if(custumerUpdateDto.name() != null){
            custumerEntity.setName(custumerUpdateDto.name());
        }
        if(custumerUpdateDto.email() != null){
            custumerEntity.setEmail(custumerUpdateDto.email());
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!custumerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado para exclusão.");
        }
        custumerRepository.deleteById(id);
    }
}
