package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.ReceivableMovementEntity;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.ReceivableMovementId;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.ReceivableMovementRepository;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableResponseDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableUpdateDto;
import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;
import br.com.eventhorizon.personaladminsitration.shared.exception.BusinessException;
import br.com.eventhorizon.personaladminsitration.shared.exception.ResourceAlreadyExistsException;
import br.com.eventhorizon.personaladminsitration.shared.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

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
        movementEntity.setMovementValue(BigDecimal.ZERO);
        movementEntity.setInterestValue(BigDecimal.ZERO);
        movementEntity.setDiscountValue(BigDecimal.ZERO);
        movementEntity.setMovementType(MovementType.INCLUSAO);
        movementEntity.setMovementObservation("Movimento criado por inclusão do título");
        movementEntity.setMovementInstant(Instant.now());

        receivableMovementRepository.save(movementEntity);

        return receivableMapper.toResponse(savedEntity);
    }

    @Transactional(readOnly = true)
    public ReceivableResponseDto findById(ReceivableId receivableId) {
        return receivableRepository.findById(receivableId)
                .map(receivableMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("O título informado não foi encontrado."));
    }

    @Transactional
    public ReceivableResponseDto update(ReceivableId id, ReceivableUpdateDto receivableUpdateDto) {
        ReceivableEntity receivableEntity = receivableRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("O titulo informado não foi encontrado."));
        Integer maxSequence = receivableMovementRepository.findMaxSequence(id.getCustomerId(),id.getDocument());
        if(maxSequence == null){
            throw  new BusinessException("Não foram encontrados movimentos para o titulo informado.");
        }
        if (maxSequence > 1){
            throw new BusinessException("Não é possível atualizar o título pois já existem movimentos superiores ao de Inserção.");
        }
        if (maxSequence == 1){
            receivableEntity.setOriginalValue(receivableUpdateDto.originalValue());
            receivableEntity.setRemainingValue(receivableUpdateDto.originalValue());
            receivableEntity.setOriginalDueDate(receivableUpdateDto.originalDueDate());
            receivableEntity.setDueDate(receivableUpdateDto.originalDueDate());

            ReceivableMovementId receivableMovementId = new ReceivableMovementId(receivableEntity.getId(),1);
            ReceivableMovementEntity receivableMovementEntity = receivableMovementRepository.findById(receivableMovementId)
                    .orElseThrow(() -> new EntityNotFoundException("Movimento não encontrado."));
            receivableMovementEntity.setMovementValue(BigDecimal.ZERO);
            receivableMovementEntity.setMovementInstant(Instant.now());

            //Não é necessário informar .save ao alterar registros devido o dirtycheck do hibernate, se identificar que tem alterações entre o banco e a memoria ele vai la no banco e grava a informação, portanto a alteração fica implicita no codigo
            return receivableMapper.toResponse(receivableEntity);
        }

        throw new BusinessException("Operação não prevista.");
    }
}
