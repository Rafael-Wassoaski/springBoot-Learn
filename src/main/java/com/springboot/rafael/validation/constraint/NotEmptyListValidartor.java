package com.springboot.rafael.validation.constraint;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.springboot.rafael.validation.NotEmptyList;

public class NotEmptyListValidartor implements ConstraintValidator<NotEmptyList, List> {

    @Override
    public void initialize(NotEmptyList constraintAnnotation) {}

    @Override
    public boolean isValid(List value, ConstraintValidatorContext context) {
        // TODO Auto-generated method stub
        if (value.isEmpty()) {
            return false;
        }

        if (value == null) {
            return false;
        }

        return true;
    }

}
