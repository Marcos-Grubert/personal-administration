package br.com.eventhorizon.personaladminsitration.register.customers;

import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerLookupDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerResponseDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerUpdateDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register/costumers")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService costumerService) {
        this.customerService = costumerService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDto> create(@RequestBody @Valid CustomerCreateDto customerCreateDto) {
        CustomerResponseDto responseDto = customerService.create(customerCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

/*    @GetMapping("/read")
    public ResponseEntity<List<CustomerResponseDto>> read() {
        List<CustomerResponseDto> customerResponseDto = custumerService.readAll();
        return ResponseEntity.ok(customerResponseDto);
    }*/

    @GetMapping("/read")
    public ResponseEntity<Page<CustomerLookupDto>> read(@RequestParam (required = false) String term,
                                                        @RequestParam (defaultValue = "0") Integer page,
                                                        @RequestParam (defaultValue = "20") Integer size) {
        Page<CustomerLookupDto> customers = customerService.searchCustomersLookup(term, page, size);

        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Long id,
            @RequestBody CustomerUpdateDto customerUpdateDto
    ) {
        CustomerResponseDto response = customerService.update(id, customerUpdateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

