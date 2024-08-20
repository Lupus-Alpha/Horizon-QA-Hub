package com.universe.backend.request;

import lombok.Data;

import java.util.List;

@Data
public class UserRequest {
    private String roleId;
    private List<String> userIds;

}
