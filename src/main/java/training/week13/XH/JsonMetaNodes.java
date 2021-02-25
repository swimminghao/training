package training.week13.XH;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Triple;
import training.week13.XH.nodes.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class JsonMetaNodes {
	private static String ARRAY_IDENTIFIER = "$";

	public static JsonMetaNode parse(String name, Iterable<? extends JSON> jsons) {
		JsonMetaNode jsonMetaNode = StreamSupport.stream(jsons.spliterator(), true)
				.map(o -> parse(name, o))
				.reduce(JsonMetaNode::merge)
				.orElse(null);
		return jsonMetaNode;
	}

	private static JsonMetaNode parse(String name, Object object) {
		class HoldingConsumer implements Consumer<JsonMetaNode> {
			@Getter private JsonMetaNode node;

			@Override
			public void accept(JsonMetaNode node) {
				this.node = node;
			}
		}
		HoldingConsumer consumer = new HoldingConsumer();
		LinkedList<Triple<String, Object, Consumer<JsonMetaNode>>> stack = new LinkedList<>();
		for (Triple<String, Object, Consumer<JsonMetaNode>> triple = Triple.of(name, object, consumer); triple != null; triple = stack.poll()) {
			String n = triple.getLeft();
			Object v = triple.getMiddle();
			JsonMetaNode node;
			if (v instanceof List) {
				node = new ArrayNode(n, ((List) v).size());
				for (Object c : ((List) v)) {
					stack.push(Triple.of(ARRAY_IDENTIFIER, c, ((CompositeNode) node)::addChild));
				}
			} else if (v instanceof Boolean) {
				node = new BooleanNode(n);
			} else if (v == null) {
				node = new NullNode(n);
			} else if (v instanceof Number) {
				node = new NumberNode(n, NumberNode.NumberType.fromValue((Number) v));
			} else if (v instanceof Map) {
				node = new ObjectNode(n);
				for (Map.Entry<?, ?> entry : ((Map<?, ?>) v).entrySet()) {
					stack.push(Triple.of((String) entry.getKey(), entry.getValue(), ((CompositeNode) node)::addChild));
				}
			} else if (v instanceof String) {
				node = new TextNode(n, ((String) v).length(), ((String) v).length());
			} else {
				throw new GeneralScanException("unknown type: " + v.getClass().getName());
			}
			triple.getRight().accept(node);
		}
		return consumer.getNode();
	}
}
