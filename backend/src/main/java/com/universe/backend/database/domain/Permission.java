package com.universe.backend.database.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Permission implements Serializable {
    private String id;

    private String name;

}
