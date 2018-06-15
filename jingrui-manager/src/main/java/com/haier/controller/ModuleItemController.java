package com.haier.controller;


import com.haier.base.IMRespEnum;
import com.haier.base.RespResult;
import com.haier.controller.po.ModuleItemListResp;
import com.haier.controller.po.ModuleItemReq;
import com.haier.model.ModuleItemEntity;
import com.haier.service.ModuleItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.Objects;

@Api(tags = "模块管理")
@RestController
@RequestMapping(value = "module")
public class ModuleItemController {

@Autowired
private ModuleItemService moduleItemService;

    @ApiOperation(value = "获取模块列表", httpMethod = "GET", notes = "获取模块列表")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public RespResult<ModuleItemListResp> list(
            @ApiParam(value = "模块类型，1：大事记，2：价目表", required = true) @RequestParam("moduleId") Long moduleId,
            @ApiParam(value = "模糊查询搜索条件", required = false) @RequestParam(name = "searchAttr", required = false) String searchAttr,
            @ApiParam(value = "模块状态", required = false) @RequestParam(name = "status", required = false) Integer status,
            @ApiParam(value = "页码", required = true) @RequestParam(name = "pageNo") Integer pageNo,
            @ApiParam(value = "每页长度", required = true) @RequestParam(name = "pageSize") Integer pageSize) {
        RespResult respResult = new RespResult();
            if (null == pageNo || pageNo <= 0) {
                pageNo = 1;
            }
            if (null == pageSize || pageSize <= 0) {
                pageSize = 10;
            }
            if (moduleId==1 || moduleId==2) {
                if(status == null || status.equals(1) || status.equals(2)){
                    respResult = moduleItemService.listAllModuleItems(moduleId,searchAttr,status,pageNo,pageSize);
                }else{
                    respResult.setMsg(IMRespEnum.PARAM_ERROR_MoDULE_STATUS.getMsg());
                    respResult.setCode(IMRespEnum.PARAM_ERROR_MoDULE_STATUS.getCode());
                }
            }else{
                respResult.setMsg(IMRespEnum.PARAM_ERROR_MoDULE_ID.getMsg());
                respResult.setCode(IMRespEnum.PARAM_ERROR_MoDULE_ID.getCode());
            }
        return respResult;
    }



    @ApiOperation(value = "添加模块信息", httpMethod = "POST", notes = "添加模块信息")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @RequestMapping(value = "/add", method = RequestMethod.POST,produces = "application/json")
    public RespResult addModuleItem(
            @ApiParam(value = "模块类型，1：大事记，2：价目表", required = true) @RequestParam(required=true,name="moduleId") Long moduleId,
            @ApiParam(value = "模块名称（标题）", required = true) @RequestParam(required=true,name="itemName") String itemName,
            @ApiParam(value = "内容类型，1：图片内容，2.文字内容", required = false, defaultValue = "1") @RequestParam(required=true,name="contentType")Integer contentType,
            @ApiParam(value = "摘要", required = false) @RequestParam(required=false,name="headlines")String headlines,
            @ApiParam(value = "模块详情描述", required = false) @RequestParam(required=false,name="detailDesc")String detailDesc,
            @ApiParam(value = "模块展示顺序，从100-0倒顺", required = false,defaultValue = "1") @RequestParam(required=false,name="order")Integer order,
            @ApiParam(value = "缩略图", required = false)@RequestParam(required=false,name="thumPic") MultipartFile thumPic,
            @ApiParam(value = "背景图", required = false)@RequestParam(required=false,name="backGroundPic") MultipartFile backGroundPic,
            @ApiParam(value = "详情图", required = false)@RequestParam(required=false,name="detailPic") MultipartFile detailPic
            ) {
        RespResult respResult = new RespResult();
        ModuleItemEntity addEn = new ModuleItemEntity();
        addEn.setModuleId(moduleId);
        addEn.setItemName(itemName);
        addEn.setContentType(contentType);
        addEn.setDetailDesc(detailDesc);
        addEn.setModuleOrder(order);
        addEn.setHeadlines(headlines);
        if (Objects.nonNull(addEn)){
            return moduleItemService.addModleItem(addEn,thumPic,backGroundPic,detailPic);
        }
        return respResult;
    }

    @ApiOperation(value = "修改模块信息", httpMethod = "POST", notes = "修改模块信息")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @RequestMapping(value = "/upd", method = RequestMethod.POST)
    public RespResult updModuleItem(
                                    @RequestParam(required=true,name="id") Long id,
                                    @ApiParam(value = "模块类型，1：大事记，2：价目表", required = true)@RequestParam(required=false,name="moduleId") Long moduleId,
                                    @ApiParam(value = "模块名称（标题）", required = true)@RequestParam(required=false,name="itemName") String itemName,
                                    @ApiParam(value = "内容类型，1：图片内容，2.文字内容", required = false, defaultValue = "1")@RequestParam(required=false,name="contentType")Integer contentType,
                                    @ApiParam(value = "摘要", required = false) @RequestParam(required=false,name="headlines")String headlines,
                                    @ApiParam(value = "模块详情描述", required = false) @RequestParam(required=false,name="detailDesc")String detailDesc,
                                    @ApiParam(value = "模块展示顺序，从100-0倒顺", required = false,defaultValue = "1")@RequestParam(required=false,name="order")Integer order,
                                    @ApiParam(value = "缩略图", required = false)@RequestParam(required=false,name="thumPic") MultipartFile thumPic,
                                    @ApiParam(value = "背景图", required = false)@RequestParam(required=false,name="backGroundPic") MultipartFile backGroundPic,
                                    @ApiParam(value = "详情图", required = false)@RequestParam(required=false,name="detailPic") MultipartFile detailPic) {
        RespResult respResult = new RespResult();
        ModuleItemEntity updEn = new ModuleItemEntity();
        updEn.setId(id);
        updEn.setModuleId(moduleId);
        updEn.setItemName(itemName);
        updEn.setContentType(contentType);
        updEn.setDetailDesc(detailDesc);
        updEn.setModuleOrder(order);
        updEn.setHeadlines(headlines);
        if (Objects.nonNull(updEn)){
            return moduleItemService.updateModuleItem(updEn,thumPic,backGroundPic,detailPic);
        }
        return respResult;
    }


//    @ApiOperation(value = "删除模块信息", httpMethod = "DELETE", notes = "删除模块信息")
//    @ApiResponse(code = 200, message = "success", response = RespResult.class)
//    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
//    public RespResult delModuleItem(@RequestParam Long id ) {
//        RespResult respResult = new RespResult();
//        if (Objects.nonNull(id)){
//            respResult = moduleItemService.delModuleItem(id);
//        }
//        return respResult;
//    }

    @ApiOperation(value = "模块上下线", httpMethod = "POST", notes = "模块上下线")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public RespResult changeStatus(@ApiParam(value = "id", required = true)@RequestParam(name = "id", required = true) Long id ,
                                   @ApiParam(value = "状态，1：上线，2：下线", required = true)@RequestParam(name = "status",required = true)Integer status) {
        RespResult respResult = new RespResult();
        if (Objects.nonNull(id) && id > 0){
            if(status.equals(1) || status.equals(2)){
                respResult = moduleItemService.changeModuleItemStatus(id,status);
            }else{
                respResult.setMsg(IMRespEnum.PARAM_ERROR_MoDULE_STATUS.getMsg());
                respResult.setCode(IMRespEnum.PARAM_ERROR_MoDULE_STATUS.getCode());
            }
        }
        return respResult;
    }


    @ApiOperation(value = "模块详情", httpMethod = "GET", notes = "模块详情")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @RequestMapping(value = "/findItemById", method = RequestMethod.GET)
    public RespResult findItemById(@ApiParam(value = "id", required = true)@RequestParam(name = "id", required = true) Long id ){
        RespResult respResult = new RespResult();
        if (Objects.nonNull(id) && id > 0){
            respResult = moduleItemService.findItemById(id);
        }else {
            respResult.setCode(IMRespEnum.FAIL.getCode());
            respResult.setMsg(IMRespEnum.FAIL.getMsg());
        }
        return respResult;
    }


}


