package no.kristiania.project.management.database.tabels;

public class WorkerTask {
    private long id;
    private long workerID;
    private long taskID;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setWorkerID(long workerID) {
        this.workerID = workerID;
    }

    public long getWorkerID() {
        return workerID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public long getTaskID() {
        return taskID;
    }
}
