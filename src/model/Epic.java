package model;

import java.util.List;
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Epic extends Task {

    private Duration duration = Duration.ZERO;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtask(Integer subtaskId) {
        if (subtaskId != null && subtaskId.equals(getId())) {
            throw new IllegalArgumentException("Эпик не может содержать себя как сабтаск");
        }
        subtaskIds.add(subtaskId);
    }

    public void removeSubtask(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void clearSubtasks() {
        subtaskIds.clear();
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateTimeFields(List<Subtask> subtasks) {
        if (subtasks == null || subtasks.isEmpty()) {
            this.duration = Duration.ZERO;
            this.startTime = null;
            this.endTime = null;
            return;
        }

        this.duration = subtasks.stream()
                .map(Subtask::getDuration)
                .filter(d -> d != null)
                .reduce(Duration.ZERO, Duration::plus);

        this.startTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(t -> t != null)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        this.endTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(t -> t != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + getTaskStatus() +
                ", subtaskIds=" + subtaskIds +
                ", duration=" + (duration != null ? duration.toMinutes() + "m" : "null") +
                ", startTime=" + (startTime != null ? startTime.format(formatter) : "null") +
                ", endTime=" + (endTime != null ? endTime.format(formatter) : "null") +
                '}';
    }
}
