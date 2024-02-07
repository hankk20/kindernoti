package kr.co.kindernoti.auth.epages;

import org.springframework.http.HttpStatus;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.OperationResponseFactory;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

public class ResponseJwtMockContentModifier implements OperationPreprocessor {
    private final OperationResponseFactory responseFactory = new OperationResponseFactory();
    @Override
    public OperationRequest preprocess(OperationRequest request) {
        return null;
    }

    @Override
    public OperationResponse preprocess(OperationResponse response) {
        return responseFactory.create(HttpStatus.OK, null, "{\"token\":\"jwt\"}".getBytes());
    }
}
