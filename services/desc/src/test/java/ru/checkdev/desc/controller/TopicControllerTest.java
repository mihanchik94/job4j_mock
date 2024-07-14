package ru.checkdev.desc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.checkdev.desc.dto.TopicDTO;
import ru.checkdev.desc.service.TopicService;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TopicControllerTest {
    @Mock
    public TopicService topicService;
    @InjectMocks
    public TopicController topicController;
    public TopicDTO topicDTO;

    @BeforeEach
    void setUp() {
        topicDTO = new TopicDTO();
    }

    @Test
    void whenFindAllTopicDTOsByCategoryIdThenGetResponseEntityWithTopicDTOs() {
        when(topicService.getAllTopicDTOs()).thenReturn(List.of(topicDTO));
        assertThat(topicController.findAll()).isEqualTo(
                new ResponseEntity<>(List.of(topicDTO), HttpStatus.FOUND)
        );
    }

    @Test
    void whenFindAllTopicDTOsByCategoryIdAndEmptyListThenHttpStatusNotFound() {
        when(topicService.getAllTopicDTOs()).thenReturn(emptyList());
        assertThat(topicController.findAll()).isEqualTo(
                new ResponseEntity<>(emptyList(), HttpStatus.NOT_FOUND)
        );
    }
}