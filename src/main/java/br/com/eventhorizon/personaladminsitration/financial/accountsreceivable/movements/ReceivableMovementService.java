package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementResponseDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementUpdateDto;
import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;
import br.com.eventhorizon.personaladminsitration.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceivableMovementService {
    private final ReceivableMovementRepository receivableMovementRepository;
    private final ReceivableMovementMapper receivableMovementMapper;

    public ReceivableMovementService(ReceivableMovementRepository receivableMovementRepository, ReceivableMovementMapper receivableMovementMapper) {
        this.receivableMovementRepository = receivableMovementRepository;
        this.receivableMovementMapper = receivableMovementMapper;
    }

    @Transactional
    public ReceivableMovementResponseDto create(ReceivableMovementCreateDto receivableMovementCreateDtoCreateDto) {

        ReceivableMovementEntity receivableMovementEntity = receivableMovementMapper.toEntity(receivableMovementCreateDtoCreateDto);
        Integer lastSequence = receivableMovementRepository.findMaxSequence(
                receivableMovementEntity
                        .getId()
                        .getReceivableId()
                        .getCustomerId(),
                receivableMovementEntity.getId()
                        .getReceivableId()
                        .getDocument());
        Integer nextSequence = lastSequence + 1;

        receivableMovementEntity.getId().setSequenceMovement(nextSequence);

        ReceivableMovementEntity savedEntity = receivableMovementRepository.save(receivableMovementEntity);
        return receivableMovementMapper.toResponseDto(savedEntity);
    }

    /*@Transactional
    public ReceivableMovementResponseDto update(ReceivableMovementUpdateDto receivableMovementUpdateDto) {

        if(receivableMovementUpdateDto.movementType() == null) {
            return  null;
        }


        //Alteração de inclusão
        if(receivableMovementUpdateDto.movementType() == MovementType.INCLUSAO){
            ReceivableMovementEntity receivableMovementEntity = receivableMovementRepository
                    .findById(receivableMovementUpdateDto.receivableMovementId())
                    .orElseThrow(() -> new ResourceNotFoundException("O movimento informado não existe."));

            if(receivableMovementEntity.getMovementType() != MovementType.INCLUSAO) {
                new Res
            }
        }


    }*/
}
