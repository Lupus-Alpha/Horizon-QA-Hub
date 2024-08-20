package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Api;
import com.universe.backend.request.QueryRequest;

import java.util.List;

public interface ApiMapper {
    void saveApi(Api api);
    Api getApiByName(String id);
    void updateApi(Api api);
    void deleteApi(String id);
    Api getApiById(String id);
    List<Api> getApiList(QueryRequest request);
}
