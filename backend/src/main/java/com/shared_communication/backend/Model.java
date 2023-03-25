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

public class Model {
    String path;
    String pagePath = "./src/main/resources/static/pages.json";

    public Model(String jsonPath) {
        this.path = jsonPath;
    }



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
