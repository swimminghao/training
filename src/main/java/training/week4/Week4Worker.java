package training.week4;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;

//https://en.wikipedia.org/wiki/Directed_acyclic_graph
public interface Week4Worker {
	<T> List<Collection<T>> sort(List<Pair<T, T>> relations);// right depend on left
}
