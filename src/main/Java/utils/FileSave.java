package utils;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.UUID;

public class FileSave {

    private Logger logger = LoggerFactory.getLogger(FileSave.class);

    private static volatile FileSave fileSave = null;

    private FileSave() {
    }

    public static synchronized FileSave getFileSave() {
        if (fileSave == null) {
            fileSave = new FileSave();
        }
        return fileSave;
    }

    public String saveFile(FileItem fileItem) {
        Properties properties = new Properties();
        String fileLocation = null;
        try {
            properties.load(new InputStreamReader(this.getClass().getResourceAsStream("/config/fileApplication.properties")));
        } catch (IOException e) {
            logger.error("配置文件读取异常...", e);
            return null;
        }
        String path = properties.getProperty("file.upload.path");
        String f = UUID.randomUUID() + fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
        try {
            fileItem.write(new File(path, f));
        } catch (Exception e) {
            logger.error("文件写入异常...", e);
            return null;
        }
        fileLocation = path +"/"+ f;
        return fileLocation;
    }

    /**
     * 获取文件MD5
     *
     * @return 文件的 MD5 编码
     */
    public String getFileMd5(FileItem fileItem) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = new byte[1024 * 1024 * 10];
        int length = -1;
        try (InputStream fileInputStream = fileItem.getInputStream()) {
            while ((length = fileInputStream.read(bytes, 0, 1024 * 1024)) != -1) {
                messageDigest.update(bytes, 0, length);
            }
        } catch (IOException e) {
            logger.error("文件转换流出现异常", e);
        }
        byte[] md5Bytes = messageDigest.digest();
        BigInteger bigInteger = new BigInteger(1, md5Bytes);
        return bigInteger.toString();
    }
}
