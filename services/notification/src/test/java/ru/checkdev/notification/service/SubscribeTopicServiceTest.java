package ru.checkdev.notification.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.SubscribeCategory;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.dto.CategoryDTO;
import ru.checkdev.notification.dto.TopicDTO;
import ru.checkdev.notification.service.subscribe.SubscribeTopicService;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.web.TemplateController;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = NtfSrv.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SubscribeTopicServiceTest {
    @Autowired
    private SubscribeTopicService service;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenGetAllSubTopicReturnContainsValue() {
        SubscribeTopic subscribeTopic = this.service.save(new SubscribeTopic(0, 1, 1));
        List<SubscribeTopic> result = this.service.findAll();
        assertTrue(result.contains(subscribeTopic));
    }

    @Test
    public void requestByUserIdReturnCorrectValue() {
        SubscribeTopic subscribeTopic = this.service.save(new SubscribeTopic(1, 2, 2));
        List<Integer> result = this.service.findTopicByUserId(subscribeTopic.getUserId());
        assertEquals(result, List.of(2));
    }

    @Test
    public void whenDeleteTopicCatItIsNotExist() {
        SubscribeTopic subscribeTopic = this.service.save(new SubscribeTopic(2, 3, 3));
        subscribeTopic = this.service.delete(subscribeTopic);
        List<SubscribeTopic> result = this.service.findAll();
        assertFalse(result.contains(subscribeTopic));
    }

    @Test
    public void whenExistByIdThenTrue() {
        SubscribeTopic subscribeTopic = this.service.save(new SubscribeTopic(0, 1, 1));
        assertTrue(this.service.existByUserId(subscribeTopic.getUserId()));
    }

    @Test
    public void whenExistByIdThenFalse() {
        SubscribeTopic subscribeTopic = new SubscribeTopic(0, 1, 1);
        assertFalse(this.service.existByUserId(subscribeTopic.getUserId()));
    }

    @Test
    public void whenDeleteByUserIdSuccessfully() {
        int userId = 3;
        this.service.save(new SubscribeTopic(2, userId, 3));
        this.service.save(new SubscribeTopic(4, userId, 4));
        this.service.deleteByUserId(userId);
        List<SubscribeTopic> result = this.service.findAll();
        assertThat(result, is(Collections.emptyList()));
    }

    @Test
    public void whenSubscribeSuccessfully() {
        int userId = 3;
        List<TopicDTO> topics = List.of(new TopicDTO(1, "first", 1), new TopicDTO(2, "second", 1));
        service.subscribe(userId, topics);
        List<SubscribeTopic> result = this.service.findAll();
        assertThat(result, is(List.of(new SubscribeTopic(1, userId, 1), new SubscribeTopic(2, userId, 2))));
    }
}