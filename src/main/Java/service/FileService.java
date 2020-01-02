package service;

import javaBean.Detail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface FileService {
    Logger logger = LoggerFactory.getLogger(FileService.class);

    default public Detail fileUpload(HttpServletRequest request) {
        return null;
    }

    default public Map fileDownload(){
        return null;
    }
}
