package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.dto.CollectionDTO;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.CollectionService;
import com.universe.backend.utils.PageUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auto_test/collection")
public class CollectionController {
    @Resource
    private CollectionService collectionService;

    @PostMapping("/save")
    public void saveCollection(@RequestBody CollectionDTO collectionDTO, HttpServletRequest httpServletRequest) {
        String user = httpServletRequest.getSession().getAttribute("account").toString();
        collectionDTO.setUpdateUser(user);
        collectionService.saveCollection(collectionDTO);

    }
    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<CollectionDTO>> getCollectionList(@PathVariable int goPage, @PathVariable int pageSize,
                                                          @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, collectionService.getCollectionList(request));
    }

    @GetMapping("/types/{collectionId}")
    public Map<String, Boolean> getCollectionCaseTypes(@PathVariable String collectionId){
        return collectionService.getCollectionCaseTypes(collectionId);
    }

    @GetMapping("/detail/{collectionId}")
    public CollectionDTO getCollectionDetail(@PathVariable String collectionId){
        return collectionService.getCollectionDetail(collectionId);
    }




}
