package training.week13.zq.json.parse.node;

import lombok.Getter;
import lombok.ToString;
import training.week13.zq.json.parse.AbstractJsonNode;
import training.week13.zq.json.parse.ChildNode;
import training.week13.zq.json.parse.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2019 善诊
 * ArrayJsonNode
 *
 * @Author: zhuqiang
 * @Date: 2019-12-30
 */
@ToString(callSuper = true)
public class ObjectJsonNode extends AbstractJsonNode implements ChildNode {

    @Getter
    private String id;
    @Getter
    private String pid;
    @Getter
    private Map<String, JsonNode> childNode = new HashMap<>();

    public ObjectJsonNode(String name,  String id, String pid) {
        super(name, "Object");
        this.id = id;
        this.pid = pid;
    }

    public ObjectJsonNode(String name) {
        super(name,"Object");
    }

    @Override
    public List<JsonNode> queryChildNodes() {
        return new ArrayList<>(childNode.values());
    }

    @Override
    public void addChild(JsonNode childNode) {
        if (null==childNode){
            return;
        }
     this.childNode.put(childNode.getName(),merge(this.childNode.get(childNode.getName()),childNode));
    }
}
