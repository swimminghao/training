package training.week13.fz.nodes;

import com.wayue.olympus.common.TreeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import training.week13.fz.CompositeNode;
import training.week13.fz.SchemaNode;
import training.week13.fz.SchemaScanException;

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
public abstract class AbstractNode implements SchemaNode {
	private final String name, type;
	@ToString.Exclude
	@Setter
	private SchemaNode parent;

	public static SchemaNode merge(SchemaNode left, SchemaNode right) {
		if (right == null) {
			return left;
		} else if (left == null) {
			return right;
		} else if (!left.getName().equals(right.getName())) {
			throw new SchemaScanException(String.format("name not match: '%s' vs. '%s'", left.getName(), right.getName()));
		} else if (right instanceof NullNode) {
			return left;
		} else if (left instanceof NullNode) {
			return right;
		} else if (!left.getClass().equals(right.getClass())) {
			String lc = left.getClass().getSimpleName(), rc = right.getClass().getSimpleName();
			throw new SchemaScanException(String.format("type not match at [%s]: '%s' vs. '%s'", left.getName(), lc, rc));
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
			throw new SchemaScanException("unknown type: " + left.getType());
		}
		return left;
	}

	@Override
	public List<String> getPath() {
		LinkedList<String> path = new LinkedList<>();
		for (SchemaNode node = this; node != null; node = node.getParent()) {
			path.push(node.getName());
		}
		return path;
	}

	@Override
	public SchemaNode merge(SchemaNode node) {
		return merge(this, node);
	}

	@Override
	public String prettyPrint() {
		Function<SchemaNode, List<String>> texts = n -> Stream.concat(
				Stream.of(n.toString().replaceAll(".*\\(|\\)", "").split(", ")),
				Stream.of("")
		).collect(Collectors.toList());
//		Function<SchemaNode, List<String>> texts = n -> Arrays.asList(n.toString(), "");
		Function<SchemaNode, List<SchemaNode>> children = n -> n instanceof CompositeNode ?
				((CompositeNode) n).getChildren().stream().sorted(Comparator.comparing(SchemaNode::getName)).collect(Collectors.toList())
				: Collections.emptyList();
		return TreeUtil.prettyPrintTree(this, children, texts);
	}
}