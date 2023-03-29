package com.shared_communication.backend;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class User{

    private String email;
    private String password;
    private String userName;
    private ArrayList<InformationPage> myPages = new ArrayList<>();
    private Roles role;

    public User(String email, String password, String userName,Roles role, String jsonPath) throws JSONException, IOException {

        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = role;
        //addCredentialsToFile(jsonPath,email,userName,password); Changing the user file when called
    }

    public String getPassword() {
        return this.password;
    }
    public boolean passMatch(String password){

        if(password.equals(this.password)){

            return true;
        }
        return false;
    }

    public boolean userNameMatch(String userName){

        if(userName.equals(this.userName)){

            return true;
        }

        else {
            return false;
        }
    }

    public String getUserName(){

        return this.userName;
    }

    public String getEmail(){

        return this.email;
    }

    public void addCredentialsToFile(String jsonPath,String email, String userName,String passWord) throws IOException, JSONException {

        JSONObject json = null;
        try {
            json = loadCredentials(jsonPath);
        }
        catch (IOException | JSONException e){

            throw new IllegalArgumentException("File with this name doesn't exists.");
        }

        if(json != null) {

            JSONObject credToAdd = new JSONObject();
            JSONObject userPass = new JSONObject();
            userPass.put(userName,passWord);
            credToAdd.put(email,userPass);
            json.put(email,userPass);
            FileWriter file = new FileWriter(jsonPath);
            file.write(json.toString());
            file.flush();
            file.close();
        }

    }

    protected static JSONObject loadCredentials(String jsonPath)
            throws IOException, JSONException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(jsonPath)));
        return new JSONObject(jsonBody);
    }

    public Roles getRole(){

        return this.role;
    }

    public void setRole(Roles role){

        this.role = role;
    }

    protected String getUserEmail(){

        return this.email;
    }
    public boolean userEquals(User user){

        if(this.email.equals(user.getUserEmail())){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<InformationPage> getMyPages(){

        return this.myPages;
    }

    public void addMyPage(InformationPage page){

        this.myPages.add(page);
    }



}