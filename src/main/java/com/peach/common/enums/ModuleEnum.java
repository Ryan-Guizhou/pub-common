package com.peach.common.enums;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 06 3月 2025 22:02
 */
public enum ModuleEnum {

    SCHEDULE("SCHEDULE", "定时任务模块"),
    SECURITY("SECURITY","认证模块"),
    FILSERVICE("FILESERVICE","文件服务模块")
    ;

    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 模块名称
     */
    private String moduleName;

    ModuleEnum(String moduleCode, String moduleName) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

}
