package training.week13.XH.nodes;

import com.wayue.olympus.common.TreeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import training.week13.XH.CompositeNode;
import training.week13.XH.JsonMetaNode;
import training.week13.XH.GeneralScanException;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
@ToString
public abstract class AbstractNode implements JsonMetaNode {
	private final String name;
	private final String type;
	@ToString.Exclude
	@Setter
	private JsonMetaNode parent;

	public static JsonMetaNode merge(JsonMetaNode left, JsonMetaNode right) {
		if (right == null) {
			return left;
		} else if (left == null) {
			return right;
		} else if (!left.getName().equals(right.getName())) {
			throw new GeneralScanException(String.format("name not match: '%s' vs. '%s'", left.getName(), right.getName()));
		} else if (right instanceof NullNode) {
			return left;
		} else if (left instanceof NullNode) {
			return right;
		} else if (!left.getClass().equals(right.getClass())) {
			String lc = left.getClass().getSimpleName();
			String rc = right.getClass().getSimpleName();
			throw new GeneralScanException(String.format("type not match at [%s]: '%s' vs. '%s'", left.getName(), lc, rc));
		} else if (left instanceof BooleanNode) {
		} else if (left instanceof ArrayNode) {
			ArrayNode l = (ArrayNode) left, r = (ArrayNode) right;
			l.addChild(r.getChild());
			l.maxSize = Math.max(l.maxSize, r.maxSize);
		} else if (left instanceof NumberNode) {
			NumberNode l = (NumberNode) left, r = (NumberNode) right;
			l.numberType = l.numberType.merge(r.numberType);
		} else if (left instanceof ObjectNode) {
			ObjectNode l = (ObjectNode) left, r = (ObjectNode) right;
			r.children.values().forEach(l::addChild);
		} else if (left instanceof TextNode) {
			TextNode l = (TextNode) left, r = (TextNode) right;
			l.maxLength = Math.max(l.maxLength, r.maxLength);
			l.minLength = Math.min(l.minLength, r.minLength);
		} else {
			throw new GeneralScanException("unknown type: " + left.getType());
		}
		return left;
	}

	@Override
	public List<String> getPath() {
		LinkedList<String> path = new LinkedList<>();
		for (JsonMetaNode node = this; node != null; node = node.getParent()) {
			path.push(node.getName());
		}
		return path;
	}

	@Override
	public JsonMetaNode merge(JsonMetaNode node) {
		return merge(this, node);
	}

	@Override
	public String prettyPrint() {
		Function<JsonMetaNode, List<String>> texts = n -> Stream.concat(
				Stream.of(n.toString().replaceAll(".*\\(|\\)", "").split(", ")),
				Stream.of("")
		).collect(Collectors.toList());
//		Function<SchemaNode, List<String>> texts = n -> Arrays.asList(n.toString(), "");
		Function<JsonMetaNode, List<JsonMetaNode>> children = n -> n instanceof CompositeNode ?
				((CompositeNode) n).getChildren().stream().sorted(Comparator.comparing(JsonMetaNode::getName)).collect(Collectors.toList())
				: Collections.emptyList();
		return TreeUtil.prettyPrintTree(this, children, texts);
	}
}