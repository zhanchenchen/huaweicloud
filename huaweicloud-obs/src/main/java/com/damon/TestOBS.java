package com.damon;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;

import java.io.*;

/**
 * 测试华为云OBS
 */
public class TestOBS {
    private static final String END_POINT = "obs.cn-north-4.myhuaweicloud.com";
    private static final String AK = "PKGQTB904INJTLGUSEAK";
    private static final String SK = "vGM9chfpRSm45FKMCCepmMl0GUmT7NZuGOdY5P7e";
    private static final String BUCKET = "vkepai-test";

    public static void main(String[] args){
        ObsClient obsClient = null;
        try {
            // 创建obsClient实例
            obsClient = new ObsClient(AK, SK, END_POINT);

            // 访问OBS

            // 创建桶
//        obsClient.createBucket("vkepai-test");

//        upload(obsClient);    // 上传对象
            uploadAndSetAcl(obsClient); // 上传对象，并且设置访问权限
//        download(obsClient);    // 下载对象
//        objectList(obsClient);  // 列举对象
//        deleteObject(obsClient);    // 删除对象


        } catch (ObsException e) {
            System.out.println("HTTP Code: " + e.getResponseCode());
            System.out.println("Error Code:" + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());

            System.out.println("Request ID:" + e.getErrorRequestId());
            System.out.println("Host ID:" + e.getErrorHostId());
        } finally {
            // 关闭ObsClient实例，如果是全局ObsClient实例，可以不在每个方法调用完成后关闭
            // ObsClient在调用ObsClient.close方法关闭后不能再次使用
            if(obsClient != null){
                try {
                    // 关闭obsClient,全局ObsClient实例可不用在每个方法调用完成后关闭
                    obsClient.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    /**
     * 删除对象
     * @param obsClient
     */
    public static void deleteObject(ObsClient obsClient){
        DeleteObjectResult result = obsClient.deleteObject(BUCKET, "ColdPlay-cc.jpg");
        System.out.println(result);
    }

    /**
     * 列举桶中对象
     * @param obsClient
     */
    public static void objectList(ObsClient obsClient){
        ObjectListing objectListing = obsClient.listObjects(BUCKET);
        for (ObsObject obsObject: objectListing.getObjects()) {
            System.out.println("-"+obsObject.getObjectKey()+"   (size = "+obsObject.getMetadata().getContentLength()+")");
        }
    }

    /**
     * 下载对象
     *
     * @param obsClient
     */
    public static void download(ObsClient obsClient) throws IOException {
        ObsObject obsObject = obsClient.getObject(BUCKET, "ColdPlay-cc.jpg");
        System.out.println(obsObject);
        InputStream objectContent = obsObject.getObjectContent();   // 获得输入流
        if (objectContent != null) {
            File file = new File("D:" + File.separator + "测试图片.jpg");
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024]; // 读入缓冲区
            int len = 0;
            while ((len =objectContent.read(buffer))!=-1) {
                outputStream.write(buffer,0,len);   // 输出文件流
            }
            outputStream.close(); // 关闭输出流
            objectContent.close();  // 关闭输入流
        }
    }

    /**
     * 上传对象
     *
     * @param obsClient
     */
    public static void upload(ObsClient obsClient) {
        //        PutObjectResult putObjectResult = obsClient.putObject("vkepai-test","test-cc.txt",new ByteArrayInputStream("Hello OBS".getBytes()));
        PutObjectResult putObjectResult = obsClient.putObject(BUCKET, "ColdPlay-cc.jpg", new File("D:" + File.separator + "图片" + File.separator + "我的图片" + File.separator + "QQ图片20190707160407.jpg"));
        System.out.println(putObjectResult);
        System.out.println(putObjectResult.getObjectUrl()); // 获得对象访问地址
        System.out.println(putObjectResult.getStatusCode());    // 状态码
        System.out.println(putObjectResult.getRequestId()); // 请求id
    }

    /**
     * 上传对象,并且设置访问权限
     *
     * @param obsClient
     */
    public static void uploadAndSetAcl(ObsClient obsClient) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(); // 设置上传对象请求
        putObjectRequest.setBucketName(BUCKET);
        putObjectRequest.setFile(new File("D:" + File.separator + "图片" + File.separator + "我的图片" + File.separator + "QQ图片20190707160407.jpg"));
        putObjectRequest.setObjectKey("ColdPlay-cc.jpg");
        putObjectRequest.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ); // 匿名读
        PutObjectResult putObjectResult = obsClient.putObject(putObjectRequest);
        System.out.println(putObjectResult);
        System.out.println(putObjectResult.getObjectUrl()); // 获得对象访问地址
        System.out.println(putObjectResult.getStatusCode());    // 状态码
        System.out.println(putObjectResult.getRequestId()); // 请求id
    }
}
