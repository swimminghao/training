package training.week4;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Week4GJC implements Week4Worker {
    @Override
    public <T> List<Collection<T>> sort(List<Pair<T, T>> relations) {
        Set<T> waitedSet = relations.stream().map(Pair::getLeft).collect(Collectors.toSet());
        waitedSet.addAll(relations.stream().map(Pair::getRight).collect(Collectors.toSet()));
        Map<T, Set<T>> limitedMap = relations.stream()
                .collect(Collectors.groupingBy(Pair::getRight, Collectors.mapping(Pair::getLeft, Collectors.toSet())));

        List<Collection<T>> accessedSets = new ArrayList<>();
        Collection<T> accessedSet;
        while (!(accessedSet = access(waitedSet, limitedMap)).isEmpty()) {
            accessedSets.add(accessedSet);
        }
        return accessedSets;
    }

    private <T> Collection<T> access(Set<T> waitedSet, Map<T, Set<T>> limitedMap) {
        Set<T> accessedSet = new HashSet<>();
        for (T t : waitedSet) {
            if (limitedMap.get(t) == null) {
                accessedSet.add(t);
            }
        }
        for (T t : accessedSet) {
            waitedSet.remove(t);
        }
        for (Map.Entry<T, Set<T>> e : limitedMap.entrySet()) {
            if (e.getValue() != null) {
                e.setValue(e.getValue().stream().filter(t -> !accessedSet.contains(t)).collect(Collectors.toSet()));
                if (e.getValue().size() == 0) {
                    e.setValue(null);
                }
            }
        }
        return accessedSet;
    }
}
