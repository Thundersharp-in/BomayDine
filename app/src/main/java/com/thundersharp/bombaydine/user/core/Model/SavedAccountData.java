package com.thundersharp.bombaydine.user.core.Model;

public class SavedAccountData {

    private String name,id,uid,type;

    public SavedAccountData(String name, String id, String uid, String type) {
        this.name = name;
        this.id = id;
        this.uid = uid;
        this.type = type;
    }

    public static SavedAccountData start(String name, String id, String uid, String type){
        return new SavedAccountData(name,id,uid,type);
    }

    public SavedAccountData(){}

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getType() {
        return type;
    }
}
