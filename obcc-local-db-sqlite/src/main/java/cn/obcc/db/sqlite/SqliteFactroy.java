package cn.obcc.db.sqlite;

import cn.obcc.db.sqlite.mapper.TableMapper;

import java.io.File;
import java.io.IOException;

/**
 * SqliteFactroy
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/20 9:42
 * @details
 */
public class SqliteFactroy {

    public static final String TB_CONNECTOR = "tr_connector";
    public static final String TB_UP_CHAIN_RECORD = "tr_up_record";

    private static SqliteHelper instance;

    public static SqliteHelper getInstance() {
        return instance;
    }


    private static void createDb(String dbName) throws IOException {
        File file = new File(dbName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private synchronized static SqliteHelper createSqliteHelper(String dbName) throws Exception {
        if (instance == null) {
            instance = new SqliteHelper(dbName);
        }
        return instance;
    }

    private static void createTables(TableMapper tableMapper) {
        if (tableMapper != null) {
            tableMapper.createTable(instance, TB_CONNECTOR, TB_UP_CHAIN_RECORD);
        }
    }

    public static void init(String dbName, TableMapper tableMapper) {
        try {
            createDb(dbName);
            createSqliteHelper(dbName);
            createTables(tableMapper);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
