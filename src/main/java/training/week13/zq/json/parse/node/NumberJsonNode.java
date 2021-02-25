package training.week13.zq.json.parse.node;

import lombok.Getter;
import lombok.ToString;
import training.week13.zq.json.parse.AbstractJsonNode;

/**
 * Copyright (C), 2018-2019 善诊
 * ArrayJsonNode
 *
 * @Author: zhuqiang
 * @Date: 2019-12-30
 */
@ToString(callSuper = true)
public class NumberJsonNode extends AbstractJsonNode {
    @Getter
    private String numberType;

    public NumberJsonNode(String name, String type) {
        super(name, "Number");
        this.numberType=type;
    }
}
