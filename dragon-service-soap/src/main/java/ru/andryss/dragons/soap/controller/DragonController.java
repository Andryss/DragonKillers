package ru.andryss.dragons.soap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.andryss.dragons.soap.gen.CountDragonsByColorRequest;
import ru.andryss.dragons.soap.gen.CountDragonsByColorResponse;
import ru.andryss.dragons.soap.service.DragonsService;

import static ru.andryss.dragons.soap.config.WsConfig.NAMESPACE_URI;

@Endpoint
@RequiredArgsConstructor
public class DragonController {

    private final DragonsService dragonsService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "countDragonsByColorRequest")
    @ResponsePayload
    public CountDragonsByColorResponse countDragonsByColor(@RequestPayload CountDragonsByColorRequest request) {
        Integer count = dragonsService.countByColor(request.getColor());
        CountDragonsByColorResponse response = new CountDragonsByColorResponse();
        response.setCount(count);
        return response;
    }
}
