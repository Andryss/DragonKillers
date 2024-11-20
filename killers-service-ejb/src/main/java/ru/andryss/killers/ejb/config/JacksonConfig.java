package ru.andryss.killers.ejb.config;

import java.net.http.HttpClient;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class JacksonConfig {

    @Produces
    public XmlMapper xmlMapper() {
        return XmlMapper.builder()
                .defaultUseWrapper(false)
                .addModule(new JavaTimeModule())
                .build();
    }

    @Produces
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

}
