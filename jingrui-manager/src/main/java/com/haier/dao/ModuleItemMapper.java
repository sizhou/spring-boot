package com.haier.dao;

import com.haier.model.ModuleItemEntity;
import com.haier.po.ModuleItemPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModuleItemMapper {


    /**
     * 新增
     * @param entity
     * @return
     */
    int insert(@Param("entity") ModuleItemEntity entity);

    /**
     * 根据主键修改信息
     * @param entity
     * @return
     */
    int updateByPrimary(@Param("entity") ModuleItemEntity entity);

    int deleteById(@Param("id")Long id);

    List<ModuleItemEntity> getModuleItemsBy(@Param("po")ModuleItemPo entity);

    int getCountBy(@Param("po")ModuleItemEntity po);

}
