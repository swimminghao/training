package training.week13.zq.json.parse;

import com.wayue.olympus.common.TreeUtil;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Triple;
import training.week13.zq.json.parse.node.ArrayJsonNode;
import training.week13.zq.json.parse.node.ObjectJsonNode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019 善诊
 * AbstractJsonNode
 *
 * @author zhuqiang
 * @date  2019-12-29
 */

@Slf4j
@ToString
@Accessors(chain = true)
public class AbstractJsonNode implements JsonNode {

    @Getter
    private final String name;

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Getter
    private  String type;
    @Getter
    //id pid value
    private List<Triple<String,String,Object>> valueList=new ArrayList<>();

    public void addPidMapPid(String id, String pid) {
        this.idMapPidList.computeIfAbsent(pid,a -> new ArrayList<>()).add(id);
    }

    @Getter
    private Map<String,List<String>> idMapPidList=new HashMap<>();



    public AbstractJsonNode(String name, String type) {
        this.name = name;
        this.type = type;

    }

    @Override
    public JsonNode merge(JsonNode node) {
       return merge(this,node);
    }

    public JsonNode merge(JsonNode left,JsonNode right) {
        if (null==left){
            return right;
        }else
        if (null==right){
            return left;
        }else
        if (!left.getName().equals(right.getName())||!left.getType().equals(right.getType())){
            log.error("merge name or type is do not match");
            left.setType(left.getType()+right.getType());
        }else
        if (left instanceof ArrayJsonNode){
            ArrayJsonNode l=(ArrayJsonNode)left;
            ArrayJsonNode r=(ArrayJsonNode)right;
            l.addChild(r.getChild());
        }else if (left instanceof ObjectJsonNode){
            ObjectJsonNode l=(ObjectJsonNode)left;
            ObjectJsonNode r=(ObjectJsonNode)right;
            r.queryChildNodes().forEach(l::addChild);
        }
        left.getValueList().addAll(right.getValueList());

        Set<Map.Entry<String, List<String>>> idMapList = ((AbstractJsonNode)right).getIdMapPidList().entrySet();
        for (Map.Entry<String, List<String>> listEntry : idMapList) {
            ((AbstractJsonNode)left).getIdMapPidList().computeIfAbsent(listEntry.getKey(),a->new ArrayList<>()).addAll(listEntry.getValue());
        }
        return left;
    }

    @Override
    public String prettyPrint() {
        Function<JsonNode, List<String>> texts = n ->n!=null? Arrays.asList(ArrayUtils.add(n.toString().replaceAll(".*\\(|\\)", "").split(", "), "")):Arrays.asList("nullNode");
        Function<JsonNode, List<JsonNode>> children = n -> n instanceof ChildNode ?
                ((ChildNode) n).queryChildNodes().stream().sorted(Comparator.comparing(JsonNode::getName))
                        .collect(Collectors.toList())
                : Collections.emptyList();
        return TreeUtil.prettyPrintTree(this, children, texts);
    }



    @Override
    public void addValueList(String id, String pid, Object o) {
        this.valueList.add(Triple.of(id,pid,o));
    }
}
