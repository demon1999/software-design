package events_statistics;

import java.util.Map;

public interface EventsStatistics {
    void incEvent(String name);
    void printStatistics();
    Double getEventStatisticByName(String name);
    Map<String, Double> getAllEventStatistic();
}
