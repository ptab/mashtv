package me.taborda.mashtv.tracker;

import java.util.Objects;

public class FindShowResult {

    private String title;
    private int year;
    private int trackerId;

    public FindShowResult(String title, int year, int trackerId) {
        this.title = title;
        this.year = year;
        this.trackerId = trackerId;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getTrackerId() {
        return trackerId;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) [%d]", title, year, trackerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FindShowResult)) return false;
        FindShowResult that = (FindShowResult) o;
        return year == that.year && trackerId == that.trackerId && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year, trackerId);
    }
}
