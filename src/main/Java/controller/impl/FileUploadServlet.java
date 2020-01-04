package controller.impl;

import controller.FileOperator;
import javaBean.Detail;
import org.json.JSONObject;
import service.FileService;
import service.impl.FileUploadServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "/fileUploadServlet", value = "/fileUploadServlet")
public class FileUploadServlet extends HttpServlet implements FileOperator {

    private FileService fileService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        fileService = new FileUploadServiceImpl();
        Detail detail = fileService.fileUpload(request);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", detail.getStatus());
        jsonObject.put("msg", detail.getMsg());
        jsonObject.put("result", detail.getMap());
        response.setCharacterEncoding("GBK");
        response.getWriter().write(jsonObject.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
