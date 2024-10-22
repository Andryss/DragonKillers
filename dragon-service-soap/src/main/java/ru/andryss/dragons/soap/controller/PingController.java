package ru.andryss.dragons.soap.controller;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.andryss.dragons.soap.gen.PingRequest;
import ru.andryss.dragons.soap.gen.PingResponse;

import static ru.andryss.dragons.soap.config.WsConfig.NAMESPACE_URI;

@Endpoint
public class PingController {
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "pingRequest")
    @ResponsePayload
    public PingResponse ping(@RequestPayload PingRequest request) {
        PingResponse response = new PingResponse();
        response.setMessage(request.getMessage());
        return response;
    }
}
