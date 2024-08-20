package com.universe.backend.dto;

import com.universe.backend.database.domain.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDTo extends User implements Serializable {
    private List<String> permission;
}
