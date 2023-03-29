package cs5031.shared_communication.model;

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
        addPageToJson();

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

    // remo for commit
    public void setPagePath(String path){

        this.pagePath = path;
    }
    // remo for commit
    public String getPagePath(){

        return this.pagePath;
    }


    private void addPageToJson() throws IOException, JSONException {

        JSONObject json = null;
        try {
            json = loadPages();
        }
        catch (IOException | JSONException e){

            throw new IllegalArgumentException("File with this name doesn't exists.");
        }

        if(json != null) {
            ArrayList<String> userList = new ArrayList<>();
            userList.add(author.getEmail());
            JSONArray jsonArray = new JSONArray(userList);
            JSONArray jsonArrayRead = new JSONArray(userList);
            JSONObject newPage = new JSONObject();
            newPage.put("title",pageTitle);
            newPage.put("content","");
            newPage.put("admin",this.author.getEmail());
            newPage.put("editAccessUser",jsonArray);
            newPage.put("readAccessUser",jsonArrayRead);


            json.put("page"+json.length()+1,newPage);
            FileWriter file = new FileWriter(pagePath);
            file.write(json.toString());
            file.flush();
            file.close();
        }

    }

    protected JSONObject loadPages()
            throws IOException, JSONException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(this.pagePath)));
        return new JSONObject(jsonBody);
    }



}
