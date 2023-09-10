package com.ketty.api_web.controller;

import com.ketty.common_base.MinioUtil;
import com.ketty.common_utils.R;
import io.minio.Result;
import io.minio.messages.DeleteError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Auther: Ketty Allen
 * @Date:2023/3/2 - 20:54
 * @Description:com.ketty.api_web.controller
 * @version: 1.0
 */
@RestController
@Slf4j
@RequestMapping("/minio")
public class MinioController {
    @Autowired
    MinioUtil minioUtil;

    @PostMapping("/upload")
    public R upload(MultipartFile file, String folder) {
        String upload = minioUtil.upload(file, folder);
        return R.ok().data("result",upload);
    }

    @PostMapping("/delete")
    public R delete(String url) {
        boolean results = minioUtil.remove(url);
        return R.ok().data("result",results);
    }

}
