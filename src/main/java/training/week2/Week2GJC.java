package training.week2;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Week2GJC implements Week2Worker {

    @AllArgsConstructor
    @Data
    static class StationPair {
        private Station from;
        private Station to;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof StationPair) {
                StationPair pair = (StationPair) obj;
                return (from == pair.from && to == pair.to) || (from == pair.to && to == pair.from);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return from.hashCode() + to.hashCode();
        }
    }

    private final static HashMap<StationPair, Integer> costTimeMap = new HashMap<StationPair, Integer>() {
        {
            put(new StationPair(Station.A1, Station.A2), 3);
            put(new StationPair(Station.A2, Station.A3), 4);
            put(new StationPair(Station.A3, Station.A4), 4);
            put(new StationPair(Station.A4, Station.A5), 5);
            put(new StationPair(Station.B1, Station.B2), 4);
            put(new StationPair(Station.B2, Station.B3), 3);
            put(new StationPair(Station.B3, Station.B4), 3);
            put(new StationPair(Station.B4, Station.B5), 4);
            put(new StationPair(Station.B5, Station.B6), 3);
            put(new StationPair(Station.C1, Station.C2), 5);
            put(new StationPair(Station.C2, Station.C3), 4);
            put(new StationPair(Station.C3, Station.C4), 3);
            put(new StationPair(Station.C4, Station.C5), 3);
            put(new StationPair(Station.C5, Station.C6), 4);
            put(new StationPair(Station.C6, Station.C7), 4);
            put(new StationPair(Station.C7, Station.C8), 3);
            put(new StationPair(Station.A2, Station.C3), 3);
            put(new StationPair(Station.A4, Station.B2), 3);
            put(new StationPair(Station.B4, Station.C5), 5);
        }
    };

    private final static HashMap<Station, List<Station>> nextStationMap = new HashMap<Station, List<Station>>() {
        {
            for (Station from : Station.values()) {
                for (Station to : Station.values()) {
                    if (costTimeMap.containsKey(new StationPair(from, to))) {
                        List<Station> stations;
                        if (get(from) == null) {
                            stations = new ArrayList<>();
                        } else {
                            stations = get(from);
                        }
                        stations.add(to);
                        put(from, stations);
                    }
                }
            }
        }
    };

    static private List<Station> deepCopy(List<Station> source) {
        List<Station> target = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(source)) {
            target.addAll(source);
        }
        return target;
    }

    public static void main(String[] args) {
        Week2GJC w = new Week2GJC();

        Route route = w.computeRoute(Station.B4, Station.A2);
        System.out.println(route.getCostMinutes());
        System.out.println(route.getStations());
    }

    @Override
    public Route computeRoute(Station begin, Station end) {
        List<List<Station>> arrivedRoutes = new ArrayList<>();
        List<Station> route = new ArrayList<>();
        route.add(begin);
        Queue<List<Station>> candidateRoutesQueue = new LinkedList<>();
        candidateRoutesQueue.add(route);
        while (candidateRoutesQueue.size() > 0) {
            route = candidateRoutesQueue.poll();
            if (arrived(route, end)) {
                arrivedRoutes.add(route);
            } else {
                List<List<Station>> newRoutes = getNextRoutes(route);
                if (CollectionUtils.isNotEmpty(newRoutes)) {
                    candidateRoutesQueue.addAll(newRoutes);
                }
            }
        }
        List<Station> fastRoute = arrivedRoutes.stream().min(Comparator.comparing(this::computeCostTime)).orElse(null);
        return new Route(fastRoute, computeCostTime(fastRoute));
    }

    private int computeCostTime(List<Station> route) {
        int time = 0;
        if (CollectionUtils.isNotEmpty(route) && route.size() > 1) {
            for (int i = 0; i < route.size() - 1; i++) {
                time += costTimeMap.get(new StationPair(route.get(i), route.get(i + 1)));
            }
        }
        return time;
    }

    private boolean arrived(List<Station> route, Station end) {
        return CollectionUtils.isNotEmpty(route) && route.get(route.size() - 1) == end;
    }

    private List<List<Station>> getNextRoutes(List<Station> route) {
        List<Station> nextStations = findNextStations(route.get(route.size() - 1), route);
        List<List<Station>> newRoutes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(nextStations)) {
            for (Station nextStation : nextStations) {
                List<Station> newRoute = deepCopy(route);
                newRoute.add(nextStation);
                newRoutes.add(newRoute);
            }
        }
        return newRoutes;
    }

    private static List<Station> findNextStations(Station now, List<Station> stations) {
        List<Station> nextStations = nextStationMap.get(now);
        Set<Station> existingSet = new HashSet<>(stations);
        return nextStations.stream().filter(a -> !existingSet.contains(a)).collect(Collectors.toList());
    }

}
