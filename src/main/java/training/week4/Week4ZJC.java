package training.week4;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class Week4ZJC implements Week4Worker {
	@Override
	public <T> List<Collection<T>> sort(List<Pair<T, T>> relations) {
		Map<T,List<T>> depen = new HashMap<>();
		if (relations.size()!=0) {
			for (Pair<T,T> tt : relations){
				depen.put(tt.getRight(),null);
			}
			for (Pair<T, T> t : relations) {
				List<T> values = new ArrayList<>();
				if (depen.get(t.getRight())==null) {
					values.add(t.getLeft());
					depen.put(t.getRight(), values);
				} else {
					depen.get(t.getRight()).add(t.getLeft());
				}
			}
		}
		relations.stream().map(Pair::getLeft).distinct().forEach(k -> depen.putIfAbsent(k, new ArrayList<>()));
		List<Collection<T>> sorted = new ArrayList<>();
		while (!depen.isEmpty()){
			List<T> empty = new ArrayList<>();
			for (Map.Entry<T, List<T>> entry : depen.entrySet()){
				if (entry.getValue().isEmpty())
					empty.add(entry.getKey());
			}
			if (empty==null)  throw new RuntimeException("无法执行");
			for (T t : empty) {
				depen.values().forEach(c -> c.remove(t));
				depen.remove(t);
			}
			sorted.add(empty);
		}
		return sorted;
	}
}
