package com.shared_communication.backend;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InformationPage {
    private String pageTitle;
    private User author;

    private String pagePath = "./src/main/resources/static/pages.json";

    private ArrayList<User> readUsers = new ArrayList<>();
    private ArrayList<User> writeUsers = new ArrayList<>();
    private LinkedHashMap<User,String> posts = new LinkedHashMap<>();

    public InformationPage(String title, User author,String pagePath) throws JSONException, IOException {

        this.pageTitle = title;
        this.author = author;
        if(pagePath!=null){
            this.pagePath=pagePath;
        }
        registerToAuthor();

    }

    public User getAuthor(){

        return this.author;
    }
    public void registerToAuthor(){

        this.author.addMyPage(this);
    }

    public void addReadUser(User user){

        if(!readUsers.contains(user)) {
            readUsers.add((user));
        }
        else{
            throw new IllegalArgumentException("User already exists in the list of users having read access tot he page");
        }
    }

    public void addWriteUser(User user){

        if(!writeUsers.contains(user)) {
            writeUsers.add((user));

            if(!readUsers.contains(user)){
                addReadUser(user);
            }
        }
        else{
            throw new IllegalArgumentException("User already exists in the list of page users");
        }
    }

    public ArrayList<User> getPageReadUsers(){

        return this.readUsers;
    }

    public ArrayList<User> getPageWriteUsers(){

        return this.writeUsers;
    }
    public void removeReadUser(User user){

        if(readUsers.contains(user)) {
            readUsers.remove(user);

            if(writeUsers.contains(user)){

                writeUsers.remove(user);
            }

        }
        else{
            throw new IllegalArgumentException("This user is not in the list of users having read access");
        }
    }

    public void removeWriteUser(User user){

        if(writeUsers.contains(user)) {
            writeUsers.remove(user);
        }
        else{
            throw new IllegalArgumentException("This user is not in the list of users having write access");
        }
    }

    //@TODO check if system admins can make any changes in a post or not
    public void addPost(User user, String post){

        if(writeUsers.contains(user) | this.author.userEquals(user) | (user.getRole().equals(Roles.SystemAdmin))){

            if(!posts.containsValue(post)) {
                posts.put(user, post);
            }else{
                throw new IllegalArgumentException("This post already exists on this page.");
            }
        }
        else{
            throw new IllegalArgumentException("Do not have permission to add any posts on this page");
        }

    }

    public LinkedHashMap<User,String> getPosts(){

        return this.posts;
    }

    public void changeReadToWriteUser(User user){

        if(!writeUsers.contains(user)){

            if(readUsers.contains(user)) {

                writeUsers.add(user);
            } else{

                throw new IllegalArgumentException("This user is not part of list of users having read access");
            }
        }else{

            throw new IllegalArgumentException("This user is already part ot list of users having write access");
        }
    }


}
