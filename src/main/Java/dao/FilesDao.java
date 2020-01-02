package dao;

import component.JDBC;
import javaBean.FileInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public interface FilesDao {
    Logger logger = LoggerFactory.getLogger(FilesDao.class);    // 日志框架，用于打印日志

    /**
     * 用于在执行 sql 前连接相关信息:
     * <code>Connection</code>
     * <code>statement</code>
     * @throws SQLException
     */
    public void beforeConnection() throws SQLException;

    /**
     * 用于执行sql后关闭相关连接:
     * <code>connection</code>
     * <code>statement</code>
     * <code>resultSet</code> 如果不为 null
     * @throws SQLException
     */
    public void afterConnection() throws SQLException;

    /**
     * 将文件的相关信息存储到数据库中
     * @param fileInformation <class>FileInformation</class>
     * @return  返回插入值的id
     * @throws SQLException
     */
    public Integer insertFile(FileInformation fileInformation);

    /**
     * 通过文件的MD5 获得文件的id
     * @param md5
     * @return <code>-1</code> 出现异常, <code>0</code> 结果为空， <code>id</code> 文件的id
     */
    public Integer getFileIdByMD5(String md5);

    /**
     * 通过文件Id 获得文件的相关信息
     * @param id
     * @return
     */
    default public FileInformation getFileById(Integer id){
        return null;
    }

    public String getAccessPathById(Integer id);

}
