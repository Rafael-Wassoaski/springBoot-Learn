package com.springboot.rafael.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import com.springboot.rafael.validation.constraint.NotEmptyListValidartor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotEmptyListValidartor.class)
public @interface NotEmptyList {
    String message() default "A lista não pode ser vazia";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
