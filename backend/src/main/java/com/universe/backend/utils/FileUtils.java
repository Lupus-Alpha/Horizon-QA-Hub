package com.universe.backend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.universe.backend.common.exception.FileException;
import org.aspectj.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileUtils {
    public static String transferFile(MultipartFile uploadFile, String dir, String fileName){
        if(uploadFile == null || uploadFile.isEmpty()){
            throw new IllegalArgumentException("MultipartFile is empty");
        }

        File testDir = new File(dir);
        if(!testDir.exists()){
            if(!testDir.mkdirs()){
                throw new RuntimeException("Fail to create directory");
            }
        }
        String filePath = testDir + "/" + fileName;
        File file = new File(filePath);
        try (InputStream in = uploadFile.getInputStream(); OutputStream out = new FileOutputStream(file)) {
            file.createNewFile();
            FileUtil.copyStream(in, out);
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        }
        return file.getPath();
    }
    public static void delFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            String dir = path.length() == 0 ? "" : path + "/";
            //递归删除目录中的子目录下
            for (int i=0; i<files.length; i++) {
                delFile(dir + files[i].getName());
            }
        }
        file.delete();
    }

    public static void createJsonFile(JSONObject json,String filePath){
        String content = JSON.toJSONString(json, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        try{
            File file = new File(filePath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            Writer writer = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            writer.write(content);
            writer.flush();
            writer.close();
        }catch (IOException e){
            throw new FileException("创建文件失败");
        }
    }

    public static void downloadFile(HttpServletResponse response, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileException("文件不存在");
        }
        try (InputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()){
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new FileException("下载文件失败");
        }
    }

}
