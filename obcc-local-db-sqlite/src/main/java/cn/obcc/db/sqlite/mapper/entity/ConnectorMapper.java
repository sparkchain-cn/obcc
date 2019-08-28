//package cn.obcc.db.sqlite.mapper.entity;
//
//import cn.obcc.db.sqlite.mapper.BaseMapper;
//import com.sparkchain.taurus.chain.vo.LvDataVo;
//import com.sparkchain.taurus.sqlite.SqliteFactroy;
//import com.sparkchain.taurus.sqlite.vo.DataBase;
//import org.springframework.stereotype.Component;
//
///**
// * ConnectorMapper
// *
// * @author ecasona
// * @version 1.4
// * @date 2019/8/20 14:03
// * @details
// */
//@Component
//public class ConnectorMapper extends BaseMapper<LvDataVo> {
//
//
//    public int insert(DataBase dataBase) {
//        return SqliteFactroy.getInstance().insert(getTableName(), dataBase);
//    }
//
//    @Override
//    public String getTableName() {
//        return SqliteFactroy.TB_CONNECTOR;
//    }
//}
