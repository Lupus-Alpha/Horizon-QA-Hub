package com.universe.backend.common.constant;

public enum ReportStatus {
    PREPARED("prepared"),   // 等待执行
    RUNNING("running"),     // 执行中
    TIMEOUT("timeout"),     // 超时
    DISCONTINUE("discontinue"),    // 手动终止
    COMPLETED("completed"),    // 完成
    SUCCESS("success"),    // 全部用例成功 则为成功
    FAIL("fail"),   // 只有断言失败 则为失败
    ERROR("error");     // 网络请求等错误 则为错误

    private final String value;
    ReportStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
