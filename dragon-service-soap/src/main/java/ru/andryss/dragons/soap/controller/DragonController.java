package ru.andryss.dragons.soap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.andryss.dragons.soap.gen.DragonDto;
import ru.andryss.dragons.soap.gen.GetDragonRequest;
import ru.andryss.dragons.soap.gen.GetDragonResponse;
import ru.andryss.dragons.soap.service.DragonsService;

import static ru.andryss.dragons.soap.config.WsConfig.NAMESPACE_URI;

@Endpoint
@RequiredArgsConstructor
public class DragonController {

    private final DragonsService dragonsService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDragonRequest")
    @ResponsePayload
    public GetDragonResponse getDragon(@RequestPayload GetDragonRequest request) {
        DragonDto dragonDto = dragonsService.getDragonById(request.getId());
        GetDragonResponse response = new GetDragonResponse();
        response.setDragon(dragonDto);
        return response;
    }
}
