package training.week13.hongxing.node;

import com.alibaba.fastjson.JSON;
import com.wayue.olympus.common.TreeUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Nodes {
	public static Node parseNode(String rootName, String arrayElementName, Iterable<JSON> jsons) {
		return StreamSupport.stream(jsons.spliterator(), false)
				.map(json -> parseNode(rootName, arrayElementName, json))
				.reduce(Node::merge)
				.orElse(null);
	}

	private static Node parseNode(String rootName, String arrayElementName, Object object) {
		Node root = null;
		Deque<Triple<String, Object, Node>> stack = new LinkedList<>();
		List<Pair<Node, Node>> releations = new ArrayList<>();
		for (Triple<String, Object, Node> triple = Triple.of(rootName, object, null); triple != null; triple = stack.poll()) {
			String key = triple.getLeft();
			Object value = triple.getMiddle();
			Node parent = triple.getRight();
			Node node;
			if (value == null) {
				node = new EmptyNode(key);
			} else if (value instanceof CharSequence) {
				node = new StringNode(key, ((CharSequence) value).length());
			} else if (value instanceof Integer || value instanceof Long) {
				node = new IntegerNode(key);
			} else if (value instanceof Boolean) {
				node = new BoolNode(key);
			} else if (value instanceof Number) {
				node = new NumberNode(key);
			} else if (value instanceof List) {
				node = new ArrayNode(key, arrayElementName, ((List) value).size());
				for (Object v : ((List) value)) {
					stack.push(Triple.of(arrayElementName, v, node));
				}
			} else if (value instanceof Map) {
				node = new ObjectNode(key);
				for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
					stack.push(Triple.of((String) entry.getKey(), entry.getValue(), node));
				}
			} else {
				throw new IllegalArgumentException(String.format("illegal value type : [%s] class : [%s]", value, value.getClass()));
			}
			if (root == null) {
				root = node;
			} else {
				releations.add(Pair.of(parent, node));
			}
		}
		for (int i = releations.size() - 1; i >= 0; i--) {
			Pair<Node, Node> pair = releations.get(i);
			pair.getLeft().addChild(pair.getRight());
		}
		return root;
	}

	public static void main(String[] args) {
		Function<Node, String> treePrinter = node -> TreeUtil.prettyPrintTree(node, Node::getChildren, n -> {
			if (n.getType().equals(Node.NodeType.array)) {
				return Arrays.asList(n.getName(), String.format("*%s[%s]", n.getType().name(), n.getChildren().get(0).getType()), "");
			}
			return Arrays.asList(n.getName(), "*" + n.getType().name());
		});
		try (InputStream is = Resources.class.getResourceAsStream("/algorithm/corpwork/week13/json-20191225.txt")) {
			List<JSON> jsons = IOUtils.readLines(is, "UTF-8").stream().map(JSON::parseObject).map(JSON.class::cast).collect(Collectors.toList());
			Node node = parseNode("root", "$", jsons);
			System.out.println(treePrinter.apply(node));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
