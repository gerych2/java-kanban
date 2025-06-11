package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void updateTimeFields(List<Subtask> subtasks) {
        if (subtasks == null || subtasks.isEmpty()) {
            this.startTime = null;
            this.endTime = null;
            this.duration = Duration.ZERO;
            return;
        }

        LocalDateTime earliest = null;
        LocalDateTime latest = null;
        Duration totalDuration = Duration.ZERO;

        for (Subtask sub : subtasks) {
            if (sub.getStartTime() != null && sub.getEndTime() != null) {
                if (earliest == null || sub.getStartTime().isBefore(earliest)) {
                    earliest = sub.getStartTime();
                }
                if (latest == null || sub.getEndTime().isAfter(latest)) {
                    latest = sub.getEndTime();
                }
                totalDuration = totalDuration.plus(sub.getDuration());
            }
        }

        this.startTime = earliest;
        this.endTime = latest;
        this.duration = totalDuration;
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
