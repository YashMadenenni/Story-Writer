package com.shared_communication.backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.io.File;

import java.io.PrintWriter;

import java.sql.Array;

import java.util.HashMap;



public class Model {
    String path;
    String pagePath = "./src/main/resources/static/pages.json";

    String userPath;
    String adminPath;
    private HashMap<String, User> allUsers;
    private HashMap<String, User> allAdmins;
    JSONObject userJson = null;
    JSONObject adminJson = null;
    String adminFilePath = "src/main/resources/static/admin.json";
    String userFilePath = "src/main/resources/static/users.json";

    public Model(String jsonPath) {
        this.path = jsonPath;
    }

    public Model(String userJsonPath, String adminJsonPath) {
        this.userPath = userJsonPath;
        this.adminPath = adminJsonPath;
        
        try {
           this.userJson = loadInitialState(userPath);
           this.adminJson = loadInitialState(adminPath);

        } catch (IOException | JSONException e) {
        }
        if (null != userJson) {
            try {
                allUsers = processUsersJson(userJson);
                allAdmins = processAdminJson(adminJson);
            } catch (JSONException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private HashMap<String, User> processUsersJson(JSONObject userJson) throws JSONException, IOException {
        HashMap<String, User> allUser = new HashMap<>();

        JSONArray jsonUsers = userJson.getJSONArray("users");
        for (int i = 0; i < jsonUsers.length(); i++) {
            JSONObject userObject = jsonUsers.getJSONObject(i);

            String userName = userObject.getString("userName");
            String userEmail = userObject.getString("userEmail");
            String password = userObject.getString("password");

            User newUser = new User(userEmail, password, userName, Roles.StandardUser,
                    "src/main/resources/static/users.json");

            String newUserEmail = newUser.getEmail();

            allUser.put(newUserEmail, newUser);
        }
        return allUser;
    }

    private HashMap<String, User> processAdminJson(JSONObject adminJson) throws JSONException, IOException {
        HashMap<String, User> allAdmin = new HashMap<>();

        JSONArray jsonAdmins = adminJson.getJSONArray("admin");
        for (int i = 0; i < jsonAdmins.length(); i++) {
            JSONObject adminObject = jsonAdmins.getJSONObject(i);

            String userName = adminObject.getString("userName");
            String userEmail = adminObject.getString("adminEmail");
            String password = adminObject.getString("password");

            User newAdmin = new User(userEmail, password, userName, Roles.SystemAdmin,
                    "src/main/resources/static/admin.json");

            String newAdminEmail = newAdmin.getEmail();

            allAdmin.put(newAdminEmail, newAdmin);
        }
        return allAdmin;
    }

    protected static JSONObject loadInitialState(String jsonPath)
            throws IOException, JSONException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(jsonPath)));
        return new JSONObject(jsonBody);
    }

    // userregister
    public Boolean registerUser(String userName, String email, String password, String role) throws JSONException {
        JSONObject newUser = new JSONObject();
        newUser.put("email", email);
        newUser.put("userName", userName);
        newUser.put("password", password);
        //System.out.println(" Password" + password);
        //System.out.println(" ");
        User user = null;
        try {

            if (role == "admin") {
               
                
                if(!allAdmins.containsKey(email)){
                    user = new User(email,password, userName,  Roles.SystemAdmin, "src/main/resources/static/admin.json");
                allAdmins.put(email, user);
                }
            } else if(role=="user") {
                if(!allUsers.containsKey(email)){
                    user = new User(email,password, userName,  Roles.StandardUser, "src/main/resources/static/users.json");
                allUsers.put(email, user);
                }
            }

            // for (User users : allUsers.values()) {
            //     System.out.println("After regi ");
            //     System.out.println(users.getUserEmail());
            //     System.out.println(users.getPassword());
            //     System.out.println(" ");
            // }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (user != null) {
            return true;
        } else {
            return false;
        }

    }

    // userLogin
    public Boolean userLogin(String email, String password, String role) {

        System.out.println("Model");
        System.out.println(email);
            System.out.println(password);

            for (User users : allUsers.values()) {
                System.out.println(users.getUserEmail());
            }

        boolean check = false;
        if (role == "user") {
            
            if (allUsers.containsKey(email)) {
                System.out.println("user in");
                User user = allUsers.get(email);
                System.out.println("verify");
                System.out.println(user.getPassword());
                System.out.println(password);
                if ((user.getPassword()).equals(password)) {
                    System.out.println("user in 2");
                    check = true;
                }
            }
        } else if (role == "admin") {
            System.out.println("Admin in");
            if (allAdmins.containsKey(email)) {
                System.out.println("Admin in 2");
                User user = allAdmins.get(email);
                if ((user.getPassword()).equals(password)) {
                    System.out.println("Admin in 3");
                    check = true;
                }
            }
        }

        return check;
    }
    // deleteUser

    public Boolean deleteUserSystem(String email, String role) {

        boolean check = false;
        if (role == "user") {
            if (allUsers.containsKey(email)) {
                allUsers.remove(email);
                check = true;
            }
        } else if (role == "admin") {
            if (allAdmins.containsKey(email)) {
                allAdmins.remove(email);

                check = true;

            }
        }

        return check;
    }

    // getUser
    public JSONObject getUser(String email) {

        boolean check = false;
        JSONObject userJson = new JSONObject();

        if (allUsers.containsKey(email)) {
            User user = allUsers.get(email);
            try {
                userJson.put("userName", user.getUserName());
                userJson.put("userEmail", user.getEmail());
                userJson.put("pages",user.getMyPages());
                userJson.put("role", user.getRole());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (allAdmins.containsKey(email)) {
            User user = allAdmins.get(email);
            try {
                userJson.put("adminName", user.getUserName());
                userJson.put("adminEmail", user.getEmail());
                userJson.put("pages",user.getMyPages());
                userJson.put("role", user.getRole());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return userJson;
    }

    // updateUser
    public Boolean updateUser(String email, String currentRole, String roleChange) {

        boolean check = false;
        if (currentRole == "user") {
            if (allUsers.containsKey(email)) {
                User user = allUsers.get(email);
                user.setRole(Roles.StandardUser);
            }
        } else if (currentRole == "admin") {
            if (allAdmins.containsKey(email)) {
                User user = allAdmins.get(email);
                user.setRole(Roles.SystemAdmin);
            }
            changeDataInFile(roleChange, adminJson);
        }

        return check;
    }

    public static void changeDataInFile(String initialFilePath, JSONObject jsonDataToWrite) {
        File myObj = new File(initialFilePath);
        if (myObj.delete()) {
            System.out.println("UPDATED JSON: " + myObj.getName());
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(initialFilePath))) {
            out.write(jsonDataToWrite.toString());
            out.close();
        } catch (Exception e) {

        }
    }
    //User methods



    public void createNewPage(User user,String title,String pagePath) throws JSONException, IOException {

        if(!getAllPageTitles().contains(title)) {
            InformationPage page = new InformationPage(title, user, pagePath);
        }
        else{
            throw new IllegalArgumentException("Page with this tile already exists");
        }
    }

    public ArrayList<String> getAllPageTitles() throws IOException, JSONException {

        String jsonPath = new String(Files.readAllBytes(Paths.get(this.pagePath)));
        JSONObject jsonPages = new JSONObject(jsonPath);
        ArrayList<String> pageTitles = new ArrayList<>();
        for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
            String key = (String) it.next();

            JSONObject temp = jsonPages.getJSONObject(key);
            pageTitles.add(temp.getString("title"));

        }
        return pageTitles;

    }
    public void setPagePath(String path){

        this.pagePath = path;
    }

    public String getPagePath(){

        return this.pagePath;
    }
    public JSONObject loadPages() throws IOException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(this.pagePath)));
        JSONObject json = null;
        try {
            json = new JSONObject(jsonBody);
        } catch (JSONException e){

            throw new IllegalArgumentException("File with this name doesn't exists.");
        }
        return json;
    }

    public ArrayList<String> getPageUsers(String title) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        String foundKey=null;

        for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
            String key = (String) it.next();

            JSONObject temp = jsonPages.getJSONObject(key);

            if(temp.getString("title").equals(title)){
                foundKey = key;
            }

        }

        if(foundKey != null) {

            JSONObject page = jsonPages.getJSONObject(foundKey);
            JSONArray users = page.getJSONArray("editAccessUser");
            ArrayList<String> userList = new ArrayList<>();
            if (users != null) {
                int len = users.length();
                for (int i=0;i<len;i++){
                    userList.add(users.get(i).toString());
                }
            }
            return userList;

        }
        return null;
    }

    public ArrayList<String> getPagePosts(String title) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        String foundKey=null;

        for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
            String key = (String) it.next();

            JSONObject temp = jsonPages.getJSONObject(key);

            if(temp.getString("title").equals(title)){
                foundKey = key;
            }

        }

        if(foundKey != null) {

            JSONObject page = jsonPages.getJSONObject(foundKey);
            String posts = page.getString("content");
            ArrayList<String> currentContent = new ArrayList<>(List.of(posts.split(",")));
            return currentContent;

        }
        return null;
    }


    public boolean addPostToPage(String title,User user, String post) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        String foundKey = null;
        if(jsonPages != null) {

            for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
                String key = (String) it.next();

                JSONObject temp = jsonPages.getJSONObject(key);

                if(temp.getString("title").equals(title)){
                    foundKey = key;
                }
            }

            if(foundKey != null){
                JSONObject page = jsonPages.getJSONObject(foundKey);
                String content = page.getString("content");
                ArrayList<String> currentContent = new ArrayList<>(List.of(content.split(",")));
                if(!currentContent.contains(post) | getPageUsers(title).contains(user.getEmail())){
                    String newContent;
                    if(content.equals("")){
                        newContent = post;

                    }else {
                     newContent=content + "," + post;
                    }
                    page.put("content",newContent);
                    jsonPages.remove(foundKey);
                    jsonPages.put(foundKey,page);
                    FileWriter file = new FileWriter(this.pagePath);
                    file.write(jsonPages.toString());
                    file.flush();
                    file.close();
                    return true;
                }
                else{
                    throw new IllegalArgumentException("Same post already exists on this page or user doesn't have required access");
                }
            }
            else{
                throw new IllegalArgumentException("Page with this title doesn't exists.");
            }

        }

        return false;
    }


    public boolean deleteUser(String title,String user) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        String foundKey=null;
        if(jsonPages != null) {


            for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
                String key = (String) it.next();

                JSONObject temp = jsonPages.getJSONObject(key);

                if(temp.getString("title").equals(title)){
                    foundKey = key;
                }

            }

            if(foundKey != null){

                JSONObject page = jsonPages.getJSONObject(foundKey);
                JSONArray  users = page.getJSONArray("editAccessUser");
                ArrayList<String> userList = getPageUsers(title);
                if(userList.contains(user)){
                    userList.remove(user);
                    JSONArray updatedUsers = new JSONArray(userList);
                    page.put("editAccessUser",updatedUsers);
                    jsonPages.remove(foundKey);
                    jsonPages.put(foundKey,page);
                    FileWriter file = new FileWriter(this.pagePath);
                    file.write(jsonPages.toString());
                    file.flush();
                    file.close();
                    return true;
                }
                else{
                    throw new IllegalArgumentException("User doesn't have access to this page.");
                }

            }
            else{
                throw new IllegalArgumentException("Page with this title doesn't exists.");
            }

        }

        return false;
    }

    public boolean addUser(String title,String userEmail) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        if(jsonPages != null) {

            String foundKey=null;
            for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
                String key = (String) it.next();

                JSONObject temp = jsonPages.getJSONObject(key);

                if(temp.getString("title").equals(title)){
                    foundKey = key;
                }

            }

            if(foundKey != null){

                JSONObject page = jsonPages.getJSONObject(foundKey);
                JSONArray  users = page.getJSONArray("editAccessUser");
                ArrayList<String> userList = getPageUsers(title);
                if(!userList.contains(userEmail)){
                    userList.add(userEmail);
                    JSONArray updatedUsers = new JSONArray(userList);
                    page.put("editAccessUser",updatedUsers);
                    jsonPages.remove(foundKey);
                    jsonPages.put(foundKey,page);
                    FileWriter file = new FileWriter(this.pagePath);
                    file.write(jsonPages.toString());
                    file.flush();
                    file.close();
                    return true;
                }
                else{
                    throw new IllegalArgumentException("User already have write access to the page.");
                }

            }
            else{
                throw new IllegalArgumentException("Page with this title doesn't exists.");
            }

        }

        return false;
    }

}
