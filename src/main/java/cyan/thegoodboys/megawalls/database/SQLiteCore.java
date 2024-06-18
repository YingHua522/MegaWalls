/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.database;

import cyan.thegoodboys.megawalls.MegaWalls;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLiteCore extends DataBaseCore {
    private static String driverName = "org.sqlite.JDBC";
    private static Logger logger = MegaWalls.getInstance().getLogger();
    private Connection connection;
    private File dbFile;

    public SQLiteCore(File dbFile) {
        this.dbFile = dbFile;
        if (this.dbFile.exists()) {
            try {
                this.dbFile.createNewFile();
            } catch (IOException e) {
                logger.warning("\u6570\u636e\u5e93\u6587\u4ef6 " + dbFile.getAbsolutePath() + " \u521b\u5efa\u5931\u8d25!");
                e.printStackTrace();
            }
        }
        try {
            Class.forName(driverName).newInstance();
        } catch (Exception e) {
            logger.warning("\u6570\u636e\u5e93\u521d\u59cb\u5316\u5931\u8d25 \u8bf7\u68c0\u67e5\u9a71\u52a8 " + driverName + " \u662f\u5426\u5b58\u5728!");
            e.printStackTrace();
        }
    }

    public SQLiteCore(Plugin plugin, ConfigurationSection cfg) {
        this(plugin, cfg.getString("database"));
    }

    public SQLiteCore(Plugin plugin, String filename) {
        this.dbFile = new File(plugin.getDataFolder(), filename + ".db");
        if (this.dbFile.exists()) {
            try {
                this.dbFile.createNewFile();
            } catch (IOException e) {
                logger.warning("数据库\u6587\u4ef6 " + this.dbFile.getAbsolutePath() + " \u521b\u5efa\u5931\u8d25!");
                e.printStackTrace();
            }
        }
        try {
            Class.forName(driverName).newInstance();
        } catch (Exception e) {
            logger.warning("\u6570\u636e\u5e93\u521d\u59cb\u5316\u5931\u8d25 \u8bf7\u68c0\u67e5\u9a71\u52a8 " + driverName + " \u662f\u5426\u5b58\u5728!");
            e.printStackTrace();
        }
    }

    public SQLiteCore(String filepath) {
        this(new File(filepath));
    }

    @Override
    public boolean createTables(String tableName, KeyValue fields, String Conditions) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `%s` ( %s )";
        return this.execute(String.format(sql, tableName, fields.toCreateString().replace("AUTO_INCREMENT", "AUTOINCREMENT")));
    }

    public String getAUTO_INCREMENT() {
        return "AUTOINCREMENT";
    }

    @Override
    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return this.connection;
            }
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.dbFile);
            return this.connection;
        } catch (SQLException e) {
            logger.warning("\u6570\u636e\u5e93\u64cd\u4f5c\u51fa\u9519: " + e.getMessage());
            logger.warning("\u6570\u636e\u5e93\u6587\u4ef6: " + this.dbFile.getAbsolutePath());
            return null;
        }
    }
}

