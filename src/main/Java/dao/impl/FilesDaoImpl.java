package dao.impl;

import component.JDBC;
import dao.FilesDao;
import javaBean.FileInformation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FilesDaoImpl implements FilesDao {
    private JDBC jdbc;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public FilesDaoImpl() {
        jdbc = new JDBC();
    }


    @Override
    public void beforeConnection() {
        try {
            if (connection == null || connection.isClosed())
                connection = jdbc.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            logger.error("建立数据库连接失败", e);
        }
    }

    @Override
    public void afterConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                if (resultSet != null || !resultSet.isClosed()) {
                    resultSet.close();
                }
                statement.close();
                connection.close();
                jdbc.close();
            }
        } catch (SQLException e) {
            logger.error("连接关闭异常...", e);
        }
    }

    @Override
    public Integer insertFile(FileInformation fileInformation) {
        int res = -1;
        beforeConnection();
        String sql = "insert into files_saving.file_information(file_name, content_type, size, real_path, access_path, md5) values(" +
                "'" + fileInformation.getFileName() + "'," +
                "'" + fileInformation.getContentType() + "'," +
                "'" + fileInformation.getSize() + "'," +
                "'" + fileInformation.getRealPath() + "'," +
                "'" + fileInformation.getAccessPath() + "'," +
                "'" + fileInformation.getMd5() + "'" +
                ")";

        try {
            if (statement.executeUpdate(sql) != 0) {
                res = getFileIdByMD5(fileInformation.getMd5());
            }
        } catch (SQLException e) {
            logger.error("sql 执行异常...", e);
            res = -1;
        } finally {
            afterConnection();
        }
        return res;
    }

    @Override
    public Integer getFileIdByMD5(String md5) {
        Integer res = -1;
        try {
            if (connection == null || connection.isClosed()) {
                beforeConnection();
            }
        } catch (SQLException e) {
            logger.error("建立数据库连接失败", e);
            return res;
        }
        String searchSql = "select file_id from files_saving.file_information as f where f.md5 = " + md5;

        try {
            resultSet = statement.executeQuery(searchSql);
            if (resultSet.next()) {
                res = resultSet.getInt("file_id");
            }
            if (res == -1){
                res = 0;
            }
        } catch (SQLException e) {
            logger.error("sql 执行异常...", e);
        } finally {
            afterConnection();
        }
        return res;
    }

    @Override
    public String getAccessPathById(Integer id) {
        beforeConnection();
        String accessPath = null;
        String sql = "select access_path from files_saving.file_information as f where f.file_id = " + id;
        try {
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                accessPath = resultSet.getString("access_path");
            }
        } catch (SQLException e) {
            logger.error("sql 执行异常...", e);
        } finally {
            afterConnection();
        }
        return accessPath;
    }
}
