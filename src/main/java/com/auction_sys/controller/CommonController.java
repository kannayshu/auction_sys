package com.auction_sys.controller;

import com.auction_sys.common.ServerResponse;
import com.auction_sys.service.CommonService;
import com.auction_sys.service.FileService;
import com.auction_sys.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created By liuda on 2018/5/26
 */
@CrossOrigin("*")
@Controller
@ResponseBody
@RequestMapping(value = "/common/")
public class CommonController {

    @Autowired
    FileService fileService;
    @Autowired
    CommonService commonService;
    @GetMapping("create_captcha")
    public ServerResponse createCaptcha(HttpSession httpSession){
        return commonService.createCaptcha(httpSession);
    }
    @PostMapping("verify_captcha")
    public ServerResponse verifyCaptcha(HttpSession httpSession,@RequestParam(value="captcha",required = true)String captcha){
        return commonService.verifyCaptcha(httpSession,captcha);
    }
    @GetMapping("peek_captcha")
    public ServerResponse peekCaptcha(HttpSession httpSession,String captcha){
        return commonService.verifyCaptchaPeek(httpSession,captcha);
    }
    @PostMapping("upload_image")
    @ResponseBody
    public ServerResponse uploadImage(HttpServletRequest httpServletRequest, @RequestParam(value = "upload_file",required = true) MultipartFile file){

        String path = "image";
        String targetFileName = fileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServerResponse.createBySuccess(fileMap);

    }


    @PostMapping("richtext_img_upload")
    @ResponseBody
    public Map richtextImgUpload( @RequestParam(value = "upload_file",required = true) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();


//        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
////        {
////            "success": true/false,
////                "msg": "error message", # optional
////            "file_path": "[real file path]"
////        }


        String path = "image";
        String targetFileName = fileService.upload(file,path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }

}
