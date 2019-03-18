package com.zhsl.data.util;

import com.zhsl.data.entity.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.util.logging.Logger;

public class FastDFSClient {
    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageServer storageServer;
    private static StorageClient storageClient;
    private static Logger logger = Logger.getLogger("FastDFSClient.class");
    static {
        try {
            //TODO 文件路径
            //String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            //ClientGlobal.init(filePath);
            ClientGlobal.init("E:\\code\\java\\handle-data\\src\\main\\resources\\fdfs_client.conf");
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.warning("fastFDS初始化失败");
            e.printStackTrace();
        }
    }
    public static String[] upload(FastDFSFile file) {
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", file.getAuthor());
        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

        if (uploadResults == null) {
            logger.info("upload file fail, error code:" + storageClient.getErrorCode());
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];

        logger.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
        return uploadResults;
    }
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            logger.info("IO Exception: Get File from Fast DFS failed");
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Non IO Exception: Get File from Fast DFS failed");
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] downFile(String groupName, String remoteFileName) {
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            return fileByte;
        } catch (IOException e) {
            logger.info("IO Exception: Get File from Fast DFS failed");
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Non IO Exception: Get File from Fast DFS failed");
            e.printStackTrace();
        }
        return null;
    }
    public static void deleteFile(String groupName, String remoteFileName)
            throws Exception {
        storageClient = new StorageClient(trackerServer, storageServer);
        int i = storageClient.delete_file(groupName, remoteFileName);
        logger.info("delete file successfully!!!" + i);
    }
}
