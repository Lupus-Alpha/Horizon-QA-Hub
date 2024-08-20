package com.universe.backend.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.universe.backend.database.domain.Element;
import com.universe.backend.dto.ElementDTO;
import com.universe.backend.dto.PageDTO;
import com.universe.backend.request.QueryRequest;
import com.universe.backend.service.impl.ElementService;
import com.universe.backend.utils.PageUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/auto_test/element")
public class elementController {
    @Resource
    private ElementService elementService;

    @PostMapping("/save")
    public void saveElement(@RequestBody Element element, HttpServletRequest request) {
        String user = request.getSession().getAttribute("account").toString();
        element.setUpdateUser(user);
        elementService.saveElement(element);
    }

    @PostMapping("/delete/{elementId}")
    public void deleteElement(@PathVariable String elementId) {
        elementService.deleteElement(elementId);
    }

    @GetMapping("/list/module/{moduleId}")
    public List<Element> getListModuleElements(@PathVariable String moduleId) {
        return elementService.getListModuleElements(moduleId);
    }

    @PostMapping("/list/{goPage}/{pageSize}")
    public PageDTO<List<ElementDTO>> getElementList(@PathVariable int goPage, @PathVariable int pageSize,
                                                    @RequestBody QueryRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, elementService.getElementList(request));
    }
}
