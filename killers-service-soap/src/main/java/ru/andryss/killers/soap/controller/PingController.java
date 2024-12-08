package ru.andryss.killers.soap.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.andryss.killers.soap.gen.PingRequest;
import ru.andryss.killers.soap.gen.PingResponse;

import static ru.andryss.killers.soap.config.WsConfig.NAMESPACE_URI;

@Slf4j
@Endpoint
public class PingController {
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "pingRequest")
    @ResponsePayload
    public PingResponse ping(@RequestPayload PingRequest request) {
        log.info("Ping-pong");
        PingResponse response = new PingResponse();
        response.setMessage(request.getMessage());
        return response;
    }
}
