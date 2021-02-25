package training.week13.zq.json.database.execute;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * MysqlDataStructExecute
 *
 * @Author: zhuqiang
 * @Date: 2020/1/4
 */
@Slf4j
public class MysqlDataStructExecute extends AbstractDataStructExecute {
    public static final String URL = "jdbc:mysql://localhost:3306/zq";
    public static final String USER = "liulx";
    public static final String PASSWORD = "123456";


    @Override
    public void createStruct(String s) {
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2. 获得数据库连接
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            //3.操作数据库，实现增删改查
            Statement stmt = conn.createStatement();
            stmt.execute(s);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException e{}", e);
        } catch (SQLException e) {
            log.error("SQLException e{}", e);
        }
    }

    @Override
    public void dataInsert(List<String> values) {

    }
}
