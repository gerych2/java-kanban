package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Epic extends Task {
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.duration = Duration.ZERO;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setTimeFields(LocalDateTime startTime, LocalDateTime endTime, Duration duration) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration != null ? duration : Duration.ZERO;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(startTime, epic.startTime)
                && Objects.equals(endTime, epic.endTime)
                && Objects.equals(duration, epic.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startTime, endTime, duration);
    }
}
