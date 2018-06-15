package com.haier.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.haier.base.IMRespEnum;
import com.haier.base.RespResult;
import com.haier.controller.po.ModuleItemListResp;
import com.haier.dao.ModuleItemMapper;
import com.haier.model.ModuleItemEntity;
import com.haier.po.ModuleItemPo;
import com.haier.service.ModuleItemService;
import com.mysql.jdbc.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ModuleItemServiceImpl implements ModuleItemService {


    @Autowired
    ModuleItemMapper moduleItemMapper;


    @Value("${upload.url}")
    private String uploadurl;



    @Override
    public RespResult listAllModuleItems(Long moduleId, String searchAttr,Integer status, Integer pageNo, Integer pageSize) {
        RespResult respResult = new RespResult();
        int size = Integer.valueOf(pageSize);
        int page = Integer.valueOf(pageNo);
        Integer startIndex = size * (page - 1);

        ModuleItemPo po = new ModuleItemPo();
        po.setPageSize(pageSize);
        po.setStartRow(startIndex);
        po.setIsDeleted(Boolean.FALSE);//已删除不展示
        po.setModuleId(moduleId);
        if (!StringUtils.isNullOrEmpty(searchAttr)){
            searchAttr="%"+searchAttr+"%";
        }else{
            searchAttr = null;
        }
        po.setSearchAttr(searchAttr);
        po.setStatus(status);
        List<ModuleItemEntity>  list = moduleItemMapper.getModuleItemsBy(po);
        int count = moduleItemMapper.getCountBy(po);
        if (Objects.nonNull(list) && list.size()>0 && count>0){
            ModuleItemListResp resp=new ModuleItemListResp();
            resp.setTotal(count);
            resp.setPageNo(pageNo);
            resp.setPageSize(pageSize);
            resp.setList(list);
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getCode());
            respResult.setData(resp);
        }else{
            respResult.setCode(IMRespEnum.FAIL.getCode());
            respResult.setMsg(IMRespEnum.FAIL.getCode());
        }
        return respResult;
    }


    private String uploadFile(MultipartFile multipartFile){
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType( MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));
        //先上传
        if (Objects.nonNull(multipartFile)) {


            File path = null;
            try {
                path = new File(ResourceUtils.getURL("classpath:").getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(!path.exists()) {
                path = new File("");
            }
            System.out.println("path:"+path.getAbsolutePath());
            String filePath = path.getAbsolutePath()+multipartFile.getOriginalFilename();
            System.out.println(filePath);
            File files=new File(filePath);
            // 转存文件
            try {
                multipartFile.transferTo(files);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileSystemResource resource = new FileSystemResource(new File(filePath));

            MultiValueMap<String,Object> form=new LinkedMultiValueMap<>();
            form.add("file", resource);
            form.add("isPub", true);
            form.add("fKey", "fKey");
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(form, headers);
            ResponseEntity<String> resp=restTemplate.postForEntity(uploadurl,httpEntity,String.class);
            try {
                files.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println("======="+resp.getStatusCode()+"====");
            if (resp.getStatusCode().value()==200){
                Object obj = resp.getBody().toString();
                JSONObject jsonObject = JSONObject.fromObject(obj);
                RespResult JrespResult = (RespResult) JSONObject.toBean(jsonObject, RespResult.class);
                String dataUrl = JrespResult.getData().toString();
                if (!(StringUtils.isNullOrEmpty(dataUrl))) {
                    return dataUrl;
                }else{
                    return IMRespEnum.FAIL.getCode();
                }
            }else{
                return IMRespEnum.FAIL.getCode();
            }
        }
        return IMRespEnum.FAIL.getCode();
    }


    @Override
    public RespResult updateModuleItem(ModuleItemEntity updateEntity,MultipartFile thumPic,MultipartFile backGroundPic,MultipartFile detailPic) {
        RespResult respResult = new RespResult();
        //先上传
        if (Objects.nonNull(thumPic)){
            String re=this.uploadFile(thumPic);
            if (!(StringUtils.isNullOrEmpty(re)) && !(re.equals(IMRespEnum.FAIL.getCode()))){
                //成功
                updateEntity.setThumUrl(re);
            }else{
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
                return respResult;
            }
        }
        if (Objects.nonNull(backGroundPic)){
            String re=this.uploadFile(backGroundPic);
            if (!(StringUtils.isNullOrEmpty(re)) && !(re.equals(IMRespEnum.FAIL.getCode()))){
                //成功
                updateEntity.setBackgroundUrl(re);
            }else{
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
                return respResult;
            }
        }
        if (Objects.nonNull(detailPic)){
            String re=this.uploadFile(detailPic);
            if (!(StringUtils.isNullOrEmpty(re)) && !(re.equals(IMRespEnum.FAIL.getCode()))){
                //成功
                updateEntity.setDetailUrl(re);
            }else{
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
                return respResult;
            }
        }
        int count = moduleItemMapper.updateByPrimary(updateEntity);
       if (count>0){
           respResult.setCode(IMRespEnum.SUCCESS.getCode());
           respResult.setMsg(IMRespEnum.SUCCESS.getCode());
       }else{
           respResult.setCode(IMRespEnum.FAIL.getCode());
           respResult.setMsg(IMRespEnum.FAIL.getMsg());
       }
        return respResult;
    }

    @Override
    public RespResult addModleItem(ModuleItemEntity addEntity, MultipartFile thumPic, MultipartFile backGroundPic, MultipartFile detailPic) {
        RespResult respResult = new RespResult();
        //先上传
        if (Objects.nonNull(thumPic)){
            String re=this.uploadFile(thumPic);
            if (!(StringUtils.isNullOrEmpty(re)) && !(re.equals(IMRespEnum.FAIL.getCode()))){
                //成功
                addEntity.setThumUrl(re);
            }else{
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
                return respResult;
            }
        }
        if (Objects.nonNull(backGroundPic)){
            String re=this.uploadFile(backGroundPic);
            if (!(StringUtils.isNullOrEmpty(re)) && !(re.equals(IMRespEnum.FAIL.getCode()))){
                //成功
                addEntity.setBackgroundUrl(re);
            }else{
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
                return respResult;
            }
        }
        if (Objects.nonNull(detailPic)){
            String re=this.uploadFile(detailPic);
            if (!(StringUtils.isNullOrEmpty(re)) && !(re.equals(IMRespEnum.FAIL.getCode()))){
                //成功
                addEntity.setDetailUrl(re);
            }else{
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
                return respResult;
            }
        }
        int count = moduleItemMapper.insert(addEntity);
        if (count>0){
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        }else{
            respResult.setCode(IMRespEnum.FAIL.getCode());
            respResult.setMsg(IMRespEnum.FAIL.getMsg());
        }
        return respResult;
    }

    @Override
    public RespResult delModuleItem(Long id) {
        RespResult respResult = new RespResult();
        int count = moduleItemMapper.deleteById(id);
        if (count>0){
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        }else{
            respResult.setCode(IMRespEnum.FAIL.getCode());
            respResult.setMsg(IMRespEnum.FAIL.getMsg());
        }
        return respResult;
    }

    @Override
    public RespResult changeModuleItemStatus(Long id, Integer status) {
        RespResult respResult = new RespResult();
        ModuleItemEntity po = new ModuleItemEntity();
        po.setId(id);
        po.setStatus(status);
        po.setIsDeleted(Boolean.FALSE);
        int re=moduleItemMapper.updateByPrimary(po);
        if (re>0){
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        }else {
            respResult.setCode(IMRespEnum.FAIL.getCode());
            respResult.setMsg(IMRespEnum.FAIL.getMsg());
        }
        return respResult;
    }

    @Override
    public RespResult findItemById(Long id) {
        RespResult respResult = new RespResult();
        ModuleItemPo po = new ModuleItemPo();
        po.setId(id);
        List<ModuleItemEntity> list = moduleItemMapper.getModuleItemsBy(po);
        if (Objects.nonNull(list) && list.size() > 0){
            ModuleItemEntity re = list.get(0);
            if (Objects.nonNull(re)){
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getCode());
                respResult.setData(re);
            }else{
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
            }
        }else {
            respResult.setCode(IMRespEnum.FAIL.getCode());
            respResult.setMsg(IMRespEnum.FAIL.getMsg());
        }
        return respResult;
    }
}
