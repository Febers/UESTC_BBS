/*
 * Created by Febers at 18-6-14 下午10:41.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-14 下午10:41.
 */

package com.febers.uestc_bbs.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Id
    private long id;
    private String name;
    private String password;
    private int uid;
    private String title;
    private String token;
    private String secrete;
    private int score;
    private String avatar;
    private int groupId;
    private int credits;
    private int extcredits2;

    @Generated(hash = 477681708)
    public User(long id, String name, String password, int uid, String title,
            String token, String secrete, int score, String avatar, int groupId,
            int credits, int extcredits2) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.uid = uid;
        this.title = title;
        this.token = token;
        this.secrete = secrete;
        this.score = score;
        this.avatar = avatar;
        this.groupId = groupId;
        this.credits = credits;
        this.extcredits2 = extcredits2;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecrete() {
        return secrete;
    }

    public void setSecrete(String secrete) {
        this.secrete = secrete;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getExtcredits2() {
        return extcredits2;
    }

    public void setExtcredits2(int extcredits2) {
        this.extcredits2 = extcredits2;
    }
}