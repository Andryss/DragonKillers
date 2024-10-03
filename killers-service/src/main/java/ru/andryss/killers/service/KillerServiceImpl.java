package ru.andryss.killers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.andryss.killers.exception.InternalServerErrorException;
import ru.andryss.killers.model.CreateKillerTeamRequest;
import ru.andryss.killers.model.KillerTeamDto;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Slf4j
@Service
@RequiredArgsConstructor
public class KillerServiceImpl implements KillerService {

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;

    @Value("${dragon-service.url}")
    private String dragonServiceUrl;

    @Override
    public KillerTeamDto createTeam(Integer teamId, String teamName, Integer teamSize, Integer startCaveId) {
        String url = "%s/killers".formatted(dragonServiceUrl);
        CreateKillerTeamRequest request = new CreateKillerTeamRequest()
                .id(teamId).name(teamName).size(teamSize).caveId(startCaveId);

        String xmlRequest = writeAsXml(request);

        log.info("Send dragon service POST with url {} with body {}", url, xmlRequest);
        ResponseEntity<String> responseEntity = httpRequest(POST, url, xmlRequest);

        return readAsXml(responseEntity.getBody(), KillerTeamDto.class);
    }

    @Override
    public KillerTeamDto moveTeam(Integer teamId, Integer caveId) {
        String url = "%s/killers/%s".formatted(dragonServiceUrl, teamId);

        log.info("Send dragon service GET with url {}", url);
        ResponseEntity<String> responseEntity = httpGetRequest(url);

        KillerTeamDto team = readAsXml(responseEntity.getBody(), KillerTeamDto.class);

        CreateKillerTeamRequest request = new CreateKillerTeamRequest()
                .id(team.getId()).name(team.getName()).size(team.getSize()).caveId(caveId);

        String xmlRequest = writeAsXml(request);

        log.info("Send dragon service PUT with url {} with body {}", url, xmlRequest);
        responseEntity = httpRequest(PUT, url, xmlRequest);

        return readAsXml(responseEntity.getBody(),KillerTeamDto.class);
    }

    private ResponseEntity<String> httpGetRequest(String url) {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url, String.class);
        } catch (RestClientException e) {
            log.error("Catch exception while sending http request", e);
            throw new InternalServerErrorException(e.getMessage());
        }

        return responseEntity;
    }

    private ResponseEntity<String> httpRequest(HttpMethod method, String url, String xmlRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> httpEntity = new HttpEntity<>(xmlRequest, headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, method, httpEntity, String.class);
        } catch (RestClientException e) {
            log.error("Catch exception while sending http request", e);
            throw new InternalServerErrorException(e.getMessage());
        }

        return responseEntity;
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

    @SuppressWarnings("SameParameterValue")
    private <T> T readAsXml(String obj, Class<T> clazz) {
        try {
            return xmlMapper.readValue(obj, clazz);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}