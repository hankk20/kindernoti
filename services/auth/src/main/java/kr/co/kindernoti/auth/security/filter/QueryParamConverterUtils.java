package kr.co.kindernoti.auth.security.filter;

import kr.co.kindernoti.auth.login.ServiceType;

import java.util.function.Function;

public class QueryParamConverterUtils{

    public static Function<Object, ServiceType> convertorServiceType() {
         return (param) -> ServiceType.valueOf((String)param);
    }
}
