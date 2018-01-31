package com.my.mvpframe.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

/**
 * Created by ZJ register 2018/1/30.
 * 1、Room默认把类名作为数据库的表名。如果你想用其它的名称，使用@Entity注解的tableName属性，表名是大小写敏感的
 * 2、每个entity必须至少定义一个field作为主键（primary key）,必须用@PrimaryKey注释这个field
 * 3、@ColumnInfo 给表中字段取的名字
 * 4、使用@Ignore,entity中有你不想持久化的field
 * 5、使用Embedded---User里面有一个对象，该对象所包含的属性也存在同一个表中
 * 6、setter、getter方法，必须写
 */
@Entity(tableName = "user")
public class User {
    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;
    @Ignore
    private String age;

    @Embedded
    private Address address;

    public static class Address {
        private String street;
        private String state;
        private String city;

        @ColumnInfo(name = "post_code")
        private int postCode;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getPostCode() {
            return postCode;
        }

        public void setPostCode(int postCode) {
            this.postCode = postCode;
        }
    }

    public User() {
    }

    public User(int uid,String firstName, String lastName, String age, Address address) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age='" + age + '\'' +
                ", address=" + address +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
