package br.com.eventhorizon.personaladminsitration.register.custumers;

import br.com.eventhorizon.personaladminsitration.register.custumers.dto.CustumerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.custumers.dto.CustumerResponseDto;
import br.com.eventhorizon.personaladminsitration.register.custumers.dto.CustumerUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register/costumers")
public class CustumerController {
    private CustumerService custumerService;

    public CustumerController(CustumerService costumerService) {
        this.custumerService = costumerService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustumerResponseDto> create(@RequestBody @Valid CustumerCreateDto custumerCreateDto) {
        CustumerResponseDto responseDto = custumerService.create(custumerCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/read")
    public ResponseEntity<List<CustumerResponseDto>> read() {
        List<CustumerResponseDto> custumerResponseDto = custumerService.readAll();
        return ResponseEntity.ok(custumerResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustumerResponseDto> update(
            @PathVariable Long id,
            @RequestBody CustumerUpdateDto custumerUpdateDto
    ) {
        CustumerResponseDto response = custumerService.update(id, custumerUpdateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        custumerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

