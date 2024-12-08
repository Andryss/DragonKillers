package ru.andryss.killers.soap.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.andryss.killers.soap.gen.CreateKillerTeamRequest;
import ru.andryss.killers.soap.gen.KillerTeamDto;
import ru.andryss.killers.soap.gen.MoveKillerTeamRequest;

import static ru.andryss.killers.soap.config.WsConfig.NAMESPACE_URI;

@Slf4j
@Endpoint
@RequiredArgsConstructor
public class DragonController {

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createKillerTeamRequest")
    @ResponsePayload
    public KillerTeamDto createKillerTeam(@RequestPayload CreateKillerTeamRequest request) {
        log.info("Create killer team id={} name={} size={} cave={}", request.getTeamId(), request.getTeamName(),
                request.getTeamSize(), request.getStartCaveId());
        return new KillerTeamDto();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "moveKillerTeamRequest")
    @ResponsePayload
    public KillerTeamDto moveKillerTeam(@RequestPayload MoveKillerTeamRequest request) {
        log.info("Move killer team id={} cave={}", request.getTeamId(), request.getCaveId());
        return new KillerTeamDto();
    }
}
