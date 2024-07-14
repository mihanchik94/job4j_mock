package ru.checkdev.desc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.checkdev.desc.domain.Topic;
import ru.checkdev.desc.dto.TopicDTO;
import ru.checkdev.desc.mapper.topic.TopicMapper;
import ru.checkdev.desc.repository.TopicRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {
    private static final int TOPIC_ID = 1;
    private static final int CATEGORY_ID = 1;
    private static final String TOPIC_NAME = "name";

    @Mock
    public TopicRepository topicRepository;
    @Mock
    public TopicMapper topicMapper;
    @InjectMocks
    public TopicService topicService;
    public Topic topic;
    public TopicDTO topicDTO;

    @BeforeEach
    void setUp() {
        topic = new Topic();
        topic.setId(TOPIC_ID);
        topic.setName(TOPIC_NAME);
        topicDTO = new TopicDTO();
        topicDTO.setId(topic.getId());
        topicDTO.setCategoryId(CATEGORY_ID);
        topicDTO.setName(topic.getName());
    }

    @Test
    void whenFindByIdAndIdNotExistThenGetEmptyOptional() {
        when(topicRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThat(topicService.findById(TOPIC_ID)).isEqualTo(Optional.empty());
    }

    @Test
    void whenFindByIdAndThenGetOptionalOfTopic() {
        when(topicRepository.findById(anyInt())).thenReturn(Optional.of(topic));
        assertThat(topicService.findById(TOPIC_ID)).isEqualTo(Optional.of(topic));
    }

    @Test
    void whenCreateThenReturnTopic() {
        when(topicRepository.save(topic)).thenReturn(topic);
        assertThat(topicService.create(topic)).isEqualTo(topic);
    }

    @Test
    void whenFindByCategoryAndTopicsNotExistThenReturnEmptyList() {
        when(topicRepository.findByCategoryIdOrderByPositionAsc(anyInt())).thenReturn(emptyList());
        assertThat(topicService.findByCategory(CATEGORY_ID)).isEmpty();
    }

    @Test
    void whenFindByCategoryThenReturnListOfTopics() {
        when(topicRepository.findByCategoryIdOrderByPositionAsc(anyInt())).thenReturn(List.of(topic));
        assertThat(topicService.findByCategory(CATEGORY_ID)).isEqualTo(List.of(topic));
    }

    @Test
    void whenGetAllThenReturnListOfTopics() {
        when(topicRepository.findAllByOrderByPositionAsc()).thenReturn(List.of(topic));
        assertThat(topicService.getAll()).isEqualTo(List.of(topic));
    }

    @Test
    void whenGetNameByIdAndIdNotExistThenGetEmptyOptional() {
        when(topicRepository.getNameById(anyInt())).thenReturn(Optional.empty());
        assertThat(topicService.getNameById(TOPIC_ID)).isEmpty();
    }

    @Test
    void whenGetNameByIdThenGetOptionalOfName() {
        when(topicRepository.getNameById(anyInt())).thenReturn(Optional.of(TOPIC_NAME));
        assertThat(topicService.getNameById(TOPIC_ID)).isEqualTo(Optional.of(TOPIC_NAME));
    }

    @Test
    void whenGetAllTopicDTOsThenReturnListOfTopicDTOs() {
        when(topicRepository.findAllByOrderByPositionAsc()).thenReturn(List.of(topic));
        when(topicMapper.toDTOList(List.of(topic))).thenReturn(List.of(topicDTO));
        assertThat(topicService.getAllTopicDTOs()).isEqualTo(List.of(topicDTO));
    }

    @Test
    void  whenGetAllTopicDTOsThenReturnEmptyList() {
        assertThat(topicService.getAllTopicDTOs()).isEqualTo(emptyList());
    }
}