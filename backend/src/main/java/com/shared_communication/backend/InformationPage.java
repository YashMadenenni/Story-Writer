package com.shared_communication.backend;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class InformationPage {

    //@TODO Page with same title cant exist
    private String pageTitle;
    private User author;

    private ArrayList<User> allowedUsers = new ArrayList<>();
    private LinkedHashMap<User,String> posts = new LinkedHashMap<>();

    public InformationPage(String title, User author){

        this.pageTitle = title;
        this.author = author;
        registerToAuthor();
    }

    public User getAuthor(){

        return this.author;
    }
    public void registerToAuthor(){

        this.author.addMyPage(this);
    }
    public void addUser(User user){

        if(!allowedUsers.contains(user)) {
            allowedUsers.add((user));
        }
        else{
            throw new IllegalArgumentException("User already exists in the list of page users");
        }
    }

    public ArrayList<User> getPageUers(){

        return this.allowedUsers;
    }
    public void removeUser(User user){

        if(allowedUsers.contains(user)) {
            allowedUsers.remove(user);
        }
        else{
            throw new IllegalArgumentException("This user is not page of the list of page users");
        }
    }

    //@TODO check if system admins can make any changes in a post or not
    public void addPost(User user, String post){

        if(allowedUsers.contains(user) | this.author.userEquals(user) | (user.getRole().equals(Roles.SystemAdmin))){
            posts.put(user,post);
        }
        else{
            throw new IllegalArgumentException("Do not have permission to add any posts on this page");
        }

    }

    public LinkedHashMap<User,String> getPosts(){

        return this.posts;
    }









}
