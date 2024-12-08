package ru.andryss.killers.soap.controller;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.SimpleErrors;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.andryss.killers.model.KillerTeamDto;
import ru.andryss.killers.soap.exception.BadRequestException;
import ru.andryss.killers.soap.exception.InternalServerErrorException;
import ru.andryss.killers.soap.exception.NotFoundException;
import ru.andryss.killers.soap.exception.ServiceFault;
import ru.andryss.killers.soap.exception.ServiceFaultException;
import ru.andryss.killers.soap.exception.ServiceUnavailableException;
import ru.andryss.killers.soap.gen.CreateKillerTeamRequest;
import ru.andryss.killers.soap.gen.CreateKillerTeamResponse;
import ru.andryss.killers.soap.gen.MoveKillerTeamRequest;
import ru.andryss.killers.soap.gen.MoveKillerTeamResponse;
import ru.andryss.killers.soap.gen.ObjectFactory;
import ru.andryss.killers.soap.service.KillerService;

import static ru.andryss.killers.soap.config.WsConfig.NAMESPACE_URI;

@Slf4j
@Endpoint
@RequiredArgsConstructor
public class KillerController {

    private final ObjectFactory objectFactory;
    private final KillerService killerService;
    private final CreateKillerTeamRequestValidator createKillerTeamRequestValidator;
    private final MoveKillerTeamRequestValidator moveKillerTeamRequestValidator;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createKillerTeamRequest")
    @ResponsePayload
    public CreateKillerTeamResponse createKillerTeam(@RequestPayload CreateKillerTeamRequest request) {
        SimpleErrors errors = new SimpleErrors(request);
        createKillerTeamRequestValidator.validate(request, errors);
        if (errors.hasFieldErrors()) {
            throw new ServiceFaultException("Validation error", new ServiceFault("400",
                    buildValidationErrorString(errors)));
        }

        int teamId = request.getTeamId();
        String teamName = request.getTeamName();
        int teamSize = request.getTeamSize();
        int startCaveId = request.getStartCaveId();

        log.info("Create killer team id={} name={} size={} cave={}", teamId, teamName, teamSize, startCaveId);

        return wrapException(() -> {
            KillerTeamDto teamDto = killerService.createTeam(teamId, teamName, teamSize, startCaveId);
            CreateKillerTeamResponse response = objectFactory.createCreateKillerTeamResponse();
            response.setId(teamDto.getId());
            response.setName(teamDto.getName());
            response.setSize(teamDto.getSize());
            response.setCaveId(teamDto.getCaveId());
            return response;
        });
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "moveKillerTeamRequest")
    @ResponsePayload
    public MoveKillerTeamResponse moveKillerTeam(@RequestPayload MoveKillerTeamRequest request) {
        SimpleErrors errors = new SimpleErrors(request);
        moveKillerTeamRequestValidator.validate(request, errors);
        if (errors.hasFieldErrors()) {
            throw new ServiceFaultException("Validation error", new ServiceFault("400",
                    buildValidationErrorString(errors)));
        }

        int teamId = request.getTeamId();
        int caveId = request.getCaveId();

        log.info("Move killer team id={} cave={}", teamId, caveId);

        return wrapException(() -> {
            KillerTeamDto teamDto = killerService.moveTeam(teamId, caveId);
            MoveKillerTeamResponse response = objectFactory.createMoveKillerTeamResponse();
            response.setId(teamDto.getId());
            response.setName(teamDto.getName());
            response.setSize(teamDto.getSize());
            response.setCaveId(teamDto.getCaveId());
            return response;
        });
    }

    private static String buildValidationErrorString(SimpleErrors errors) {
        return errors.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
    }

    private static <T> T wrapException(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (BadRequestException e) {
            throw new ServiceFaultException("Execution error", new ServiceFault("400", e.getMessage()));
        } catch (NotFoundException e) {
            throw new ServiceFaultException("Execution error", new ServiceFault("404", e.getMessage()));
        } catch (InternalServerErrorException e) {
            throw new ServiceFaultException("Execution error", new ServiceFault("500", e.getMessage()));
        } catch (ServiceUnavailableException e) {
            throw new ServiceFaultException("Execution error", new ServiceFault("503", e.getMessage()));
        } catch (Exception e) {
            log.error("Unknown unhandled exception", e);
            throw new ServiceFaultException("Execution error", new ServiceFault("500", e.getMessage()));
        }
    }

}
