package com.maz.reggie.controller;

import com.maz.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        //获取原始文件名
        String originalFileName = file.getOriginalFilename();
        //String originalFileName =UUID.randomUUID().toString();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;
        File dir = new File(basePath);
        if(!dir.exists()){
            //目录不存在，需要创建
            dir.mkdirs();
        }
        log.info(file.toString());
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流：通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流：通过输出流将文件写回浏览器，在浏览器展示图片了
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭流
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
