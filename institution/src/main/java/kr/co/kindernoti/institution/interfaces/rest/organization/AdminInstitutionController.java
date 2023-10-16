package kr.co.kindernoti.institution.interfaces.rest.organization;

import kr.co.kindernoti.institution.application.in.org.InstitutionSearchUseCase;
import kr.co.kindernoti.institution.application.in.org.InstitutionUseCase;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.interfaces.rest.dto.InstitutionCommand;
import kr.co.kindernoti.institution.interfaces.rest.dto.InstitutionResponse;
import kr.co.kindernoti.institution.interfaces.rest.mapper.InstitutionInterfaceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/institutions")
public class AdminInstitutionController {

    private final InstitutionUseCase institutionUseCase;
    private final InstitutionSearchUseCase institutionSearchUseCase;
    private final InstitutionInterfaceMapper institutionInterfaceMapper;

    /**
     * 기관 정보를 수정한다.
     * @param id
     * @return
     */
    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<InstitutionResponse>> patchInstitution(@PathVariable String id, @RequestBody InstitutionCommand.PatchCommand patchCommand){
        return institutionSearchUseCase.findById(IdCreator.creator(InstitutionId.class).from(id))
                .flatMap(before -> {
                    Institution institution = institutionInterfaceMapper.patchToDomain(patchCommand, before);
                    return institutionUseCase.save(institution)
                            .map(institutionInterfaceMapper::toResponse)
                            .map(ResponseEntity::ok);
                })
                .onErrorMap(e -> {
                    log.error("[json error]", e);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생 하였습니다.");
                });
    }
}
