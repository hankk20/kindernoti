package kr.co.kindernoti.core.spring.security.role;

import lombok.Getter;

import java.util.List;

@Getter
public class ClientRole implements Role{

    private final String clientName;

    private final List<String> roles;

    private static final String rolePrefix = "client_";

    private ClientRole(String clientName, List<String> roles) {
        this.clientName = clientName;
        this.roles = roles;
    }

    public static ClientRole create(String clientName, List<String> roles) {
        return new ClientRole(clientName, roles.stream().map(s -> rolePrefix+clientName+"_"+s).toList());
    }

}
