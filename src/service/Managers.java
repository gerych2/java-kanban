package service;

public class Managers {

    private Managers() {
        // Приватный конструктор предотвращает создание экземпляра
    }

    public static TaskManager getDefault() {
        HistoryManager history = getDefaultHistory();
        return new InMemoryTaskManager(history);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
