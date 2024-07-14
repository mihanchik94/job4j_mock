package ru.checkdev.desc.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.checkdev.desc.dto.TopicDTO;
import ru.checkdev.desc.service.TopicService;

import java.util.List;

@RestController
@RequestMapping("/topic")
@AllArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/")
    public ResponseEntity<List<TopicDTO>> findAll() {
        List<TopicDTO> topics = topicService.getAllTopicDTOs();
        if (topics.isEmpty()) {
            return new ResponseEntity<>(topics, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(topics, HttpStatus.FOUND);
    }
}