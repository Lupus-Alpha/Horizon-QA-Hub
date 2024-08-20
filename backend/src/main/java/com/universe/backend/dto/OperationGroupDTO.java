package com.universe.backend.dto;

import com.universe.backend.database.domain.Operation;
import lombok.Data;

import java.util.List;

@Data
public class OperationGroupDTO {
    private String id;

    private String name;

    private List<Operation> operationList;
}
