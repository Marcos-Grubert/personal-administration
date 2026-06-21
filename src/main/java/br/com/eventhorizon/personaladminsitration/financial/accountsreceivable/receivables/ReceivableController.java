package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableCreateDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivablePendingCollectionDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableResponseDto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto.ReceivableUpdateDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/pendding-collections")
    public ResponseEntity<Page<ReceivablePendingCollectionDto>> getPenddingCollections(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate,
            @PageableDefault(size = 10, sort = "originalDueDate") Pageable pageable
            ) {
        Page<ReceivablePendingCollectionDto> response = receivableService.findPendingReceivables(customerId, startDate, endDate, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
