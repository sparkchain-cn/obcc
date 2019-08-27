package cn.obcc.db.sqlite.utils;

import cn.obcc.db.sqlite.SqliteFactroy;
import cn.obcc.db.sqlite.vo.Column;
import cn.obcc.db.sqlite.vo.DataBase;
import cn.obcc.db.sqlite.vo.Record;

import java.util.Date;
import java.util.Locale;

/**
 * SqlUtils
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/20 10:05
 * @details
 */
public class SqlUtils {

    public static String createTaleSql(String tableName, Column... vos) {

        String sql = String.format("create table %s (ID BIGINT PRIMARY KEY  %s ); ", tableName,
                vos.length > 0 ? getColumns(vos) : "");
        return sql;

    }

    public static String getColumns(Column... vos) {

        StringBuilder builder = new StringBuilder();

        for (Column vo : vos) {
            builder.append(", \r\n");
            builder.append(" " + vo.getColumnName() + " ");
            builder.append(" " + vo.getColumnType() + " ");
            if (vo.getDefaultValue() != null) {
                builder.append(String.format("DEFAULT ( %s ) ", vo.getDefaultValue()));
            }
        }
        return builder.toString();
    }

    public static String insert(String tableName, Object dataBase) {
        String sql =
                String.format(Locale.CHINESE, "INSERT INTO %s ( %s )  \n" +
                                "VALUES ( %s );",
                        tableName, getTableVolumes(tableName), getTableVelumValues(tableName, dataBase));
        return sql;
    }

    public static String getTableVolumes(String tableName) {
        switch (tableName) {
            case SqliteFactroy.TB_CONNECTOR:
                return "id, tag, jdbc, standard,user,password,sql,type,createTime";
            case SqliteFactroy.TB_UP_CHAIN_RECORD:
                return " id, connectorId, upchain, createTime ";
            default:
                return "";
        }
    }

    public static String getTableVelumValues(String tableName, Object data) {

        switch (tableName) {
            case SqliteFactroy.TB_CONNECTOR:
                DataBase dataBase;
                if (data instanceof DataBase) {
                    dataBase = (DataBase) data;
                } else {
                    dataBase = new DataBase();
                }
                return String.format("%d,'%s', '%s', '%s','%s','%s','%s',%d,%d", dataBase.getId(),
                        dataBase.getTag(), dataBase.getJdbcUrl(), dataBase.getStandard(),
                        dataBase.getUser(), dataBase.getPassword(), dataBase.getSql(), dataBase.getType(),
                        System.currentTimeMillis());
            case SqliteFactroy.TB_UP_CHAIN_RECORD:
                Record record;
                if (data instanceof Record) {
                    record = (Record) data;
                } else {
                    record = new Record();
                }
                return String.format(" %d , %d , %d, %d ", record.getId(),
                        record.getConnectorIdRaw(), record.getUpchain(), System.currentTimeMillis());
            default:
                return "";

        }
    }

    public static void main(String[] args) {

        String format = String.format("目前时间：%tc", new Date());

        System.out.println(format);

        System.out.println(String.format("当前时间: %tc", new Date()));

    }
}
