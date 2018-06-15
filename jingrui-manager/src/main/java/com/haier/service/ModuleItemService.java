package com.haier.service;

import com.haier.base.RespResult;
import com.haier.model.ModuleItemEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ModuleItemService {

    /**
     * 按类别条件分页查询所有
     * @param moduleId
     * @param searchAttr
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    RespResult listAllModuleItems(Long moduleId,String searchAttr,Integer status, Integer pageNo, Integer pageSize);

    RespResult updateModuleItem(ModuleItemEntity updateEntity,MultipartFile thumPic,MultipartFile backGroundPic,MultipartFile detailPic);

    RespResult addModleItem(ModuleItemEntity addEntity,MultipartFile thumPic,MultipartFile backGroundPic,MultipartFile detailPic);

    RespResult delModuleItem(Long id);

    RespResult changeModuleItemStatus(Long id,Integer status);

    RespResult findItemById(Long id);

}
