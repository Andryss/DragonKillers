package ru.andryss.killers.soap.controller;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.andryss.killers.soap.gen.MoveKillerTeamRequest;

@Component
public class MoveKillerTeamRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MoveKillerTeamRequest.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        MoveKillerTeamRequest request = (MoveKillerTeamRequest) target;

        if (request.getTeamId() == 0) {
            errors.rejectValue("teamId", "", "must be not 0");
        }

        if (request.getCaveId() == 0) {
            errors.rejectValue("caveId", "", "must be not 0");
        }
    }
}
