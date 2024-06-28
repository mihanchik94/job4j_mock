package ru.checkdev.desc.mapper.topic;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.checkdev.desc.domain.Topic;
import ru.checkdev.desc.dto.TopicDTO;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TopicMapper {
    @Mapping(source = "category.id", target = "categoryId")
    TopicDTO toDTO(Topic topic);

    List<TopicDTO> toDTOList(List<Topic> topics);
}