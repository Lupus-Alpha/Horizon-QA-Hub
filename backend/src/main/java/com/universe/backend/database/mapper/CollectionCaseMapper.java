package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.CollectionCase;
import com.universe.backend.dto.CollectionCaseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CollectionCaseMapper {
    void saveCollectionCase(ArrayList<CollectionCase> collectionCases);

    void deleteCollectionCase(String collectionId);

    List<String> getCollectionCaseTypes(String collectionId);

    List<CollectionCaseDTO> getCollectionCaseList(String collectionId);
}
