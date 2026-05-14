package br.com.eventhorizon.personaladminsitration.register.costumers;

import br.com.eventhorizon.personaladminsitration.register.costumers.dto.CostumerCreateDto;
import br.com.eventhorizon.personaladminsitration.register.costumers.dto.CostumerResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register/costumers")
public class CostumerController {
    private CostumerService costumerService;

    public CostumerController(CostumerService costumerService) {
        this.costumerService = costumerService;
    }

    @PostMapping("/create")
    public CostumerResponseDto create(@RequestBody CostumerCreateDto costumerCreateDto) {
        return costumerService.create(costumerCreateDto);
    }
}

