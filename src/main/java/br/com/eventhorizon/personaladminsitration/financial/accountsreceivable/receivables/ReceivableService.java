package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.ReceivableMovementEntity;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.ReceivableMovementId;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.ReceivableMovementRepository;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableResponseDto;
import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;
import br.com.eventhorizon.personaladminsitration.shared.exception.ResourceAlreadyExistsException;
import br.com.eventhorizon.personaladminsitration.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ReceivableService {
    private final ReceivableMapper receivableMapper;
    private final ReceivableRepository receivableRepository;
    private final ReceivableMovementRepository receivableMovementRepository;

    public ReceivableService(ReceivableMapper receivableMapper, ReceivableRepository receivableRepository, ReceivableMovementRepository receivableMovementRepository) {
        this.receivableMapper = receivableMapper;
        this.receivableRepository = receivableRepository;
        this.receivableMovementRepository = receivableMovementRepository;
    }

    @Transactional
    public ReceivableResponseDto create(ReceivableCreateDto receivableCreateDto) {
        ReceivableId compositeId = new ReceivableId(receivableCreateDto.customerCode(),receivableCreateDto.document());
        if(receivableRepository.existsById(compositeId)){
            throw new ResourceAlreadyExistsException("O título "+receivableCreateDto.document() + " do cliente " + receivableCreateDto.customerCode()+ " já existe.");
        }

        ReceivableEntity receivableEntity = receivableMapper.toEntity(receivableCreateDto);
        receivableEntity.setFinancialStatus(FinancialStatus.ABERTO);

        ReceivableEntity savedEntity = receivableRepository.save(receivableEntity);

        ReceivableMovementEntity movementEntity = new ReceivableMovementEntity();
        ReceivableMovementId movementId = new ReceivableMovementId(savedEntity.getId(),1);
        movementEntity.setId(movementId);

        movementEntity.setReceivable(savedEntity);
        movementEntity.setMovementValue(savedEntity.getOriginalValue());
        movementEntity.setInterestValue(BigDecimal.ZERO);
        movementEntity.setDiscountValue(BigDecimal.ZERO);
        movementEntity.setMovementType(MovementType.INCLUSAO);
        movementEntity.setMovementObservation("Movimento criado por inclusão do título");

        receivableMovementRepository.save(movementEntity);

        return receivableMapper.toResponse(savedEntity);
    }

    @Transactional(readOnly = true)
    public ReceivableResponseDto findById(ReceivableId receivableId) {
        return receivableRepository.findById(receivableId)
                .map(receivableMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("O título informado não foi encontrado."));
    }
}
