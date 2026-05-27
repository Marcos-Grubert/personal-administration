package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto.ReceivableMovementResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/financial/receivables/movements"))
public class ReceivableMovementController {
    private final ReceivableMovementService receivableMovementService;

    public ReceivableMovementController(ReceivableMovementService receivableMovementService) {
        this.receivableMovementService = receivableMovementService;
    }

    @PostMapping
    public ResponseEntity<ReceivableMovementResponseDto> create(@RequestBody @Valid ReceivableMovementCreateDto receivableMovementCreateDto) {
        ReceivableMovementResponseDto receivableMovementResponseDto = receivableMovementService.create(receivableMovementCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(receivableMovementResponseDto);
    }
}
