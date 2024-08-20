package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.TestFile;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.TestFileService;
import com.universe.backend.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/file")
public class FileController {
    @Autowired
    private TestFileService testFileService;

    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public void uploadFile(@RequestParam String name, @RequestParam String projectId, @RequestParam String description, @RequestParam MultipartFile file, HttpServletRequest request){
        testFileService.saveFile(name,projectId,description, file,request);

    }

    @PostMapping("/delete/{fileId}")
    public void deleteFile(@PathVariable String fileId){
        testFileService.deleteFile(fileId);
    }

    @PostMapping("/list/{pageNum}/{pageSize}")
    public PageDTO<List<TestFile>> getTestFileList(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                                   @RequestBody QueryRequest queryRequest){
        Page<Object> page = PageHelper.startPage(pageNum, pageSize, true);
        return PageUtils.setPageInfo(page, testFileService.getTestFileList(queryRequest));
    }

    @GetMapping("/all/{projectId}")
    public List<TestFile> getTestFileList(@PathVariable String projectId){
        return testFileService.getTestFileAll(projectId);
    }




}
