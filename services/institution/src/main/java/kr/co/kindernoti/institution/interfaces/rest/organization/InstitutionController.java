package kr.co.kindernoti.institution.interfaces.rest.organization;

import jakarta.validation.constraints.NotBlank;
import kr.co.kindernoti.institution.application.in.org.InstitutionSearchUseCase;
import kr.co.kindernoti.institution.application.in.org.InstitutionUseCase;
import kr.co.kindernoti.institution.interfaces.rest.dto.InstitutionResponse;
import kr.co.kindernoti.institution.interfaces.rest.mapper.InstitutionInterfaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class InstitutionController {

    private final InstitutionSearchUseCase searchUseCase;

    private final InstitutionUseCase institutionUseCase;

    private final InstitutionInterfaceMapper mapper;

    @GetMapping("/institutions")
    public Flux<InstitutionResponse> search(@RequestParam("name") @Validated @NotBlank String name) {
        return searchUseCase.search(name)
                .map(mapper::toResponse);
    }

    @PostMapping("/institutions/{institutionId}")
    public Mono<ResponseEntity<InstitutionResponse>> registryInstitution(@PathVariable String institutionId) {
        return institutionUseCase.saveInstitution(institutionId)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok);
    }


}
