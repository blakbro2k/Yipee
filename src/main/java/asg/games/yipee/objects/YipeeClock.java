package asg.games.yipee.objects;

import asg.games.yipee.tools.TimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties({ "seconds", "minutes", "elapsedSeconds" })
public class YipeeClock extends AbstractYipeeObject {
    private long start;
    private boolean running;

    //Empty Constructor required for Json.Serializable
    public YipeeClock() {
        resetTimer();
    }

    public void start(){
        running = true;
        start = TimeUtils.millis();
    }

    public int getElapsedSeconds(){
        return (int) ((TimeUtils.millis() - start) / 1000);
    }

    public void stop(){
        resetTimer();
    }

    private void resetTimer(){
        start = -1;
        running = false;
    }

    public long getStart() {
        return start;
    }

    public int getSeconds() {
        if (isRunning()) {
            return Math.round(getElapsedSeconds()) % 60;
        }
        return -1;
    }

    public int getMinutes() {
        if (isRunning()) {
            return Math.round(getElapsedSeconds()) / 60;
        }
        return -1;
    }

    public boolean isRunning() {
        return this.running;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeClock that = (YipeeClock) o;
        return getStart() == that.getStart() && isRunning() == that.isRunning();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStart(), isRunning());
    }
}