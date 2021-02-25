package training.week13.zq.json.database;

import training.week13.zq.json.database.execute.DataStructExecute;
import training.week13.zq.json.database.execute.MysqlDataStructExecute;

/**
 * Copyright (C), 2018-2019 善诊
 * ExecuteUtils
 *
 * @Author: zhuqiang
 * @Date: 2020/1/6
 */
public class ExecuteUtils {
    public static void execute(String s) {
        DataStructExecute execute = new MysqlDataStructExecute();
        execute.createStruct(s);
    }
}
