package training.week13.zq.json.database;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * DataStructNode
 *
 * @Author: zhuqiang
 * @Date: 2020/1/4
 */
@Data
@Accessors(chain = true)
public  class DataStructNode {
    private String tableName;
    private List<BaseField> fields;
    public void setChildNodes(DataStructNode childNode) {
        getChildNodes().add(childNode);
    }
    private List<DataStructNode> childNodes =new ArrayList<>();

    public      DataStructNode createDataStructNode(){
        return new DataStructNode();
    }



}

