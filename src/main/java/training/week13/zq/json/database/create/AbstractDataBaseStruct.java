package training.week13.zq.json.database.create;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Triple;
import training.week13.zq.json.database.BaseField;
import training.week13.zq.json.database.DataStructNode;
import training.week13.zq.json.parse.ChildNode;
import training.week13.zq.json.parse.JsonNode;
import training.week13.zq.json.parse.node.NumberJsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * AbstractDataBaseStruct
 *
 * @Author: zhuqiang
 * @Date: 2020/1/4
 */
public abstract class AbstractDataBaseStruct implements DataBaseStruct {
    @Getter
    private DataStructNode dataStructNode;

    @Override
    public void buildStructNode(JsonNode node) {
        this.dataStructNode = processJsonNode(structNodeCreate(), (ChildNode) node, String.format(getDataName(), node.getName()));
    }

    /**
     * 装饰node
     *
     * @param node
     * @return
     */
    public DataStructNode decoratorStructNode(DataStructNode node) {
        return node;
    }

    /**
     * 数据结构节点创建
     *
     * @return
     */
    public DataStructNode structNodeCreate() {
        return new DataStructNode();
    }

    /**
     * 数据结构节点创建
     *
     * @return
     */
    public BaseField fieldCreate(String fieldName, List<Triple<String, String,Object>> values, String fieldType) {
        return new BaseField(fieldName, values, fieldType);
    }


    /**
     * 起始 结构名
     *
     * @return
     */
    public abstract String getDataName();

    /**
     * 将jsonNode结构转为 数据库结构创建结构
     *
     * @param objectJsonNode
     * @param parentTableName
     * @return
     */
    private DataStructNode processJsonNode(DataStructNode startDataStructNode, ChildNode childNode, String parentTableName) {
        startDataStructNode.setTableName(parentTableName +((JsonNode)childNode).getName()+ "." );
        List<JsonNode> childNodes = childNode.queryChildNodes();
        List<BaseField> baseFields = new ArrayList<>();
        for (JsonNode node : childNodes) {
            if (null == node) {
                continue;
            }
            if (!(node instanceof ChildNode)) {
                if (node instanceof NumberJsonNode) {
                    baseFields.add(
                            fieldCreate(node.getName(), node.getValueList(),( (NumberJsonNode)node).getNumberType())
                    );
                } else {
                    baseFields.add(
                            fieldCreate(node.getName(), node.getValueList(), node.getType())
                    );
                }
            } else {
                startDataStructNode.setChildNodes(processJsonNode(startDataStructNode.createDataStructNode(), (ChildNode) node, startDataStructNode.getTableName()));
            }
        }
        startDataStructNode.setFields(baseFields);
        decoratorStructNode(startDataStructNode);
        return startDataStructNode;
    }




    /*private DataStructNode processObjectJsonNode(DataStructNode startDataStructNode, ObjectJsonNode objectJsonNode, String parentTableName) {
        startDataStructNode.setTableName(parentTableName + "." + objectJsonNode.getName());
        Map<String, JsonNode> childNode = objectJsonNode.getChildNode();
        List<BaseField> baseFields = new ArrayList<>();
        for (Map.Entry<String, JsonNode> stringJsonNodeEntry : childNode.entrySet()) {
            if (null == stringJsonNodeEntry.getValue()) {
                continue;
            }
            if (!(stringJsonNodeEntry.getValue() instanceof ChildNode)) {
                if (stringJsonNodeEntry.getValue() instanceof NumberJsonNode) {
                    baseFields.add(
                            fieldCreate(stringJsonNodeEntry.getKey(), stringJsonNodeEntry.getValue().getValues(),( (NumberJsonNode)stringJsonNodeEntry.getValue()).getNumberType())
                    );
                } else {
                    baseFields.add(
                            fieldCreate(stringJsonNodeEntry.getKey(), stringJsonNodeEntry.getValue().getValues(), stringJsonNodeEntry.getValue().getType())
                    );
                }
            } else if (stringJsonNodeEntry.getValue() instanceof ObjectJsonNode) {
                startDataStructNode.setChildNodes(processObjectJsonNode(startDataStructNode.createDataStructNode(), (ObjectJsonNode) stringJsonNodeEntry.getValue(), startDataStructNode.getTableName()));
            } else if (stringJsonNodeEntry.getValue() instanceof ArrayJsonNode) {
                startDataStructNode.setChildNodes(processArrayJsonNode(startDataStructNode.createDataStructNode(), (ArrayJsonNode) stringJsonNodeEntry.getValue(), startDataStructNode.getTableName()));
            }
        }
        startDataStructNode.setFields(baseFields);
        decoratorStructNode(startDataStructNode);
        return startDataStructNode;
    }

    private DataStructNode processArrayJsonNode(DataStructNode startDataStructNode, ArrayJsonNode arrayJsonNode, String parentTableName) {
        startDataStructNode.setTableName(parentTableName + "." + arrayJsonNode.getName());
        List<JsonNode> childNode = arrayJsonNode.queryChildNodes();
        List<BaseField> baseFields = new ArrayList<>();
        for (JsonNode node : childNode) {
            if (null == node) {
                continue;
            }
            if (!(node instanceof ChildNode)) {
                if (node instanceof NumberJsonNode) {
                    baseFields.add(
                            fieldCreate(node.getName(), node.getValues(),( (NumberJsonNode)node).getNumberType())
                    );
                } else {
                    baseFields.add(
                            fieldCreate(node.getName(), node.getValues(), node.getType())
                    );
                }
            } else if (node instanceof ObjectJsonNode) {
                startDataStructNode.setChildNodes(processObjectJsonNode(startDataStructNode.createDataStructNode(), (ObjectJsonNode) node, startDataStructNode.getTableName()));
            } else if (node instanceof ArrayJsonNode) {
                startDataStructNode.setChildNodes(processArrayJsonNode(startDataStructNode.createDataStructNode(), (ArrayJsonNode) node, startDataStructNode.getTableName()));
            }
        }
        startDataStructNode.setFields(baseFields);
        decoratorStructNode(startDataStructNode);
        return startDataStructNode;
    }
*/

}
