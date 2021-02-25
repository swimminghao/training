package training.week13.zq.json.parse;

import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * JsonNode
 *
 * @Author: zhuqiang
 * @Date: 2019-12-29
 */
public interface ChildNode {

   /**
    * 获取子节点
    * @return
    */
   List<JsonNode> queryChildNodes();

   /**
    * 添加子节点
    * @param childNode
    */
   void addChild(JsonNode childNode);

   String getId();
   String getPid();

}
