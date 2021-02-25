package training.week13.hongxing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wayue.olympus.common.TreeUtil;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonNodeParser {
	private static final String ROOT_NODE_NAME = "root";
	private static final String ARRAY_CHILD_NODE_NAME = "element";
	private static final EmptyNode EMPTY_ARRAY_CHILD_NODE = new EmptyNode(ARRAY_CHILD_NODE_NAME);

	interface Node {
		String getName();

		List<Node> getChildren();

		void setChildren(List<Node> children);

		void addChild(Node child);

		NodeType getType();

		Node merge(Node other);
	}

	@RequiredArgsConstructor
	@Data
	public static abstract class AbstractNode implements Node {
		private final String name;
		protected List<Node> children = new ArrayList<>();

		protected abstract boolean isLeaf();

		@Override
		public final Node merge(Node other) {
			if (!NodeType.empty.equals(other.getType())) {
				return doMerge(other);
			}
			return this;
		}

		protected abstract Node doMerge(Node other);

		protected final void typeCheck(Node other, String operateName) {
			if (!getType().equals(other.getType())) {
				throw new UnsupportedOperationException(String.format("[%s] is not supported for NodeType [%s] - [%s]", operateName, getType(), other.getType()));
			}
		}

		@Override
		public void addChild(Node child) {
			if (isLeaf()) {
				throw new UnsupportedOperationException(String.format("unsupported operation for NodeType [%s]", this.getType()));
			}
			if (children.stream().anyMatch(a -> a.getName().equals(child.getName()))) {
				throw new RuntimeException(String.format("duplicate key [%s] in Node [%s]", child.getName(), this.toString()));
			}
			children.add(child);
		}
	}

	public static class ObjectNode extends AbstractNode {
		@Getter private NodeType type = NodeType.object;
		@Getter private boolean isLeaf = false;

		public ObjectNode(String name) {
			super(name);
		}

		@Override
		public Node doMerge(Node other) {
			typeCheck(other, "merge");
			Map<String, Node> nodeMap = getChildren().stream().collect(Collectors.toMap(Node::getName, Function.identity()));
			for (Node otherChild : other.getChildren()) {
				Node child = nodeMap.get(otherChild.getName());
				if (child == null) {
					nodeMap.put(otherChild.getName(), otherChild);
				} else {
					nodeMap.put(otherChild.getName(), child.merge(otherChild));
				}
			}
			setChildren(new ArrayList<>(nodeMap.values()));
			return this;
		}
	}

	public static class NumberNode extends AbstractNode {
		@Getter private NodeType type = NodeType.number;
		@Getter private boolean isLeaf = true;

		public NumberNode(String name) {
			super(name);
		}

		@Override
		protected Node doMerge(Node other) {
			if (NodeType.integer.equals(other.getType())) {
				return this;
			}
			typeCheck(other, "merge");
			return this;
		}
	}

	public static class StringNode extends AbstractNode {
		@Getter private NodeType type = NodeType.string;
		@Getter private boolean isLeaf = true;

		public StringNode(String name) {
			super(name);
		}

		@Override
		protected Node doMerge(Node other) {
			typeCheck(other, "merge");
			return this;
		}
	}

	public static class EmptyNode extends AbstractNode {
		@Getter private NodeType type = NodeType.empty;
		@Getter private boolean isLeaf = true;

		public EmptyNode(String name) {
			super(name);
		}

		@Override
		protected Node doMerge(Node other) {
			return other;
		}
	}

	public static class ArrayNode extends AbstractNode {
		@Getter private NodeType type = NodeType.array;
		@Getter private boolean isLeaf = false;

		public ArrayNode(String name) {
			super(name);
			addChild(EMPTY_ARRAY_CHILD_NODE);
		}

		@Override
		protected Node doMerge(Node other) {
			typeCheck(other, "merge");
			Node otherChild = other.getChildren().get(0);
			Node merge = getChildren().get(0).merge(otherChild);
			setChildren(Collections.singletonList(merge));
			return this;
		}

		@Override
		public void addChild(Node child) {
			List<Node> children = getChildren();
			if (CollectionUtils.isEmpty(children)) {
				super.addChild(child);
			}
			setChildren(Collections.singletonList(getChildren().get(0).merge(child)));
		}
	}

	public static class BoolNode extends AbstractNode {
		@Getter private NodeType type = NodeType.bool;
		@Getter private boolean isLeaf = true;

		public BoolNode(String name) {
			super(name);
		}

		@Override
		protected Node doMerge(Node other) {
			typeCheck(other, "merge");
			return this;
		}
	}

	public static class IntegerNode extends AbstractNode {
		@Getter private NodeType type = NodeType.integer;
		@Getter private boolean isLeaf = true;

		public IntegerNode(String name) {
			super(name);
		}

		@Override
		protected Node doMerge(Node other) {
			if (NodeType.number.equals(other.getType())) {
				return other;
			}
			typeCheck(other, "merge");
			return this;
		}
	}

	public enum NodeType {
		object,
		number,
		string,
		array,
		bool,
		integer,
		empty,
	}

	public static Node parse(JSONObject object) {
		ObjectNode root = new ObjectNode(ROOT_NODE_NAME);
		Deque<Triple<String, Object, ? extends Node>> stack = object.entrySet().stream()
				.map(a -> Triple.of(a.getKey(), a.getValue(), root)).collect(Collectors.toCollection(LinkedList::new));
		for (; !stack.isEmpty(); ) {
			Triple<String, Object, ? extends Node> triple = stack.poll();
			String key = triple.getLeft();
			Object value = triple.getMiddle();
			Node current = triple.getRight();
			if (value == null) {
				current.addChild(new EmptyNode(key));
			} else if (value instanceof CharSequence) {
				current.addChild(new StringNode(key));
			} else if (value instanceof Integer || value instanceof Long) {
				current.addChild(new IntegerNode(key));
			} else if (value instanceof Boolean) {
				current.addChild(new BoolNode(key));
			} else if (value instanceof BigDecimal) {
				current.addChild(new NumberNode(key));
			} else if (value instanceof JSONArray) {
				ArrayNode child = new ArrayNode(key);
				current.addChild(child);
				((JSONArray) value).stream().map(e -> Triple.of(ARRAY_CHILD_NODE_NAME, e, child)).forEach(stack::push);
			} else if (value instanceof JSONObject) {
				ObjectNode child = new ObjectNode(key);
				current.addChild(child);
				((JSONObject) value).entrySet().stream().map(entry -> Triple.of(entry.getKey(), entry.getValue(), child)).forEach(stack::push);
			} else {
				throw new RuntimeException(String.format("invalid value class in JSONObject : [%s] class : [%s]", value, value.getClass()));
			}
		}
		return root;
	}

	public static void main(String[] args) {
		Function<Node, String> treePrinter = node -> TreeUtil.prettyPrintTree(node, Node::getChildren, n -> {
			if (n.getType().equals(NodeType.array)) {
				return Arrays.asList(n.getName(), String.format("*%s[%s]", n.getType().name(), n.getChildren().get(0).getType()), "");
			}
			return Arrays.asList(n.getName(), "*" + n.getType().name(), "");
		});
		try (InputStream is = Resources.class.getResourceAsStream("/week13/json-20191225.txt")) {
			Node merged = null;
			for (String json : IOUtils.readLines(is, StandardCharsets.UTF_8)) {
				Node node = parse(JSON.parseObject(json));
				merged = merged == null ? node : merged.merge(node);
			}
			System.out.println(treePrinter.apply(merged));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
