package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.site.dto.CategoryDTO;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.ProfileDTO;
import ru.job4j.site.dto.TopicIdNameDTO;
import ru.job4j.site.service.*;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@AllArgsConstructor
@Slf4j
public class IndexController {
    private final CategoriesService categoriesService;
    private final InterviewsService interviewsService;
    private final AuthService authService;
    private final NotificationService notifications;
    private final ProfilesService profilesService;
    private final TopicsService topicsService;

    @GetMapping({"/", "index"})
    public String getIndexPage(Model model, HttpServletRequest req) throws JsonProcessingException {
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/");
        try {
            model.addAttribute("categories", categoriesService.getMostPopular());
            var token = getToken(req);
            if (token != null) {
                var userInfo = authService.userInfo(token);
                model.addAttribute("userInfo", userInfo);
                model.addAttribute("userDTO", notifications.findCategoriesByUserId(userInfo.getId()));
                RequestResponseTools.addAttrCanManage(model, userInfo);
            }
        } catch (Exception e) {
            log.error("Remote application not responding. Error: {}. {}, ", e.getCause(), e.getMessage());
        }
        List<InterviewDTO> interviewDTOs = interviewsService.getByType(1);
        Set<ProfileDTO> profiles= interviewDTOs.stream()
                .map(interviewDTO -> profilesService.getProfileById(interviewDTO.getSubmitterId()))
                .map(Optional::get)
                .collect(Collectors.toSet());
        List<CategoryDTO> categories = categoriesService.getAll();
        Map<Integer, Integer> interviewsCount = new HashMap<>();
        for (CategoryDTO category : categories) {
            List<Integer> topicDtoIds = topicsService.getTopicIdNameDtoByCategory(category.getId())
                    .stream().map(TopicIdNameDTO::getId).toList();
            int interviewCount = (int) interviewsService
                    .getByTopicsIds(topicDtoIds, 0, 0)
                    .stream()
                    .filter(interviewDTO -> interviewDTO.getStatus() == 1)
                    .count();
            interviewsCount.put(category.getId(), interviewCount);
        }
        model.addAttribute("new_interviews", interviewDTOs);
        model.addAttribute("profiles", profiles);
        model.addAttribute("interviewsCount", interviewsCount);
        return "index";
    }
}