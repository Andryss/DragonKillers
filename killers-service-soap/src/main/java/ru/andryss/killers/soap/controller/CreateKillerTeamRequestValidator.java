package ru.andryss.killers.soap.controller;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.andryss.killers.soap.gen.CreateKillerTeamRequest;

@Component
public class CreateKillerTeamRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CreateKillerTeamRequest.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CreateKillerTeamRequest request = (CreateKillerTeamRequest) target;

        if (request.getTeamId() == 0) {
            errors.rejectValue("teamId", "", "must be not 0");
        }

        if (request.getTeamName() == null || request.getTeamName().isBlank()) {
            errors.rejectValue("teamName", "", "must be not blank");
        }

        if (request.getTeamSize() == 0) {
            errors.rejectValue("teamSize", "", "must be not 0");
        }

        if (request.getStartCaveId() == 0) {
            errors.rejectValue("startCaveId", "", "must be not 0");
        }
    }
}
