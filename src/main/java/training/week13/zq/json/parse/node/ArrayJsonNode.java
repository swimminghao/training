package training.week13.zq.json.parse.node;

import lombok.Getter;
import lombok.ToString;
import training.week13.zq.json.parse.AbstractJsonNode;
import training.week13.zq.json.parse.ChildNode;
import training.week13.zq.json.parse.JsonNode;

import java.util.Collections;
import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * ArrayJsonNode
 *
 * @Author: zhuqiang
 * @Date: 2019-12-30
 */
@ToString(callSuper = true)
public class ArrayJsonNode extends AbstractJsonNode implements ChildNode {
    @Getter
    private String id;
    @Getter
    private String pid;
    @Getter
    private JsonNode child;

    public ArrayJsonNode(String name,  String id, String pid) {
        super(name, "Array");
        this.id = id;
        this.pid = pid;
    }

    public ArrayJsonNode(String name) {
        super(name,"Array");
    }

    @Override
    public List<JsonNode> queryChildNodes() {
        return Collections.singletonList(child);
    }

    @Override
    public void addChild(JsonNode childNode) {
        this.child=merge(this.child,childNode);
    }
}
