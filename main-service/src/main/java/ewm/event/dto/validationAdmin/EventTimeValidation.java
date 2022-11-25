package ewm.event.dto.validationAdmin;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
public @interface EventTimeValidation {
    String message() default "{end} должен быть позже, чем {start}";

    String start();

    String end();

    String typeValidate();

    Class[] groups() default {};

    Class[] payload() default {};
}

