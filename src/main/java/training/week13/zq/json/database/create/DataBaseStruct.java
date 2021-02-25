package training.week13.zq.json.database.create;

import training.week13.zq.json.database.DataStructNode;
import training.week13.zq.json.parse.JsonNode;

/**
 * Copyright (C), 2018-2019 善诊
 * DataBaseStruct
 *
 * @Author: zhuqiang
 * @Date: 2020/1/4
 */
public interface DataBaseStruct {
    /**
     *
     * 建表语句
     * @param node
     * @return
     */
    void buildStructNode(JsonNode node);

    /**
     * 获取数据结构节点
     * @return
     */
    DataStructNode getDataStructNode();

}
