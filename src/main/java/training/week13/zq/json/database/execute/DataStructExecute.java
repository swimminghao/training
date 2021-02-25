package training.week13.zq.json.database.execute;

import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * DataStructExexcute
 *
 * @Author: zhuqiang
 * @Date: 2020/1/4
 */
public interface DataStructExecute {
    /**
     * 结构创建
     * @param s
     */
    void createStruct(String s);

    /**
     * 数据新增
     * @param values
     */
    void dataInsert(List<String> values);
}
