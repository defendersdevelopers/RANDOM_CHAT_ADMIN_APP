package com.kyle.admin.models;
import java.io.Serializable;
import java.util.ArrayList;
public class Statistics implements Serializable {
    int usersNumber, femaleUsersNumber, maleUsersNumber, reactionsNumber, lovesNumber, likesNumber,inCallLikesNumber;
    ArrayList<String> messagesList;
    ArrayList<Integer> deviceList;
    ArrayList<String> onlineUsers;
    public int getInCallLikesNumber() {
        return inCallLikesNumber;
    }
    public void setInCallLikesNumber(int inCallLikesNumber) {
        this.inCallLikesNumber = inCallLikesNumber;
    }
    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }
    public void setOnlineUsers(ArrayList<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
    public Statistics() {
    }
    public void setUsersNumber(int usersNumber) {
        this.usersNumber = usersNumber;
    }
    public void setFemaleUsersNumber(int femaleUsersNumber) {
        this.femaleUsersNumber = femaleUsersNumber;
    }
    public void setMaleUsersNumber(int maleUsersNumber) {
        this.maleUsersNumber = maleUsersNumber;
    }
    public void setOnlineUsersNumber(ArrayList<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
    public void setOfflineUsersNumber(ArrayList<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
    public void setReactionsNumber(int reactionsNumber) {
        this.reactionsNumber = reactionsNumber;
    }
    public void setLovesNumber(int lovesNumber) {
        this.lovesNumber = lovesNumber;
    }
    public void setLikesNumber(int likesNumber) {
        this.likesNumber = likesNumber;
    }
    public void setMessagesList(ArrayList<String> messagesList) {
        this.messagesList = messagesList;
    }
    public void setDeviceList(ArrayList<Integer> deviceList) {
        this.deviceList = deviceList;
    }
    public int getUsersNumber() {
        return usersNumber;
    }
    public int getFemaleUsersNumber() {
        return femaleUsersNumber;
    }
    public int getMaleUsersNumber() {
        return maleUsersNumber;
    }
    public int getReactionsNumber() {
        return reactionsNumber;
    }
    public int getLovesNumber() {
        return lovesNumber;
    }
    public int getLikesNumber() {
        return likesNumber;
    }
    public ArrayList<String> getMessagesList() {
        return messagesList;
    }
    public ArrayList<Integer> getDeviceList() {
        return deviceList;
    }
}
