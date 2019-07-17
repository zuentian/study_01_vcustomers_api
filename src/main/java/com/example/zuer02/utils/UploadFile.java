package com.example.zuer02.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class UploadFile {



    /*
    filePath是从日期开始到文件名的路径
     */
    public static String uploadMultipartFile(MultipartFile file,String filePath)throws Exception{

        Calendar cal = Calendar.getInstance();
        String year=cal.get(Calendar.YEAR)+"";
        String month=cal.get(Calendar.MONTH)+1<10?"0"+(cal.get(Calendar.MONTH)+1):(cal.get(Calendar.MONTH)+1)+"";
        String day=cal.get(Calendar.DAY_OF_MONTH)+"";
        String datePath=year+ File.separator+month+File.separator+day;
        if (file!=null) {
            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            // 判断文件类型
            type = fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
            if (type!=null) {// 判断文件类型是否为空

                File path=new File(ResourceUtils.getURL("classpath:").getPath());
                if(!path.exists()){
                    path=new File("");
                }
                File upload=new File(path.getAbsolutePath(),"static/file/images/");
                if(!upload.exists()) {
                    upload.mkdirs();
                }
                File finalFileDir=new File(upload.getAbsolutePath()+File.separator+datePath);
                if(!finalFileDir.exists()) {
                    finalFileDir.mkdirs();
                }
                try {
                    file.transferTo(new File(finalFileDir.getAbsolutePath()+ File.separator+filePath+"."+type));
                    return datePath+ File.separator+filePath+"."+type;
                } catch (Exception e) {
                    throw new Exception("图片文件储存异常！");
                }

            }else {

                throw new Exception("上传文件类型有误！");

            }
        }
        return "";
    }



}
