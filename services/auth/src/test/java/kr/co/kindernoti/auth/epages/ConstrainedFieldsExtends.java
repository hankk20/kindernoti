package kr.co.kindernoti.auth.epages;

import org.apache.commons.lang3.StringUtils;
import org.springframework.restdocs.constraints.ValidatorConstraintResolver;
import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public class ConstrainedFieldsExtends {

    private final ValidatorConstraintResolver validatorConstraintResolver = new ValidatorConstraintResolverExtends();
    private final Class<?> classHoldingConstraints;
    private final String CONSTRAINTS_KEY = "validationConstraints";
    private final String DOT_NOTATION_DELIMITER = ".";
    public ConstrainedFieldsExtends(Class<?> classHoldingConstraints) {
        this.classHoldingConstraints = classHoldingConstraints;
    }

    public FieldDescriptor withPath(String path) {
        return withMappedPath(path, beanPropertyNameFromPath(path));
    }

    public FieldDescriptor withMappedPath(String jsonPath, String beanPropertyName) {
        return addConstraints(fieldWithPath(jsonPath), beanPropertyName);
    }

    public FieldDescriptor addConstraints(FieldDescriptor fieldDescriptor, String beanPropertyName) {
        fieldDescriptor.attributes(key(CONSTRAINTS_KEY)
                .value(this.validatorConstraintResolver.resolveForProperty(beanPropertyName, classHoldingConstraints))
        );
        return fieldDescriptor;
    }

    public String beanPropertyNameFromPath(String jsonPath) {
        return StringUtils.defaultIfEmpty(StringUtils.substringAfterLast(jsonPath, DOT_NOTATION_DELIMITER), jsonPath);
    }

}
