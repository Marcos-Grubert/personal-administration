package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableResponseDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/financial/receivables")
public class ReceivableController {

    private final ReceivableService receivableService;

    public ReceivableController(ReceivableService receivableService) {
        this.receivableService = receivableService;
    }

    @PostMapping
    public ResponseEntity<ReceivableResponseDto> create(@RequestBody @Valid ReceivableCreateDto receivableCreateDto) {
        ReceivableResponseDto receivableResponseDto = receivableService.create(receivableCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(receivableResponseDto);
    }

    @GetMapping("/{customer}/{document}")
    public ResponseEntity<ReceivableResponseDto> findById(@PathVariable Long customer, @PathVariable String document) {
        ReceivableId receivableId =  new ReceivableId(customer, document);
        ReceivableResponseDto receivableResponseDto = receivableService.findById(receivableId);
        return ResponseEntity.status(HttpStatus.OK).body(receivableResponseDto);
    }

    @PutMapping("/{customer}/{document}")
    public ResponseEntity<ReceivableResponseDto> update(
            @PathVariable Long customer,
            @PathVariable String document,
            @RequestBody @Valid ReceivableUpdateDto receivableUpdateDto) {
        ReceivableId receivableId =  new ReceivableId(customer, document);
        ReceivableResponseDto responseDto = receivableService.update(receivableId, receivableUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
