package events_statistics;

import clock.Clock;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClockEventsStatistic implements EventsStatistics {
    private class TimePair {
        Instant time;
        String name;
        TimePair(Instant time, String name) {
            this.time = time;
            this.name = name;
        }
    }

    private Clock clock;
    private Deque<TimePair> queue;
    private Map<String, Long> events;
    private final long minutesInHour = TimeUnit.HOURS.toMinutes(1);
    public final static long secondsInHour = TimeUnit.HOURS.toSeconds(1);

    public ClockEventsStatistic(Clock clock) {
        this.clock = clock;
        this.queue = new ArrayDeque<>();
        events = new HashMap<>();
    }

    private double fromRPHtoRPM(Long k) {
        return (double)k / (double)minutesInHour;
    }
    private void popUnRecent(Instant now) {
        if (!queue.isEmpty() && now.getEpochSecond() < queue.getLast().time.getEpochSecond()) {
            throw new IllegalArgumentException("Wrong clock: time shouldn't move back");
        }
        while(!queue.isEmpty()) {
            TimePair front = queue.getFirst();
            if (now.getEpochSecond() - front.time.getEpochSecond() >= secondsInHour) {
                Long num = events.get(front.name);
                events.remove(front.name);
                num--;
                if (num > 0)
                    events.put(front.name, num);
                queue.removeFirst();
            } else {
                break;
            }
        }
    }

    @Override
    public void incEvent(String name) throws IllegalArgumentException {
        Instant now = clock.now();
        popUnRecent(now);
        queue.addLast(new TimePair(now, name));
        Long c = (long) 0;
        if (events.containsKey(name)) {
            c = events.get(name);
        }
        c++;
        events.remove(name);
        events.put(name, c);
    }

    @Override
    public void printStatistics() throws IllegalArgumentException {
        System.out.println(getAllEventStatistic());
    }

    @Override
    public Double getEventStatisticByName(String name) throws IllegalArgumentException {
        Instant now = clock.now();
        popUnRecent(now);
        if (events.containsKey(name))
            return fromRPHtoRPM(events.get(name));
        return 0.0;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() throws IllegalArgumentException {
        Instant now = clock.now();
        popUnRecent(now);
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Long> entry : events.entrySet()) {
            result.put(entry.getKey(), fromRPHtoRPM(entry.getValue()));
        }
        return result;
    }
}
