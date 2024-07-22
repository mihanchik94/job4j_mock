package ru.job4j.site.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.job4j.site.service.NotificationService;

@Controller
@AllArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/subscribeCategory/{userId}/{categoryId}")
    public String createSubscribeCategory(@PathVariable("userId")int userId,
                                          @PathVariable("categoryId")int categoryId) {
        notificationService.addSubscribeCategory(userId, categoryId);
        return "redirect:/categories/";
    }

    @GetMapping("/unSubscribeCategory/{userId}/{categoryId}")
    public String deleteSubscribeCategory(@PathVariable("userId")int userId,
                                          @PathVariable("categoryId")int categoryId) {
        notificationService.deleteSubscribeCategory(userId, categoryId);
        return "redirect:/categories/";
    }

    @GetMapping("/subscribeTopic/{userId}/{categoryId}/{topicId}")
    public String createSubscribeTopic(@PathVariable("userId")int userId,
                                       @PathVariable("categoryId")int categoryId,
                                       @PathVariable("topicId")int topicId) {
        notificationService.addSubscribeTopic(userId, topicId);
        return "redirect:/topics/" + categoryId;
    }

    @GetMapping("/unSubscribeTopic/{userId}/{categoryId}/{topicId}")
    public String deleteSubscribeTopic(@PathVariable("userId")int userId,
                                       @PathVariable("categoryId")int categoryId,
                                       @PathVariable("topicId")int topicId) {
        notificationService.deleteSubscribeTopic(userId, topicId);
        return "redirect:/topics/" + categoryId;
    }

    @GetMapping("/subscribeTopicFromDetails/{userId}/{topicId}")
    public String createSubscribeTopicFromDetails(@PathVariable("userId")int userId,
                                                  @PathVariable("topicId")int topicId) {
        notificationService.addSubscribeTopic(userId, topicId);
        return "redirect:/topic/" + topicId;
    }

    @GetMapping("/unSubscribeTopicFromDetails/{userId}/{topicId}")
    public String deleteSubscribeTopicFromDetails(@PathVariable("userId")int userId,
                                                  @PathVariable("topicId")int topicId) {
        notificationService.deleteSubscribeTopic(userId, topicId);
        return "redirect:/topic/" + topicId;
    }
}