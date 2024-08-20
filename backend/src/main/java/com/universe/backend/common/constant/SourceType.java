package com.universe.backend.common.constant;

public enum SourceType {
    // 用来区分执行数据从哪儿取
    PLAN(3),
    COLLECTION(2),
    CASE(1),
    TEMP(0); // 调试任务 从debug_data临时表取

    private final Integer value;

    SourceType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
