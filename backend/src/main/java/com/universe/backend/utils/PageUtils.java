package com.universe.backend.utils;


import com.github.pagehelper.Page;
import com.universe.backend.dto.PageDTO;

public class PageUtils {
    public static <T> PageDTO<T> setPageInfo(Page page, T obj) {
        try {
            PageDTO<T> pager = new PageDTO<>();
            pager.setList(obj);
            pager.setTotal(page.getTotal());
            pager.setPage(page.getPageNum());
            pager.setSize(page.getPageSize());
            return pager;
        } catch (Exception e) {
            throw new RuntimeException("Error saving current page number data！");
        }
    }
}
