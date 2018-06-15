package com.haier.base;

public enum ModuleItemRespEnum implements ICDEnum {

    SUCCESS("0", "成功", "成功"),
    EXIST("010100010101", "存在", "存在"),
    NOTEXIST("010100010102", "服务不可用", "服务不可用"),
    FAIL("010100010103", "失败", "失败"),
    SYSTEM_OVERTIME("010100010104", "系统超时", "系统超时"),

    THUM_UPLOAD_FAILD("010100010105", "上传缩略图失败", "操作失败"),
    BACK_UPLOAD_FAILD("010100010105", "上传背景图失败", "操作失败"),
    DETAIL_UPLOAD_FAILD("010100010105", "上传详情图失败", "操作失败"),




    TOKE_SUCCESS("010100010106", "票据校验成功", "票据校验成功"),
    TOKE_ILLE_FAIL("010100010107", "票据非法", "票据非法");

    private String code;
    private String desc;
    private String msg;

    private ModuleItemRespEnum(String code, String desc, String msg) {
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
