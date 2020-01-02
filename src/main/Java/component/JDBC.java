package component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBC {
    private Logger logger = LoggerFactory.getLogger(JDBC.class);
    private String url;
    private String username;
    private String password;
    private String driver;
    private Connection connection;

    public JDBC() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(this.getClass().getResourceAsStream("/config/fileApplication.properties")));
            url = properties.getProperty("jdbc.url");
            username = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");
            driver = properties.getProperty("jdbc.driver");
        } catch (IOException e) {
            logger.error("jdbc 配置文件读取异常...", e);
        }
    }

    public Connection getConnection(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            logger.error("driver 实例异常", e);
        } catch (SQLException e) {
            logger.error("数据库连接异常...", e);
        }
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
