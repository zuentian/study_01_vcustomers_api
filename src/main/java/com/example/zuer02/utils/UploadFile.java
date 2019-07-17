package com.example.zuer02.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class UploadFile {



    public static String uploadMultipartFile(MultipartFile file,String filePath)throws Exception{

        File path=new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path.exists()){
            path=new File("");
        }
        if (file!=null) {
            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            // 判断文件类型
            type = fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
            if (type!=null) {// 判断文件类型是否为空
                if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {
                    File upload=new File(path.getAbsolutePath(),"static/file/images/");

                    if(!upload.exists()) {
                        upload.mkdirs();
                    }
                    try {
                        file.transferTo(new File(upload.getAbsolutePath()+ File.separator+fileName));
                    } catch (Exception e) {
                        throw new Exception("图片文件储存异常！");
                    }
                }else{
                    File upload=new File(path.getAbsolutePath(),"static/file/other/");

                    if(!upload.exists()) {
                        upload.mkdirs();
                    }
                    try {
                        file.transferTo(new File(upload.getAbsolutePath()+ File.separator+fileName));
                        return upload.getAbsolutePath()+File.separator+fileName;
                    } catch (Exception e) {
                        throw new Exception("文件储存异常！");
                    }
                }
            }else {

                throw new Exception("上传文件类型有误！");

            }
        }


        return "";
    }



}
