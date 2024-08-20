package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Collection;
import com.universe.backend.dto.CollectionDTO;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectionMapper {
    void saveCollection(Collection collection);

    void updateCollection(Collection collection);

    List<CollectionDTO> getCollectionList(QueryRequest request);

    CollectionDTO getCollectionDetail(String collectionId);
}
