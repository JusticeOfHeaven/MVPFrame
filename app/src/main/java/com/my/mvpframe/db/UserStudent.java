package com.my.mvpframe.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by ZJ register 2018/1/30
 * 关联表格，foreignKeys表名关联表格
 */
@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "uid", childColumns = "school_id" ))
public class UserStudent {
    @PrimaryKey
    @ColumnInfo(name = "school_id")
    private int schoolId;

    private String schoolName;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
