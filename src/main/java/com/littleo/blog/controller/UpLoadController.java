package com.littleo.blog.controller;

import com.littleo.blog.utils.QiniuUtils;
import com.littleo.blog.vo.params.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.UUID;

/**
 * @author 欧阳巍
 * @Date 2022/2/21 10:32
 */
@RestController
@RequestMapping("upload")
public class UpLoadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result upLoad(@RequestParam("image") MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString()+"."+StringUtils.substringAfterLast(originalFilename, ".");
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
}
