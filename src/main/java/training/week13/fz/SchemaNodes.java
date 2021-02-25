package training.week13.fz;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import training.week13.fz.nodes.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;

public class SchemaNodes {
	private static String ARRAY_IDENTIFIER = "$";

	public static SchemaNode parseIterative(String name, Iterable<? extends JSON> jsons) {
		return StreamSupport.stream(jsons.spliterator(), true)
				.map(o -> parseIterative(name, o))
				.reduce(SchemaNode::merge)
				.orElse(null);
	}

	private static SchemaNode parseIterative(String name, Object object) {
		@RequiredArgsConstructor
		class Holder {
			private final CompositeNode parent;
			@Getter private SchemaNode node;

			private void postOrder(BiConsumer<CompositeNode, SchemaNode> consumer) {
				consumer.accept(parent, node);
			}

			private void preOrder(SchemaNode node) {
				this.node = node;
			}
		}
		Holder holder = new Holder(null);
		LinkedList<Triple<String, Object, Holder>> stack = new LinkedList<>();
		for (Triple<String, Object, Holder> triple = Triple.of(name, object, holder); triple != null; triple = stack.poll()) {
			String n = triple.getLeft();
			Object o = triple.getMiddle();
			Holder h = triple.getRight();
			if (!stack.isEmpty() && stack.peek().getMiddle() != o) {
				h.postOrder(CompositeNode::addChild);
			} else {
				SchemaNode node;
				if (o instanceof List) {
					node = new ArrayNode(n, ((List) o).size());
					for (Object so : ((List) o)) {
						Holder sh = new Holder((CompositeNode) node);
						stack.push(Triple.of(ARRAY_IDENTIFIER, so, sh));
						stack.push(Triple.of(ARRAY_IDENTIFIER, so, sh));
					}
				} else if (o instanceof Boolean) {
					node = new BooleanNode(n);
				} else if (o == null) {
					node = new NullNode(n);
				} else if (o instanceof Number) {
					node = new NumberNode(n, NumberNode.NumberType.fromValue((Number) o));
				} else if (o instanceof Map) {
					node = new ObjectNode(n);
					for (Map.Entry<?, ?> entry : ((Map<?, ?>) o).entrySet()) {
						Holder sh = new Holder((CompositeNode) node);
						stack.push(Triple.of((String) entry.getKey(), entry.getValue(), sh));
						stack.push(Triple.of((String) entry.getKey(), entry.getValue(), sh));
					}
				} else if (o instanceof String) {
					node = new TextNode(n, ((String) o).length(), ((String) o).length());
				} else {
					throw new SchemaScanException("unknown type: " + o.getClass().getName());
				}
				h.preOrder(node);
			}
		}
		return holder.getNode();
	}

	private static SchemaNode parseRecursive(String n, Object o) {
		if (o instanceof List) {
			ArrayNode node = new ArrayNode(n, ((List) o).size());
			for (Object so : ((List) o)) {
				node.addChild(parseRecursive(ARRAY_IDENTIFIER, so));
			}
			return node;
		} else if (o instanceof Boolean) {
			return new BooleanNode(n);
		} else if (o == null) {
			return new NullNode(n);
		} else if (o instanceof Number) {
			return new NumberNode(n, NumberNode.NumberType.fromValue((Number) o));
		} else if (o instanceof Map) {
			ObjectNode node = new ObjectNode(n);
			for (Map.Entry<?, ?> entry : ((Map<?, ?>) o).entrySet()) {
				node.addChild(parseRecursive((String) entry.getKey(), entry.getValue()));
			}
			return node;
		} else if (o instanceof String) {
			return new TextNode(n, ((String) o).length(), ((String) o).length());
		} else {
			throw new SchemaScanException("unknown type: " + o.getClass().getName());
		}
	}
}
