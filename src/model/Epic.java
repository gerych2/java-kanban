package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.type = TaskType.EPIC;
    }

    public void updateTimeFields(List<Subtask> subtasks) {
        if (subtasks == null || subtasks.isEmpty()) {
            super.setDuration(Duration.ZERO);
            super.setStartTime(null);
            return;
        }

        Duration totalDuration = Duration.ZERO;
        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                LocalDateTime subStart = subtask.getStartTime();
                LocalDateTime subEnd = subtask.getEndTime();

                if (earliestStart == null || subStart.isBefore(earliestStart)) {
                    earliestStart = subStart;
                }
                if (latestEnd == null || (subEnd != null && subEnd.isAfter(latestEnd))) {
                    latestEnd = subEnd;
                }

                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }

        super.setDuration(totalDuration);
        super.setStartTime(earliestStart);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", status=" + super.getStatus() +
                ", type=" + super.getType() +
                ", duration=" + super.getDuration() +
                ", startTime=" + super.getStartTime() +
                ", endTime=" + super.getEndTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(super.getDuration(), epic.getDuration())
                && Objects.equals(super.getStartTime(), epic.getStartTime())
                && Objects.equals(super.getEndTime(), epic.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getDuration(), super.getStartTime(), super.getEndTime());
    }
}
