package training.week14.gujc.table;

import training.week13.gujc.BaseNode;
import training.week13.gujc.ComplexNode;

import java.util.List;

public class Processor {

    public static void build(BaseNode root, ModelTable parent) {
        if (root.getName().contains("tags")) {
            System.out.println("tags");
        }
        if (root instanceof ComplexNode) {
            ModelTable mt = new ModelTable(root, parent);
            System.out.println(mt.getCreateSql());
            System.out.println(mt.getInsertSql());
            List<BaseNode> list = ((ComplexNode) root).getChildren();
            for (BaseNode baseNode : list) {
                build(baseNode, mt);
            }
        } else {
            ModelTable mt = new ModelTable(root, parent);
            System.out.println(mt.getInsertSql());
        }
    }

}
