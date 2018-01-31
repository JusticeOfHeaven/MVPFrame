package com.my.mvpframe.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by ZJ register 2018/1/30.
 * sql语句，表名必须跟实体类Entity定义的表名一致
 * 所有的跟新都会根据主键去查询更新，不传入主键，将默认为0
 */
@Dao
public interface UserDao {
    /**
     * 获取所有的数据
     */
    @Query("SELECT * FROM user")
    List<User> getAll();

    /**
     * 根据用户uid 查找对应的数据，有多个的时候，只会取第一个数据
     * @param userId the user uid
     */
    @Query("SELECT * FROM user WHERE uid IN (:userId) LIMIT 1")
    User loadById(int userId);

    /**
     * Insert a user in the database. If the user already exists, replace it.
     *
     * @param users the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdata(User... users);

    @Delete
    void delete(User user);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM user")
    void deleteAllUsers();
}
