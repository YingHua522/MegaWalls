/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 */
package cyan.thegoodboys.megawalls.database;

import cyan.thegoodboys.megawalls.MegaWalls;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQLCore extends DataBaseCore {
    private static String driverName = "com.mysql.jdbc.Driver";
    private static Logger logger = MegaWalls.getInstance().getLogger();
    private String username;
    private String password;
    private Connection connection;
    private String url;

    public MySQLCore(ConfigurationSection cfg) {
        this(cfg.getString("ip"), cfg.getInt("port"), cfg.getString("database"), cfg.getString("username"), cfg.getString("password"));
    }

    public MySQLCore(String host, int port, String dbname, String username, String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
        this.username = username;
        this.password = password;
        try {
            Class.forName(driverName).newInstance();
        } catch (Exception e) {
            logger.warning("数据库初始化失败 请检查驱动 " + driverName + " 是否存在!");
        }
    }

    @Override
    public boolean createTables(String tableName, KeyValue fields, String conditions) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `" + tableName + "` ( " + fields.toCreateString() + (conditions == null ? "" : " , " + conditions) + " );";
        return this.execute(sql);
    }

    @Override
    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return this.connection;
            }
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
            return this.connection;
        } catch (SQLException e) {
            logger.warning("\u6570\u636e\u5e93\u64cd\u4f5c\u51fa\u9519: " + e.getMessage());
            logger.warning("\u767b\u5f55URL: " + this.url);
            logger.warning("\u767b\u5f55\u8d26\u6237: " + this.username);
            logger.warning("\u767b\u5f55\u5bc6\u7801: " + this.password);
            return null;
        }
    }
}

