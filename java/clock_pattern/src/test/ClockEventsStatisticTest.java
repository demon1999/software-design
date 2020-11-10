package test;

import clock.SetableClock;
import events_statistics.ClockEventsStatistic;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Map;

public class ClockEventsStatisticTest {
    void putBasic(SetableClock clock, ClockEventsStatistic stats) {
        stats.incEvent("music");
        clock.setNow(Instant.ofEpochSecond(140));
        stats.incEvent("music2");
        clock.setNow(Instant.ofEpochSecond(180));
        stats.incEvent("music2");
        clock.setNow(Instant.ofEpochSecond(188));
        stats.incEvent("music1");
    }

    @Test
    public void testAllInHourByName() {
        SetableClock clock = new SetableClock(Instant.ofEpochSecond(100));
        ClockEventsStatistic stats = new ClockEventsStatistic(clock);
        putBasic(clock, stats);
        Assert.assertEquals(stats.getEventStatisticByName("music"), 1.0/60.0, 1e-6);
        Assert.assertEquals(stats.getEventStatisticByName("music1"), 1.0/60.0, 1e-6);
        Assert.assertEquals(stats.getEventStatisticByName("music2"), 2.0/60.0, 1e-6);
    }

    @Test
    public void testAllInHour() {
        SetableClock clock = new SetableClock(Instant.ofEpochSecond(100));
        ClockEventsStatistic stats = new ClockEventsStatistic(clock);
        putBasic(clock, stats);
        Map<String, Double> res = stats.getAllEventStatistic();
        Assert.assertEquals(res.get("music"), 1.0/60.0, 1e-5);
        Assert.assertEquals(res.get("music2"), 2.0/60.0, 1e-5);
        Assert.assertEquals(res.get("music1"), 1.0/60.0, 1e-5);
    }

    @Test
    public void testExpiredByName() {
        SetableClock clock = new SetableClock(Instant.ofEpochSecond(100));
        ClockEventsStatistic stats = new ClockEventsStatistic(clock);
        putBasic(clock, stats);
        clock.setNow(Instant.ofEpochSecond(100 + ClockEventsStatistic.secondsInHour));
        Assert.assertEquals(stats.getEventStatisticByName("music"), 0.0, 1e-6);
        Assert.assertEquals(stats.getEventStatisticByName("music1"), 1.0/60.0, 1e-6);
        Assert.assertEquals(stats.getEventStatisticByName("music2"), 2.0/60.0, 1e-6);
        clock.setNow(Instant.ofEpochSecond(140 + ClockEventsStatistic.secondsInHour));
        Assert.assertEquals(stats.getEventStatisticByName("music"), 0.0, 1e-6);
        Assert.assertEquals(stats.getEventStatisticByName("music1"), 1.0/60.0, 1e-6);
        Assert.assertEquals(stats.getEventStatisticByName("music2"), 1.0/60.0, 1e-6);
        clock.setNow(Instant.ofEpochSecond(188 + ClockEventsStatistic.secondsInHour));
        Assert.assertEquals(stats.getEventStatisticByName("music"), 0.0, 1e-6);
        Assert.assertEquals(stats.getEventStatisticByName("music1"), 0.0/60.0, 1e-6);
        Assert.assertEquals(stats.getEventStatisticByName("music2"), 0.0/60.0, 1e-6);
    }

    @Test
    public void testExpired() {
        SetableClock clock = new SetableClock(Instant.ofEpochSecond(100));
        ClockEventsStatistic stats = new ClockEventsStatistic(clock);
        putBasic(clock, stats);
        clock.setNow(Instant.ofEpochSecond(100 + ClockEventsStatistic.secondsInHour));
        Map<String, Double> res = stats.getAllEventStatistic();
        Assert.assertEquals(res.getOrDefault("music", 0.0), 0.0, 1e-6);
        Assert.assertEquals(res.getOrDefault("music2", 0.0), 2.0/60.0, 1e-6);
        Assert.assertEquals(res.getOrDefault("music1", 0.0), 1.0/60.0, 1e-6);
        clock.setNow(Instant.ofEpochSecond(140 + ClockEventsStatistic.secondsInHour));
        res = stats.getAllEventStatistic();
        Assert.assertEquals(res.getOrDefault("music", 0.0), 0.0, 1e-6);
        Assert.assertEquals(res.getOrDefault("music1", 0.0), 1.0/60.0, 1e-6);
        Assert.assertEquals(res.getOrDefault("music2", 0.0), 1.0/60.0, 1e-6);
        clock.setNow(Instant.ofEpochSecond(188 + ClockEventsStatistic.secondsInHour));
        res = stats.getAllEventStatistic();
        Assert.assertEquals(res.getOrDefault("music", 0.0), 0.0, 1e-6);
        Assert.assertEquals(res.getOrDefault("music1", 0.0), 0.0/60.0, 1e-6);
        Assert.assertEquals(res.getOrDefault("music2", 0.0), 0.0/60.0, 1e-6);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNormalTime() {
        SetableClock clock = new SetableClock(Instant.ofEpochSecond(100));
        ClockEventsStatistic stats = new ClockEventsStatistic(clock);
        putBasic(clock, stats);
        clock.setNow(Instant.ofEpochSecond(1));
        stats.incEvent("bad");
    }
}
