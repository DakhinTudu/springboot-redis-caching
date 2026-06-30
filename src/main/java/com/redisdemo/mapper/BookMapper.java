package com.redisdemo.mapper;

import com.redisdemo.dto.BookRequest;
import com.redisdemo.dto.BookResponse;
import com.redisdemo.dto.BookUpdateRequest;
import com.redisdemo.entity.Books;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, unmappedSourcePolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {

    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Books   toEntity(BookRequest request);

    BookResponse toResponse(Books entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(BookUpdateRequest request, @MappingTarget Books entity);

    List<BookResponse> toResponseList(List<Books> entities);

    List<Books> toEntityList(List<BookRequest> requests);
}
