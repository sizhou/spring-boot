package com.haier.base;

public enum IMRespEnum implements ICDEnum {

    SUCCESS("0", "成功", "成功"),
    EXIST("010100010101", "存在", "存在"),
    NOTEXIST("010100010102", "服务不可用", "服务不可用"),
    FAIL("010100010103", "失败", "失败"),
    SYSTEM_OVERTIME("010100010104", "系统超时", "系统超时"),
    SYSTEM_EXCEPTION("010100010105", "系统繁忙", "系统繁忙"),

    PARAM_ERROR_MoDULE_ID("010100010106", "moduleId参数错误，只为1或2", "参数无效"),
    PARAM_ERROR_MoDULE_STATUS("010100010107", "status参数错误，只为1或2", "参数无效");



    private String code;
    private String desc;
    private String msg;

    private IMRespEnum(String code, String desc, String msg) {
        this.code = code;
        this.desc = desc;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
