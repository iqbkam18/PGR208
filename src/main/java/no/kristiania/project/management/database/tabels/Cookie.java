package no.kristiania.project.management.database.tabels;

public class Cookie {
    private long workerID;
    private String cookie;
    private long ID;

    public void setWorkerID(long workerID) {
        this.workerID = workerID;
    }

    public long getWorkerID() {
        return workerID;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setID(long id) {
        this.ID = id;
    }

    public long getID() {
        return ID;
    }

}
