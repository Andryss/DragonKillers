package ru.andryss.killers.config;

import javax.naming.NamingException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import ru.andryss.killers.ejb.service.RemoteKillerService;

@Configuration
public class JNDIConfig {

    @Bean
    public JndiTemplate jndiTemplate() {
        return new JndiTemplate();
    }

    @Bean
    public RemoteKillerService remoteKillerService() throws NamingException {
        String path = "ejb:/%s/%s!%s".formatted(
                "killers-service-ejb-1.0-SNAPSHOT", "RemoteKillerService",
                RemoteKillerService.class.getName()
        );
        return jndiTemplate().lookup(path, RemoteKillerService.class);
    }
}
