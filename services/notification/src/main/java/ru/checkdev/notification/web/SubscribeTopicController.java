package ru.checkdev.notification.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.service.subscribe.SubscribeTopicService;
import java.util.List;

@RestController
@RequestMapping("/subscribeTopic")
@AllArgsConstructor
public class SubscribeTopicController {
    private final SubscribeTopicService service;

    @GetMapping("/{id}")
    public ResponseEntity<List<Integer>> findTopicByUserId(@PathVariable int id) {
        List<Integer> list = service.findTopicByUserId(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}