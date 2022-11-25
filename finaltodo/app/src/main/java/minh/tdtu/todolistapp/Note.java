package minh.tdtu.todolistapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Note implements Serializable {
    private int id;
    private String title;
    private String date;
    private String time;
    private String status;
    private String content;

    public Note() {

    }

    public Note(String title, String date, String time, String status, String content) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.status = status;
        this.content = content;
    }

    public Note(int id, String title, String date, String time, String status, String content) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.status = status;
        this.content = content;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                ", name='" + title + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static ArrayList<Note> generate10Note(){
        ArrayList<Note> notes = new ArrayList<>();

        notes.add(new Note(0,"Note 1", "24/12/2022","14:12", "Done","Di an com 1"));


        return notes;
    }
}