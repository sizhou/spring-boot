package com.haier.controller.po;

import com.haier.model.ModuleItemEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModuleItemListResp {

    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数")
    private Integer total;

    /**
     * 页码.
     */
    @ApiModelProperty(value = "页码")
    private Integer pageNo;
    /**
     * 每页长度.
     */
    @ApiModelProperty(value = "每页长度")
    private Integer pageSize;

    @ApiModelProperty(value = "模块列表")
    private List<ModuleItemEntity> list;

}
