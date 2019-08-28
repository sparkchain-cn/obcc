//package cn.obcc.db.sqlite.mapper.entity;
//
//import cn.obcc.db.sqlite.mapper.BaseMapper;
//import com.sparkchain.taurus.chain.vo.RecordVo;
//import com.sparkchain.taurus.sqlite.SqliteFactroy;
//import com.sparkchain.taurus.sqlite.vo.Record;
//import org.springframework.stereotype.Service;
//
//import java.io.UnsupportedEncodingException;
//import java.sql.SQLException;
//
///**
// * RecordMapper
// *
// * @author ecasona
// * @version 1.4
// * @date 2019/8/20 16:16
// * @details
// */
//@Service
//public class RecordMapper extends BaseMapper<RecordVo> {
//
//    public static void updateResultState(RecordVo recordVo, int resultState) {
//        try {
//            SqliteFactroy.getInstance().update(String.format(" update %s set result = %d where id = %s ",
//                    SqliteFactroy.TB_UP_CHAIN_RECORD, resultState, recordVo.getId()));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void updateResult(RecordVo recordVo) {
//        try {
//            SqliteFactroy.getInstance().update(String.format(" update %s set result = %d,lastUpChainPath='%s',lastCheckTime=%d where id = %s ",
//                    SqliteFactroy.TB_UP_CHAIN_RECORD, recordVo.getResult(), recordVo.getLastUpChainPath(), recordVo.getLastCheckTimeRaw(), recordVo.getId()));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void updateUpchainStateAndHash(Record record, int upchainState) {
//        try {
//            SqliteFactroy.getInstance().update(String.format(" update %s set upchain = %d, hash='%s' where id = %d ",
//                    getTableName(), upchainState, record.getHash(), record.getId()));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public String getTableName() {
//        return SqliteFactroy.TB_UP_CHAIN_RECORD;
//    }
//}
