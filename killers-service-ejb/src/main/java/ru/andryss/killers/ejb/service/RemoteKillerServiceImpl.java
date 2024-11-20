package ru.andryss.killers.ejb.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import ru.andryss.killers.exception.BadRequestException;
import ru.andryss.killers.exception.InternalServerErrorException;
import ru.andryss.killers.exception.NotFoundException;
import ru.andryss.killers.model.CreateKillerTeamRequest;
import ru.andryss.killers.model.ErrorObject;
import ru.andryss.killers.model.KillerTeamDto;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Slf4j
@Stateless(name = "RemoteKillerService")
public class RemoteKillerServiceImpl implements RemoteKillerService {

    @Inject
    private XmlMapper xmlMapper;

    @Inject
    private HttpClient httpClient;

    private final String dragonServiceUrl = "http://localhost:8081";

    @Override
    public KillerTeamDto createTeam(Integer teamId, String teamName, Integer teamSize, Integer startCaveId) {
        String url = "%s/killers".formatted(dragonServiceUrl);

        CreateKillerTeamRequest request = new CreateKillerTeamRequest()
                .id(teamId).name(teamName).size(teamSize).caveId(startCaveId);

        String response = httpRequest(POST, url, request);

        return readAsXml(response, KillerTeamDto.class);
    }

    @Override
    public KillerTeamDto moveTeam(Integer teamId, Integer caveId) {
        String url = "%s/killers/%s".formatted(dragonServiceUrl, teamId);

        String response = httpRequest(GET, url, null);

        KillerTeamDto team = readAsXml(response, KillerTeamDto.class);

        CreateKillerTeamRequest request = new CreateKillerTeamRequest()
                .id(team.getId()).name(team.getName()).size(team.getSize()).caveId(caveId);

        response = httpRequest(PUT, url, request);

        return readAsXml(response, KillerTeamDto.class);
    }

    private <T> String httpRequest(HttpMethod method, String url, @Nullable T body) {
        try {
            HttpRequest.BodyPublisher bodyPublisher = (body == null
                    ? HttpRequest.BodyPublishers.noBody()
                    : HttpRequest.BodyPublishers.ofString(writeAsXml(body))
            );
            log.info("Send {} {} with body {}", method, url, body);
            HttpResponse<String> response = httpClient.send(
                    HttpRequest.newBuilder(URI.create(url))
                            .version(HttpClient.Version.HTTP_1_1)
                            .method(method.name(), bodyPublisher)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            int code = response.statusCode();
            String resBody = response.body();
            log.info("Got {} with body {}", code, resBody);
            if (code >= 400) {
                ErrorObject errorObject = readAsXml(resBody, ErrorObject.class);
                if (code == HttpStatus.BAD_REQUEST.value()) {
                    throw new BadRequestException(errorObject.getMessage());
                } else if (code == HttpStatus.NOT_FOUND.value()) {
                    throw new NotFoundException(errorObject.getMessage());
                } else {
                    throw new InternalServerErrorException(errorObject.getMessage());
                }
            }
            return resBody;
        } catch (IOException e) {
            log.error("Catch exception while sending http request", e);
            throw new InternalServerErrorException(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String writeAsXml(Object obj) {
        String xmlRequest;
        try {
            xmlRequest = xmlMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        return xmlRequest;
    }

    private <T> T readAsXml(String obj, Class<T> clazz) {
        try {
            return xmlMapper.readValue(obj, clazz);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
