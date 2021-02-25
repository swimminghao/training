package training.week13.zq.json.database.create.mysql;

import lombok.Data;
import lombok.experimental.Accessors;
import training.week13.zq.json.database.DataStructNode;

/**
 * Copyright (C), 2018-2019 善诊
 * MysqlDataStructNode
 *
 * @Author: zhuqiang
 * @Date: 2020/1/4
 */
@Data
@Accessors(chain = true)
public class MysqlDataStructNode extends DataStructNode {
    String createTableSql;
    String insertSql;

    @Override
    public MysqlDataStructNode createDataStructNode() {
        return new MysqlDataStructNode();
    }
}
