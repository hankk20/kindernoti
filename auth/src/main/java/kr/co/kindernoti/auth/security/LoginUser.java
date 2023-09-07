package kr.co.kindernoti.auth.security;

import kr.co.kindernoti.auth.login.ServiceType;

public interface LoginUser<T> {

    T newUser(ServiceType serviceType);
    T joinedUser(ServiceType serviceTypes);
}
