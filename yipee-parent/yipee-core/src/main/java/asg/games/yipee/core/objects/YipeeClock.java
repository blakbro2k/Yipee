/**
 * Copyright 2024 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.core.objects;

import asg.games.yipee.core.tools.TimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * The YipeeClock class represents a timer for tracking elapsed time in the Yipee game.
 * It provides functionality to start, stop, and reset the timer, as well as retrieve
 * the elapsed time in seconds and minutes.
 * <p>
 * Features:
 * - Tracks the start time and running state of the timer.
 * - Calculates elapsed time in seconds and minutes.
 * - Includes methods to start, stop, and reset the timer.
 * <p>
 * JSON Serialization:
 * - The `@JsonIgnoreProperties` annotation excludes certain fields (`seconds`,
 * `minutes`, and `elapsedSeconds`) from being serialized in JSON output.
 * <p>
 * Notes:
 * - Utilizes `TimeUtils` for time calculations.
 * - Designed to be used as part of the Yipee game objects.
 */
@Getter
@Setter
@JsonIgnoreProperties({"seconds", "minutes", "elapsedSeconds"})
public class YipeeClock extends AbstractYipeeObject {
    private static final Logger logger = LoggerFactory.getLogger(YipeeClock.class);

    // Start time in milliseconds
    private long start;

    // Indicates whether the timer is active
    private boolean running;

    /**
     * Default constructor required for JSON serialization.
     * Initializes the timer in a reset state.
     */
    public YipeeClock() {
        resetTimer();
    }

    /**
     * Starts the timer by setting the start time to the current time in milliseconds.
     */
    public void start() {
        running = true;
        start = TimeUtils.millis();
    }

    /**
     * Calculates the total elapsed time in seconds since the timer started.
     *
     * @return the elapsed time in seconds, or 0 if the timer is not running.
     */
    public int getElapsedSeconds() {
        return (int) ((TimeUtils.millis() - start) / 1000);
    }

    /**
     * Stops the timer and resets it to its initial state.
     */
    public void stop() {
        resetTimer();
    }

    /**
     * Resets the timer to its default state.
     * Sets the start time to -1 and marks the timer as not running.
     */
    private void resetTimer() {
        start = -1;
        running = false;
    }

    /**
     * Retrieves the current elapsed seconds (modulo 60).
     *
     * @return the seconds component of the elapsed time, or -1 if the timer is not running.
     */
    public int getSeconds() {
        if (isRunning()) {
            return Math.round(getElapsedSeconds()) % 60;
        }
        return -1;
    }

    /**
     * Retrieves the current elapsed minutes.
     *
     * @return the minutes component of the elapsed time, or -1 if the timer is not running.
     */
    public int getMinutes() {
        if (isRunning()) {
            return Math.round(getElapsedSeconds()) / 60;
        }
        return -1;
    }

    /**
     * Compares this YipeeClock instance with another for equality.
     * Two instances are considered equal if their start times and running states match.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeClock that = (YipeeClock) o;
        return getStart() == that.getStart() && isRunning() == that.isRunning();
    }

    /**
     * Computes the hash code for this YipeeClock instance based on its state.
     *
     * @return the hash code of this instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStart(), isRunning());
    }
}
