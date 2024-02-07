package kr.co.kindernoti.auth.epages;

import org.springframework.restdocs.constraints.Constraint;
import org.springframework.restdocs.constraints.ValidatorConstraintResolver;

import java.util.List;

public class ValidatorConstraintResolverExtends extends ValidatorConstraintResolver {
    private final String JAVAX = "javax";
    private final String JAKARTA = "jakarta";

    @Override
    public List<Constraint> resolveForProperty(String property, Class<?> clazz) {
        List<Constraint> constraints = super.resolveForProperty(property, clazz);
        return constraints.stream()
                .map(c -> new Constraint(c.getName().replace(JAKARTA, JAVAX), c.getConfiguration()))
                .toList();
    }
}
