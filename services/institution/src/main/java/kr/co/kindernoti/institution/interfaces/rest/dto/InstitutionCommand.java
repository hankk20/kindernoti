package kr.co.kindernoti.institution.interfaces.rest.dto;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import lombok.Value;

public class InstitutionCommand {

    @Value(staticConstructor = "of")
    public static class PatchCommand {
        Address address;

        Status status;
    }
}
