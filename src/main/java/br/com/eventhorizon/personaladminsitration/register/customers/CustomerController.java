package br.com.eventhorizon.personaladminsitration.register.customers;

import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerResponseDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register/costumers")
public class CustomerController {
    private CustomerService custumerService;

    public CustomerController(CustomerService costumerService) {
        this.custumerService = costumerService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDto> create(@RequestBody @Valid CustomerCreateDto customerCreateDto) {
        CustomerResponseDto responseDto = custumerService.create(customerCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/read")
    public ResponseEntity<List<CustomerResponseDto>> read() {
        List<CustomerResponseDto> customerResponseDto = custumerService.readAll();
        return ResponseEntity.ok(customerResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Long id,
            @RequestBody CustomerUpdateDto customerUpdateDto
    ) {
        CustomerResponseDto response = custumerService.update(id, customerUpdateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        custumerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

