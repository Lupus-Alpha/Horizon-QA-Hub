package com.universe.backend.common.constant;

public enum MenuEnum {
    BASE_CENTER(100, "公共组件", "icon-gonggongzujian", null, null, null),

    FILE_MANAGE(110, "文件管理", "icon-wenjianguanli", 100, "/baseCenter/fileManage", PermissionEnum.NORMAL_MENU.id),

    PARAM_MANAGE(120, "参数管理", "icon-gonggongcanshu", 100, "/baseCenter/paramManage", PermissionEnum.NORMAL_MENU.id),

    FUNCTION_MANAGE(130, "函数管理", "icon-hanshuguanli", 100, "/baseCenter/functionManage", PermissionEnum.NORMAL_MENU.id),

    OPERATION_MANAGE(140, "操作管理", "icon-caozuoguanli", 100, "/baseCenter/operationManage", PermissionEnum.NORMAL_MENU.id),

    ENV_CENTER(200, "环境中心", "icon-huanjingzhongxin", null, null, null),

    ENV_MANAGE(210, "环境管理", "icon-huanjingguanli", 200, "/envCenter/envManage", PermissionEnum.NORMAL_MENU.id),

    ENGINE_MANAGE(220, "引擎管理", "icon-yinqinguanli", 200, "/envCenter/engineManage", PermissionEnum.NORMAL_MENU.id),

    DEVICE_MANAGE(230, "设备管理", "icon-shebeiguanli", 200, "/envCenter/deviceManage", PermissionEnum.NORMAL_MENU.id),

    CASE_CENTER(300,"测试用例", "icon-yonglizhongxin", null, null, null),

    API(311,"接口管理", "icon-jiekouguanli", 300, "/caseCenter/apiManage", PermissionEnum.NORMAL_MENU.id),

    ELEMENT(321,"元素管理", "icon-yuansuguanli", 300, "/caseCenter/elementManage", PermissionEnum.NORMAL_MENU.id),

    CONTROL(331,"控件管理", "icon-kongjianguanli", 300, "/caseCenter/controlManage", PermissionEnum.NORMAL_MENU.id),

    CASE(341,"用例管理", "icon-yongliguanli", 300, "/caseCenter/caseManage", PermissionEnum.NORMAL_MENU.id),

    TEST(400,"计划管理", "icon-zhixingguanli", null, null, null),

    COLLECTION(411,"测试集合", "icon-ceshijihe", 400, "/planManage/testCollection", PermissionEnum.NORMAL_MENU.id),

    PLAN(421,"测试计划", "icon-ceshijihua", 400, "/planManage/testPlan", PermissionEnum.NORMAL_MENU.id),

    RESULT(500,"报告中心", "icon-ceshizhuizong", null, null, null),

    REPORT(511,"测试报告", "icon-ceshibaogao", 500, "/report/testReport", PermissionEnum.NORMAL_MENU.id),

    SYSTEM_MANAGE(900, "系统管理", "icon-xitongguanli", null, null, null),

    USER_MANAGE(910, "用户管理", "icon-yonghuguanli", 900, "/system/userManage", PermissionEnum.USER_MENU.id),

    ROLE_MANAGE(920, "角色管理", "icon-jiaoseguanli", 900, "/system/roleManage", PermissionEnum.ROLE_MENU.id),

    PROJECT_MANAGE(930, "项目管理", "icon-xiangmuguanli", 900, "/system/projectManage", PermissionEnum.PROJECT_MENU.id);


    public Integer id;
    public String name;
    public String icon;
    public Integer parentId;
    public String path;
    public String permissionId;

    MenuEnum(Integer id, String name, String icon, Integer parentId, String path, String permissionId) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.parentId = parentId;
        this.path = path;
        this.permissionId = permissionId;
    }

}
