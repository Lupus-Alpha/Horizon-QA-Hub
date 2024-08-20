package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.TestFile;
import com.universe.backend.request.QueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestFileMapper {
    int deleteByPrimaryKey(String id);

    int insert(TestFile record);

    int insertSelective(TestFile record);

    TestFile selectByPrimaryKey(String id);

    List<TestFile> getTestFileList(QueryRequest request);

    List<TestFile> getTestFileAll(String projectId);


}
