package training.week13.zq.json.parse;

import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * JsonNode
 *
 * @Author: zhuqiang
 * @Date: 2019-12-29
 */
public interface JsonNode {

    /**
     * 合并节点
     * @param left
     * @param right
     * @return
     */
    JsonNode merge(JsonNode left);

    /**
     * 获取key 名字
     * @return
     */
    String getName();

    /**
     * 获取key 类型
     * @return
     */
    String getType();

    /**
     * 打印
     * @return
     */
     String prettyPrint();

    /**
     * 获取key 类型
     * @return
     */
    void setType(String s);



    List<Triple<String,String,Object>> getValueList();


    void addValueList(String id, String pid, Object o);
}
