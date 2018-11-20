package com.my.mvpframe.customview.collsion;

/**
 * Created by jzhan on 2018/11/20.
 **/
public class TaskModel {
    private String name;
    private String id;
    private String difficultyLevel;

    public TaskModel(String name, String id, String difficultyLevel) {
        this.name = name;
        this.id = id;
        this.difficultyLevel = difficultyLevel;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
