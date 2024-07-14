package ru.checkdev.notification.telegram.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.checkdev.notification.dto.CategoryDTO;
import ru.checkdev.notification.dto.TopicDTO;

@Service
@Slf4j
public class TgDeskCallWebClint {
    private WebClient webClient;

    public TgDeskCallWebClint(@Value("${server.desk}") String urlDesk) {
        this.webClient = WebClient.create(urlDesk);
    }

    public Flux<CategoryDTO> getAllCategories(String url) {
        return webClient.
                get()
                .uri(url)
                .retrieve()
                .bodyToFlux(CategoryDTO.class)
                .doOnError(err -> log.error("API not found: {}", err.getMessage()));
    }

    public Flux<TopicDTO> getAllTopics(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(TopicDTO.class)
                .doOnError(err -> log.error("API not found: {}", err.getMessage()));

    }

/*    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }*/
}