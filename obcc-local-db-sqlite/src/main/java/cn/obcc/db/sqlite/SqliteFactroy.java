package cn.obcc.db.sqlite;

import cn.obcc.db.sqlite.mapper.TableMapper;

import java.io.File;

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

    public synchronized static SqliteHelper getInstance(String name) throws Exception {
        if (instance == null) {
            instance = new SqliteHelper(name);
        }
        return instance;
    }

    public static void init(String dbName, TableMapper tableMapper) {
        try {
            File file = new File(dbName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            getInstance(dbName);

            if (tableMapper != null) {
                tableMapper.createTable(instance, TB_CONNECTOR, TB_UP_CHAIN_RECORD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SqliteHelper getInstance() {
        return instance;
    }
}
