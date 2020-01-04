package service.impl;

import dao.FilesDao;
import dao.impl.FilesDaoImpl;
import javaBean.Detail;
import javaBean.FileInformation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.FileService;
import utils.FileSave;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class FileUploadServiceImpl implements FileService {

    private FileSave fileSave = FileSave.getFileSave();
    private FilesDao filesDao = new FilesDaoImpl();
    private Detail detail = new Detail();


    @Override
    public Detail fileUpload(HttpServletRequest request) {
        //创建磁盘文件工厂
        FileItemFactory fif = new DiskFileItemFactory();
        //文件上传处理
        ServletFileUpload sf = new ServletFileUpload(fif);

        String path;

        List<FileItem> list;
        try {
            list = sf.parseRequest(request);
        } catch (FileUploadException e) {
            logger.error("请求解析文件列表异常...", e);
            detail.setStatus(500);
            detail.setMsg("请求解析文件列表异常...");
            return detail;
        }
        for (FileItem fil : list) {
            if (!fil.getName().isEmpty()) {
                String md5;
                try {
                    md5 = fileSave.getFileMd5(fil);
                } catch (NoSuchAlgorithmException e) {
                    logger.error("MD5 计算异常", e);
                    detail.setStatus(501);
                    detail.setMsg("MD5 计算异常");
                    return detail;
                }
                int res = filesDao.getFileIdByMD5(md5);
                if (res == -1) {                 // 数据库异常
                    detail.setStatus(502);
                    detail.setMsg("数据库异常");
                } else if (res == 0) {            // 文件不存在
                    path = fileSave.saveFile(fil);
                    path = path.replace("\\", "/");
                    FileInformation fileInformation = new FileInformation();
                    fileInformation.setFileName(fil.getName());
                    fileInformation.setContentType(fil.getContentType());
                    fileInformation.setSize(fil.getSize());
                    fileInformation.setMd5(md5);
                    fileInformation.setRealPath(path);
                    String accessPath = fileSave.readProperties("access.path");
                    String[] paths = path.split("/");
                    accessPath = accessPath + "/" + paths[paths.length - 1];
                    fileInformation.setAccessPath(accessPath);            // 后续需要保存静态资源访问地址
                    if (filesDao.insertFile(fileInformation) == -1) {
                        detail.setStatus(502);
                        detail.setMsg("数据库异常");
                    } else {
                        fileInformation.setMd5(null);
                        fileInformation.setRealPath(null);
                        detail.setStatus(200);
                        detail.setMsg("成功");
                        detail.getMap().put("file", fileInformation);
                    }
                } else {                            // 文件存在
//                    String accessPath = filesDao.getAccessPathById(res);
                    FileInformation fileInformation = filesDao.getFileById(res);
                    if (fileInformation == null) {
                        detail.setStatus(502);
                        detail.setMsg("数据库异常");
                    } else {
                        detail.setStatus(200);
                        detail.setMsg("成功");
                        detail.getMap().put("file", fileInformation);
                    }

                }
            }
        }
        return detail;
    }
}
