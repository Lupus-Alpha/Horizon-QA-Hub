package com.universe.backend.common.constant;

public enum TaskType {
    DEBUG("debug"),// 调试任务 从debug_data临时表取
    BATCH("batch");

    private final String value;
    TaskType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
