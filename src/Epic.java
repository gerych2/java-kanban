import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description){
        super(name,description,TaskStatus.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds(){
        return getSubtaskIds();
    }

    public void addSubtask(int subtaskId){
        subtaskIds.add(subtaskId);
    }
    public void remove(int subtaskId){
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }



}
