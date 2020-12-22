package com.offcn.project.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.utils.OssTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "项目基本功能模块（文件上传、项目信息获取等）")
@Slf4j
@RequestMapping("/project")
@RestController
public class ProjectInfoController {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation("文件上传功能")
    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> upload(@RequestParam("file") MultipartFile[] files) throws IOException {

        Map<String,Object> map=new HashMap<>();

        List<String> list=new ArrayList<>();

        if(files!=null&&files.length>0){
            //遍历上传文件数组
            for (MultipartFile file : files) {
                //判断上传文件是否存在
                if(!file.isEmpty()){
                    //调用OSStemplate执行文件上传
                    String upload = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                    list.add(upload);

                }
            }
        }
        //把上传成功的文件集合添加到返回的map
        map.put("urls",list);

        log.debug("ossTemplate信息：{},文件上传成功访问路径{}",ossTemplate,list);

        return AppResponse.ok(map);
    }

    @ApiOperation("获取项目回报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> detailsReturn(@PathVariable("projectId") Integer projectId) {

        List<TReturn> returns = projectInfoService.getProjectReturns(projectId);
        return AppResponse.ok(returns);
    }


}
