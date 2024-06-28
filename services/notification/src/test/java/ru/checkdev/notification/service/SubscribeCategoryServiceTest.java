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
import ru.checkdev.notification.service.subscribe.SubscribeCategoryService;
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
public class SubscribeCategoryServiceTest {
    @Autowired
    private SubscribeCategoryService service;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @MockBean
    private TemplateController templateController;

    @Test
    public void whenGetAllSubCatReturnContainsValue() {
        SubscribeCategory subscribeCategory = this.service.save(new SubscribeCategory(0, 1, 1));
        List<SubscribeCategory> result = this.service.findAll();
        assertTrue(result.contains(subscribeCategory));
    }

    @Test
    public void requestByUserIdReturnCorrectValue() {
        SubscribeCategory subscribeCategory = this.service.save(new SubscribeCategory(1, 2, 2));
        List<Integer> result = this.service.findCategoriesByUserId(subscribeCategory.getUserId());
        assertEquals(result, List.of(2));
    }

    @Test
    public void whenDeleteSubCatItIsNotExist() {
        SubscribeCategory subscribeCategory = this.service.save(new SubscribeCategory(2, 3, 3));
        subscribeCategory = this.service.delete(subscribeCategory);
        List<SubscribeCategory> result = this.service.findAll();
        assertFalse(result.contains(subscribeCategory));
    }

    @Test
    public void whenExistByIdThenTrue() {
        SubscribeCategory subscribeCategory = this.service.save(new SubscribeCategory(0, 1, 1));
        assertTrue(this.service.existByUserId(subscribeCategory.getUserId()));
    }

    @Test
    public void whenExistByIdThenFalse() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        assertFalse(this.service.existByUserId(subscribeCategory.getUserId()));
    }

    @Test
    public void whenDeleteByUserIdSuccessfully() {
        int userId = 3;
        this.service.save(new SubscribeCategory(2, userId, 3));
        this.service.save(new SubscribeCategory(4, userId, 4));
        this.service.deleteByUserId(userId);
        List<SubscribeCategory> result = this.service.findAll();
        assertThat(result, is(Collections.emptyList()));
    }

    @Test
    public void whenSaveAllSuccessfully() {
        int userId = 3;
        List<Integer> categoryIds = List.of(1, 2);
        service.saveAll(userId, categoryIds);
        List<SubscribeCategory> result = this.service.findAll();
        assertThat(result, is(List.of(new SubscribeCategory(1, userId, 1), new SubscribeCategory(2, userId, 2))));
    }
}