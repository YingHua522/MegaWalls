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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DataBase {
    private final DataBaseCore dataBaseCore;
    private final Logger logger;

    public DataBase(DataBaseCore core) {
        this.dataBaseCore = core;
        this.logger = MegaWalls.getInstance().getLogger();
    }

    public static DataBase create(ConfigurationSection dbConfig) {
        Type type = Type.valueOf(dbConfig.getString("type", "SQLITE").toUpperCase());
        if (type == Type.MYSQL) {
            return new DataBase(new MySQLCore(dbConfig));
        }
        return new DataBase(new SQLiteCore((Plugin) MegaWalls.getInstance(), dbConfig));
    }

    public boolean close() {
        try {
            this.dataBaseCore.getConnection().close();
            return true;
        } catch (SQLException sQLException) {
            return false;
        }
    }

    public boolean createTables(String tableName, KeyValue fields, String Conditions) {
        try {
            this.dataBaseCore.createTables(tableName, fields, Conditions);
            return this.isTableExists(tableName);
        } catch (Exception e) {
            this.sqlerr("\u521b\u5efa\u6570\u636e\u8868 " + tableName + " \u5f02\u5e38(\u5185\u90e8\u65b9\u6cd5)...", e);
            return false;
        }
    }

    public int dbDelete(String tableName, KeyValue fields) {
        String sql = "DELETE FROM `" + tableName + "` WHERE " + fields.toWhereString();
        try {
            return this.dataBaseCore.executeUpdate(sql);
        } catch (Exception e) {
            this.sqlerr(sql, e);
            return 0;
        }
    }

    public boolean dbExist(String tableName, KeyValue fields) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + fields.toWhereString();
        try {
            return this.dataBaseCore.executeQuery(sql).next();
        } catch (Exception e) {
            this.sqlerr(sql, e);
            return false;
        }
    }

    public int dbInsert(String tabName, KeyValue fields) {
        String sql = "INSERT INTO `" + tabName + "` " + fields.toInsertString();
        try {
            return this.dataBaseCore.executeUpdate(sql);
        } catch (Exception e) {
            this.sqlerr(sql, e);
            return 0;
        }
    }

    public List<KeyValue> dbSelect(String tableName, KeyValue fields, KeyValue selCondition) {
        String sql = "SELECT " + fields.toKeys() + " FROM `" + tableName + "`" + (selCondition == null ? "" : " WHERE " + selCondition.toWhereString());
        ArrayList<KeyValue> kvlist = new ArrayList<KeyValue>();
        try {
            ResultSet dbresult = this.dataBaseCore.executeQuery(sql);
            while (dbresult.next()) {
                KeyValue kv = new KeyValue();
                for (String col : fields.getKeys()) {
                    kv.add(col, dbresult.getString(col));
                }
                kvlist.add(kv);
            }
        } catch (Exception e) {
            this.sqlerr(sql, e);
        }
        return kvlist;
    }

    public String dbSelectFirst(String tableName, String fields, KeyValue selConditions) {
        String sql = "SELECT " + fields + " FROM " + tableName + " WHERE " + selConditions.toWhereString() + " limit 1";
        try {
            ResultSet dbresult = this.dataBaseCore.executeQuery(sql);
            if (dbresult.next()) {
                return dbresult.getString(fields);
            }
        } catch (Exception e) {
            this.sqlerr(sql, e);
        }
        return null;
    }

    public int dbUpdate(String tabName, KeyValue fields, KeyValue upCondition) {
        String sql = "UPDATE `" + tabName + "` SET " + fields.toUpdateString() + " WHERE " + upCondition.toWhereString();
        try {
            return this.dataBaseCore.executeUpdate(sql);
        } catch (Exception e) {
            this.sqlerr(sql, e);
            return 0;
        }
    }

    public DataBaseCore getDataBaseCore() {
        return this.dataBaseCore;
    }

    public boolean isValueExists(String tableName, KeyValue fields, KeyValue selCondition) {
        String sql = "SELECT " + fields.toKeys() + " FROM `" + tableName + "`" + (selCondition == null ? "" : " WHERE " + selCondition.toWhereString());
        try {
            ResultSet dbresult = this.dataBaseCore.executeQuery(sql);
            return dbresult.next();
        } catch (Exception e) {
            this.sqlerr(sql, e);
            return false;
        }
    }

    public boolean isFieldExists(String tableName, KeyValue fields) {
        try {
            DatabaseMetaData dbm = this.dataBaseCore.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            if (tables.next()) {
                ResultSet f = dbm.getColumns(null, null, tableName, fields.getKeys()[0]);
                return f.next();
            }
        } catch (SQLException e) {
            this.sqlerr("\u5224\u65ad \u8868\u540d:" + tableName + " \u5b57\u6bb5\u540d:" + fields.getKeys()[0] + " \u662f\u5426\u5b58\u5728\u65f6\u51fa\u9519!", e);
        }
        return false;
    }

    public boolean isTableExists(String tableName) {
        try {
            DatabaseMetaData dbm = this.dataBaseCore.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            return tables.next();
        } catch (SQLException e) {
            this.sqlerr("\u5224\u65ad \u8868\u540d:" + tableName + " \u662f\u5426\u5b58\u5728\u65f6\u51fa\u9519!", e);
            return false;
        }
    }

    public void sqlerr(String sql, Exception e) {
        this.logger.warning("\u6570\u636e\u5e93\u64cd\u4f5c\u51fa\u9519: " + e.getMessage());
        this.logger.warning("SQL\u67e5\u8be2\u8bed\u53e5: " + sql);
    }

    static enum Type {
        MYSQL,
        SQLITE;

    }
}

