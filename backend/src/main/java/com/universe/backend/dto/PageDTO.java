package com.universe.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO <T>{
    private T list;
    private long Total;
    private int Page;
    private int Size;

}
