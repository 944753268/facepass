package com.canbot.retrofitdemo;

import android.util.Log;

import com.canbot.retrofitdemo.utlis.Utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ${ping} on 2018/9/21.
 */
public class FTPUtils {
    public static FTPUtils ftpUtils;

    private FTPUtils() {

    }

    public static FTPUtils getInstance() {
        synchronized (FTPUtils.class) {
            if (ftpUtils == null) {
                ftpUtils = new FTPUtils();
            }
        }
        return ftpUtils;
    }

    public void ftpDown(String url, int port, String username, String password, String filePath, String FTP_Dir,
                        String SD_file, String type) throws Exception {
//        FileOutputStream buffOut = null;
//        InputStream input = null;
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(6 * 1000);
        try {

            ftpClient.connect(url, port);
            boolean loginResult = ftpClient.login(username, password);
            int returnCode = ftpClient.getReplyCode();
            if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
                ftpClient.enterLocalPassiveMode();
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                Boolean isChange = ftpClient.changeWorkingDirectory(FTP_Dir);//转到对应的face_id目录下面
                if (isChange) {
                    String[] fileNames = ftpClient.listNames();
                    for (String fileName : fileNames) {
                        if (fileName.equals("face.jpg") || !fileName.contains(type)) {
                            continue;
                        }
                        Utils.debugger(fileName + "----------");


                        FileOutputStream buffOut = new FileOutputStream( filePath + SD_file, true);//创建本地输出流
                        // ftpClient.retrieveFile(new String(file.getName().getBytes(),"ISO-8859-1"), os);
                        ftpClient.retrieveFile(new String(fileName.getBytes(),"ISO-8859-1"), buffOut);
                        buffOut.flush();
                        buffOut.close();
                    }

                }
                Thread.sleep(1000);
                Utils.debugger("文件下载完成");
                ftpClient.logout();

            } else {
                Log.i("TAG", "ftp 登录失败");

            }
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException("FTP客户端出错！", e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    public void uploadFtpFile(String url, int port, String username, String password, String sdcardFilePath, String desFileName, String filename) {
        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(url, port);
            boolean loginResult = ftpClient.login(username, password);
            int returnCode = ftpClient.getReplyCode();
            if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.mkd(desFileName);//创建ftp目录
                FileInputStream srcFileStream = new FileInputStream(sdcardFilePath);
                ftpClient.storeFile(desFileName + filename, srcFileStream);
                srcFileStream.close();
                Utils.debugger("文件上传完成");
                ftpClient.logout();
            } else {
                Utils.debugger("登录服务器失败");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }

    }
}
