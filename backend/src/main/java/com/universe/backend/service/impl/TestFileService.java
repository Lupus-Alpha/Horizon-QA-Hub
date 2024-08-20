package com.universe.backend.service.impl;

import com.universe.backend.common.exception.FileException;
import com.universe.backend.database.domain.TestFile;
import com.universe.backend.database.mapper.TestFileMapper;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
public class TestFileService {
    @Resource
    private TestFileMapper testFileMapper;
    public void saveFile(String name,String projectId, String description, MultipartFile file, HttpServletRequest request) {
        TestFile testFile = new TestFile();
        String user = (String) request.getSession().getAttribute("account");
        testFile.setCreateTime(System.currentTimeMillis());
        testFile.setUpdateTime(System.currentTimeMillis());
        testFile.setId(UUID.randomUUID().toString());
        testFile.setName(name);
        testFile.setProjectId(projectId);
        testFile.setDescription(description);
        String file_path = FileUtils.transferFile(file, "test_file", testFile.getName());
        testFile.setFilePath(file_path);
        testFile.setCreateUser(user);
        testFile.setUpdateUser(user);
        testFileMapper.insert(testFile);

    }

    public void deleteFile(String fileId) {
        TestFile testFile = testFileMapper.selectByPrimaryKey(fileId);
        if(testFile == null) {
            throw new FileException("文件不存在");
        }
        FileUtils.delFile(testFile.getFilePath());
        testFileMapper.deleteByPrimaryKey(fileId);
    }

    public List<TestFile> getTestFileList(QueryRequest queryRequest) {
        if(queryRequest.getCondition() != null && !queryRequest.getCondition().equals("")){
            queryRequest.setCondition("%"+queryRequest.getCondition()+"%");
        }
        return testFileMapper.getTestFileList(queryRequest);
    }
    public List<TestFile> getTestFileAll(String projectId) {
        return testFileMapper.getTestFileAll(projectId);
    }
}
