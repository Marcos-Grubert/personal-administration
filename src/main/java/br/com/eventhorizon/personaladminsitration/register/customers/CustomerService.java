package br.com.eventhorizon.personaladminsitration.register.customers;

import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerLookupDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerUpdateDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.customers.dto.CustomerResponseDto;
import br.com.eventhorizon.personaladminsitration.shared.exception.BusinessException;
import br.com.eventhorizon.personaladminsitration.shared.exception.EmailAlreadyInUseException;
import br.com.eventhorizon.personaladminsitration.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerResponseDto create(CustomerCreateDto customerCreateDto) {
        if(customerRepository.existsByEmail(customerCreateDto.email())) {
            throw new EmailAlreadyInUseException("E-mail informado já está em uso.");
        }
        CustomerEntity costumer = customerMapper.toEntity(customerCreateDto);
        CustomerEntity savedUser = customerRepository.save(costumer);
        return customerMapper.toResponse(savedUser);
    }

/*    @Transactional(readOnly = true)
    public List<CustomerResponseDto> readAll() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .toList();
    }*/

    @Transactional(readOnly = true)
    public Page<CustomerLookupDto> searchCustomersLookup(String term, int page, int size) {

        if(term == null || term.isBlank()) {
            throw new BusinessException("O termo não pode ser nulo ou vazio.");
        }

        if(size < 1) {
            throw new BusinessException("O valor para quantidade de registros \"size\" por página é obrigatório.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        String cleanTerm = term.trim();

        if(cleanTerm.contains("@")) {
            return customerRepository.findByEmailContainingIgnoreCase(cleanTerm,pageable)
                    .map(CustomerLookupDto::new);
        }

        if(cleanTerm.matches("[0-9.\\-/]+")) {
            String documentWithoutMask = cleanTerm.replaceAll("[.\\-/]","");
            return customerRepository.findByDocumentCodeContainingIgnoreCase(documentWithoutMask,pageable)
                    .map(CustomerLookupDto::new);

        }

        return customerRepository.findByNameContainingIgnoreCase(cleanTerm, pageable)
                .map(CustomerLookupDto::new);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto readById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
    }

    @Transactional
    public CustomerResponseDto update(Long id, CustomerUpdateDto customerUpdateDto) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        if(customerUpdateDto.email() != null && !customerUpdateDto.email().equals(customer.getEmail())) {
            if(customerRepository.existsByEmail(customerUpdateDto.email())) {
                throw new EmailAlreadyInUseException("O novo e-mail já está em uso por outro usuário");
            }
        }

        updateData(customer, customerUpdateDto);

        return customerMapper.toResponse(customer);
    }

    private void updateData(CustomerEntity custumerEntity, CustomerUpdateDto customerUpdateDto) {
        if(customerUpdateDto.name() != null){
            custumerEntity.setName(customerUpdateDto.name());
        }
        if(customerUpdateDto.email() != null){
            custumerEntity.setEmail(customerUpdateDto.email());
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado para exclusão.");
        }
        customerRepository.deleteById(id);
    }
}
