package com.haier.po;

import com.haier.model.ModuleItemEntity;
import lombok.Data;

@Data
public class ModuleItemPo extends ModuleItemEntity {

    private Integer pageSize;

    private Integer pageNo;

    private Integer startRow;

}
