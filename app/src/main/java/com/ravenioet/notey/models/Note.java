package com.ravenioet.notey.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ravenioet.core.manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    String body;
    String date;
    String data;
    int flag;
    boolean empty = false;
    @Ignore
    public Note(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Note(String title, String body, String date, String data, int flag) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.data = data;
        this.flag = flag;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isDiffer(Note note){
        return !Objects.equals(this.title, note.getTitle())
                || !Objects.equals(this.body, note.getBody()) || !Objects.equals(this.flag, note.getFlag());
    }

    @Override
    public String toString(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("title", title);
            jsonObject.put("body", body);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
    public JSONObject toJson(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("title", title);
            jsonObject.put("body", body);
            jsonObject.put("date", date);
            jsonObject.put("data", data);
            jsonObject.put("flag", flag);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray toJsonData(List<Note> notes){
        JSONArray jsonArray = new JSONArray();
        for (Note note : notes){
            jsonArray.put(toJson(note));
        }
        return jsonArray;
    }
    public JSONObject toJson(Note note){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", note.getId());
            jsonObject.put("title", note.getTitle());
            jsonObject.put("body", note.getBody());
            jsonObject.put("date", note.getDate());
            jsonObject.put("data", note.getData());
            jsonObject.put("flag", note.getFlag());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Note emptyNote(){
        return new Note(true);
    }
    public Note fromJson(JSONObject object){
        try {
            return new Note(
                    object.getString("title"),
                    object.getString("body"),
                    object.getString("date"),
                    object.getString("data"),
                    object.getInt("flag"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return emptyNote();
    }
    public List<Note> fromJsonData(JSONArray jsonArray){
        ArrayList<Note> notes = new ArrayList<>();
        //JSONArray jsonArray = object.optJSONArray("data");
        for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
            JSONObject jsonData = jsonArray.optJSONObject(i);
            Note doormat = fromJson(jsonData);
            notes.add(doormat);
        }
        return notes;
    }

}
