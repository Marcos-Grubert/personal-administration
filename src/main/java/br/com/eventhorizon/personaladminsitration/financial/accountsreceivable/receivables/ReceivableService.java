package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableResponseDto;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import br.com.eventhorizon.personaladminsitration.shared.exception.ResourceAlreadyExistsException;
import br.com.eventhorizon.personaladminsitration.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceivableService {
    private final ReceivableMapper receivableMapper;
    private final ReceivableRepository receivableRepository;

    public ReceivableService(ReceivableMapper receivableMapper, ReceivableRepository receivableRepository) {
        this.receivableMapper = receivableMapper;
        this.receivableRepository = receivableRepository;
    }

    @Transactional
    public ReceivableResponseDto create(ReceivableCreateDto receivableCreateDto) {
        ReceivableId compositeId = new ReceivableId(receivableCreateDto.customerCode(),receivableCreateDto.document());
        if(receivableRepository.existsById(compositeId)){
            throw new ResourceAlreadyExistsException("O título "+receivableCreateDto.document() + " do cliente " + receivableCreateDto.customerCode()+ " já existe.");
        }

        ReceivableEntity receivableEntity = receivableMapper.toEntity(receivableCreateDto);

        ReceivableEntity savedEntity = receivableRepository.save(receivableEntity);

        return receivableMapper.toResponse(savedEntity);
    }

    @Transactional(readOnly = true)
    public ReceivableResponseDto findById(ReceivableId receivableId) {
        return receivableRepository.findById(receivableId)
                .map(receivableMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("O título informado não foi encontrado."));
    }
}
