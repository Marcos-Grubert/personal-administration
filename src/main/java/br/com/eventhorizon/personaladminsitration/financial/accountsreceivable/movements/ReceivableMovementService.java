package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementResponseDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementRollbackDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.ReceivableEntity;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.ReceivableId;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.ReceivableRepository;
import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;
import br.com.eventhorizon.personaladminsitration.shared.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceivableMovementService {

    private final ReceivableMovementRepository receivableMovementRepository;
    private final ReceivableMovementMapper receivableMovementMapper;
    private final ReceivableRepository receivableRepository;

    public ReceivableMovementService(ReceivableMovementRepository receivableMovementRepository,
                                     ReceivableMovementMapper receivableMovementMapper,
                                     ReceivableRepository receivableRepository) {
        this.receivableMovementRepository = receivableMovementRepository;
        this.receivableMovementMapper = receivableMovementMapper;
        this.receivableRepository = receivableRepository;
    }

    @Transactional
    public List<ReceivableMovementResponseDto> bulkCreate(List<ReceivableMovementCreateDto> dtos) {
        List<ReceivableMovementResponseDto> responses = new ArrayList<>();

        for (ReceivableMovementCreateDto dto : dtos) {
            // Chamamos o método que você já criou e testou
            responses.add(this.create(dto));
        }

        return responses;
    }

    @Transactional
    public ReceivableMovementResponseDto create(ReceivableMovementCreateDto receivableMovementCreateDto) {

        //Consistências gerais
        if(receivableMovementCreateDto.movementValue() == null) {
            throw new BusinessException("Valor do movimento indefinido.");
        }

        if(receivableMovementCreateDto.movementType() == null) {
            throw new BusinessException("Movimento indefinido.");
        }

        if(receivableMovementCreateDto.movementValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor do movimento não pode ser menor ou igual a zero");
        }

        if(receivableMovementCreateDto.interestValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O valor de juros não pode ser menor que zero");
        }

        ReceivableId receivableId = new ReceivableId(receivableMovementCreateDto.customerId(),receivableMovementCreateDto.document());
        ReceivableEntity receivableEntity = receivableRepository.findById(receivableId)
                .orElseThrow(() -> new EntityNotFoundException("Título não encontrado"));

        //Consiste movimento por inclusão de título
        if(receivableMovementCreateDto.movementType() == MovementType.INCLUSAO){

            Integer lastSequence = findLastSequence(receivableId);
            Integer nextSequence = lastSequence + 1;
            ReceivableMovementEntity receivableMovementEntity = receivableMovementMapper.toEntity(receivableMovementCreateDto);

            receivableMovementEntity.getId().setSequenceMovement(nextSequence);

            ReceivableMovementEntity savedEntity = receivableMovementRepository.save(receivableMovementEntity);
            return receivableMovementMapper.toResponseDto(savedEntity);
        }

        //Consiste movimentos do tipo baixa
        if(receivableMovementCreateDto.movementType() == MovementType.BAIXA){

            //Baixa completa
            if(receivableEntity.getRemainingValue().compareTo(receivableMovementCreateDto.movementValue()) == 0){
                receivableEntity.setRemainingValue(BigDecimal.ZERO);
                receivableEntity.setFinancialStatus(FinancialStatus.LIQUIDADO);
                receivableRepository.save(receivableEntity);

                ReceivableMovementEntity receivableMovementEntity = receivableMovementMapper.toEntity(receivableMovementCreateDto);
                Integer lastSequence = findLastSequence(receivableId);
                Integer nextSequence = lastSequence + 1;
                receivableMovementEntity.getId().setSequenceMovement(nextSequence);
                ReceivableMovementEntity savedEntity = receivableMovementRepository.save(receivableMovementEntity);
                return receivableMovementMapper.toResponseDto(savedEntity);
            }

            //Baixa parcial
            if(receivableEntity.getRemainingValue().compareTo(receivableMovementCreateDto.movementValue()) == -1){
                throw new BusinessException("O valor do movimento não pode ser maior que o valor do título");
            }
            if(receivableEntity.getRemainingValue().compareTo(receivableMovementCreateDto.movementValue()) == 1){

                BigDecimal remainingValue = calculateRemainingValue(receivableMovementCreateDto);
                BigDecimal newRemainingValue;

                if(remainingValue.compareTo(BigDecimal.ZERO) <= 0){
                    newRemainingValue = receivableEntity.getRemainingValue();
                } else {
                    newRemainingValue = receivableEntity.getRemainingValue().subtract(remainingValue);
                }

                receivableEntity.setRemainingValue(newRemainingValue);
                receivableRepository.save(receivableEntity);

                ReceivableMovementEntity receivableMovementEntity =  receivableMovementMapper.toEntity(receivableMovementCreateDto);
                receivableMovementEntity.setMovementType(MovementType.BAIXA);
                Integer lastSequence = findLastSequence(receivableId);
                Integer nextSequence = lastSequence + 1;
                receivableMovementEntity.getId().setSequenceMovement(nextSequence);
                ReceivableMovementEntity savedEntity = receivableMovementRepository.save(receivableMovementEntity);
                return receivableMovementMapper.toResponseDto(savedEntity);
            }
        }

        //Baixa por cancelamento
        if(receivableMovementCreateDto.movementType() == MovementType.CANCELAMENTO){
            if(receivableEntity.getRemainingValue().subtract(receivableMovementCreateDto.movementValue()).compareTo(BigDecimal.ZERO) < 0){
                throw new BusinessException("O valor do movimento para cancelamento não pode ser maior que o valor em aberto do título");
            }

            BigDecimal diferenceValue = receivableEntity.getRemainingValue().subtract(receivableMovementCreateDto.movementValue());

            if(receivableMovementCreateDto.movementValue().compareTo(receivableEntity.getRemainingValue()) > 0){
                throw new BusinessException("O valor do movimento "+receivableMovementCreateDto.movementValue()+" não pode ser superior ao valor em aberto no titulo" + receivableEntity.getRemainingValue());
            }

            //se a diferença de valores entre saldo em aberto e valor original do titulo é zero, cancela o titulo
            if (diferenceValue.equals(receivableEntity.getOriginalValue())){
                if(receivableEntity.getRemainingValue().subtract(receivableMovementCreateDto.movementValue()).compareTo(BigDecimal.ZERO) == 0){
                    receivableEntity.setRemainingValue(BigDecimal.ZERO);
                    receivableEntity.setFinancialStatus(FinancialStatus.CANCELADO);
                }
            }

            if(diferenceValue.compareTo(BigDecimal.ZERO) == 0 && receivableEntity.getOriginalValue().compareTo(receivableEntity.getRemainingValue()) > 0){
                receivableEntity.setRemainingValue(BigDecimal.ZERO);
                receivableEntity.setFinancialStatus(FinancialStatus.LIQUIDADO);
            }

            if(diferenceValue.compareTo(BigDecimal.ZERO) > 0 && diferenceValue.compareTo(receivableEntity.getRemainingValue()) < 0){
                BigDecimal remainingValue = receivableEntity.getRemainingValue().subtract(receivableMovementCreateDto.movementValue());
                receivableEntity.setRemainingValue(remainingValue);
                receivableEntity.setFinancialStatus(FinancialStatus.ABERTO);
            }

            receivableRepository.save(receivableEntity);

            ReceivableMovementEntity receivableMovementEntity = receivableMovementMapper.toEntity(receivableMovementCreateDto);
            Integer lastSequence = findLastSequence(receivableId);
            Integer nextSequence = lastSequence + 1;
            receivableMovementEntity.getId().setSequenceMovement(nextSequence);
            ReceivableMovementEntity savedEntity = receivableMovementRepository.save(receivableMovementEntity);
            return receivableMovementMapper.toResponseDto(savedEntity);
        }

        throw new BusinessException("Operação não mapeada.");
    }

    @Transactional
    public ReceivableMovementResponseDto rollback(ReceivableMovementRollbackDto receivableMovementRollbackDto) {
        ReceivableId receivableId = new ReceivableId(receivableMovementRollbackDto.customerId(),receivableMovementRollbackDto.document());
        ReceivableEntity receivableEntity = receivableRepository.findById(receivableId)
                .orElseThrow(() -> new EntityNotFoundException("Título não encontrado"));

        //Baixa por estorno
        if(receivableMovementRollbackDto.movementType() == MovementType.ESTORNO){
            Integer lastSequence = findLastSequence(receivableId);
            ReceivableMovementId receivableMovementId = new ReceivableMovementId(receivableId,lastSequence);
            ReceivableMovementEntity receivableMovementEntity = receivableMovementRepository.findById(receivableMovementId)
                    .orElseThrow(() -> new EntityNotFoundException("Movimento não encontrado"));

            if(receivableMovementEntity.getMovementType().equals(MovementType.ESTORNO)){
                throw new BusinessException("Ultimo movimento já é de ESTORNO.");
            }

            //estorna baixa
            if(receivableMovementEntity.getMovementType().equals(MovementType.BAIXA)){
                BigDecimal previousMovementValue = receivableMovementEntity.getMovementValue();
                BigDecimal remaingValue = receivableEntity.getRemainingValue().add(previousMovementValue);
                receivableEntity.setRemainingValue(remaingValue);
                receivableEntity.setFinancialStatus(FinancialStatus.ABERTO);
                receivableRepository.save(receivableEntity);

                //Criação do movimento de baixa
                ReceivableMovementEntity receivableMovementEntityToReverse = new ReceivableMovementEntity();
                ReceivableMovementId reverseMovementId = new ReceivableMovementId(receivableId,lastSequence + 1);

                receivableMovementEntityToReverse.setId(reverseMovementId);
                receivableMovementEntityToReverse.setMovementType(MovementType.ESTORNO);
                receivableMovementEntityToReverse.setMovementValue(previousMovementValue);
                receivableMovementEntityToReverse.setMovementObservation(receivableMovementRollbackDto.movementObservation());
                receivableMovementRepository.save(receivableMovementEntityToReverse);
                return receivableMovementMapper.toResponseDto(receivableMovementEntityToReverse);
            }

            //estorna cancelamento
            if(receivableMovementEntity.getMovementType().equals(MovementType.CANCELAMENTO)){
                BigDecimal previousMovementValue = receivableMovementEntity.getMovementValue();
                BigDecimal remainingValue = receivableEntity.getRemainingValue();
                BigDecimal originalValue = receivableEntity.getOriginalValue();
                BigDecimal newRemainingValue;
                if(previousMovementValue.add(remainingValue).compareTo(originalValue) == 1){
                    newRemainingValue = originalValue;
                } else {
                    newRemainingValue = remainingValue.add(previousMovementValue);
                }
                receivableEntity.setRemainingValue(newRemainingValue);
                receivableEntity.setFinancialStatus(FinancialStatus.ABERTO);
                receivableRepository.save(receivableEntity);

                ReceivableMovementEntity receivableMovementEntityToReverse = new ReceivableMovementEntity();
                ReceivableMovementId reverseMovementId = new ReceivableMovementId(receivableId, lastSequence + 1);

                receivableMovementEntityToReverse.setId(reverseMovementId);
                receivableMovementEntityToReverse.setMovementType(MovementType.ESTORNO);
                receivableMovementEntityToReverse.setMovementValue(previousMovementValue);
                ReceivableMovementEntity savedEntity = receivableMovementRepository.save(receivableMovementEntityToReverse);
                return receivableMovementMapper.toResponseDto(savedEntity);
            }
        }
        throw new BusinessException("Operação não mapeada.");
    }

    private Integer findLastSequence(ReceivableId receivableId){
        Integer lastSequence = receivableMovementRepository.findMaxSequence(receivableId.getCustomerId(), receivableId.getDocument());
        return lastSequence == null ? 0 : lastSequence;
    }

    private BigDecimal calculateRemainingValue(ReceivableMovementCreateDto receivableMovementCreateDto){
        BigDecimal movement = receivableMovementCreateDto.movementValue();
        BigDecimal interest = receivableMovementCreateDto.interestValue() != null ? receivableMovementCreateDto.interestValue() : BigDecimal.ZERO;
        BigDecimal discount = receivableMovementCreateDto.discountValue() != null ? receivableMovementCreateDto.discountValue() : BigDecimal.ZERO;

        return movement.add(interest).subtract(discount);
    }
}
