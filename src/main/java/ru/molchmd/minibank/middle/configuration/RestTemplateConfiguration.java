package ru.molchmd.minibank.middle.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate(@Value("${client.urls.host}") String host) {
        return new RestTemplateBuilder()
                .rootUri(host)
                .setConnectTimeout(Duration.ofSeconds(9))
                .setReadTimeout(Duration.ofSeconds(9))
                .errorHandler(new ResponseNoErrorHandler())
                .build();
    }

    private static class ResponseNoErrorHandler extends DefaultResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse response) {
            return false;
        }
    }
}
