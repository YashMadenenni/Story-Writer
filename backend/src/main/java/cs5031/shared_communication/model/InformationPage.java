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

/**
 * Information page class
 */
public class InformationPage {
    private String pageTitle;
    private User author;

    private String pagePath = "./src/main/resources/static/pages.json";

    private ArrayList<User> readUsers = new ArrayList<>();
    private ArrayList<User> writeUsers = new ArrayList<>();
    private LinkedHashMap<User,String> posts = new LinkedHashMap<>();

    /**
     * Constructor method for the information page class
     * @param title title of the page
     * @param author author of the page
     * @param pagePath path of the file where page information is to be stored
     * @throws JSONException
     * @throws IOException
     */
    public InformationPage(String title, User author,String pagePath) throws JSONException, IOException {

        this.pageTitle = title;
        this.author = author;
        if(pagePath!=null){
            this.pagePath=pagePath;
        }
        registerToAuthor();
        addPageToJson();

    }

    /**
     * Get author of the page
     * @return user object who is author of the page
     */
    public User getAuthor(){

        return this.author;
    }

    /**
     * Method to add page to the user object
     */
    public void registerToAuthor(){

        this.author.addMyPage(this);
    }

    /**
     * Method to add a user which can read contents of the page
     * @param user user to be granted read access
     */
    public void addReadUser(User user){

        if(!readUsers.contains(user)) {
            readUsers.add((user));
        }
        else{
            throw new IllegalArgumentException("User already exists in the list of users having read access tot he page");
        }
    }

    /**
     * Method to add a user which can write to the page
     * @param user user which can write to the page
     */
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

    /**
     * Method to get list of users which can read the page
     * @return list of users
     */
    public ArrayList<User> getPageReadUsers(){

        return this.readUsers;
    }

    /**
     * Method to get list of users which can write to the page
     * @return list of users
     */
    public ArrayList<User> getPageWriteUsers(){

        return this.writeUsers;
    }

    /**
     * Method to remove read access of the user
     * @param user users whose access is to be removed
     */
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

    /**
     * Method to remove write access of the user
     * @param user user whose write access is to be removed
     */
    public void removeWriteUser(User user){

        if(writeUsers.contains(user)) {
            writeUsers.remove(user);
        }
        else{
            throw new IllegalArgumentException("This user is not in the list of users having write access");
        }
    }

    /**
     * Method to add a post to the page
     * @param user user to add the post
     * @param post post content
     */

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

    /**
     * Method to get posts stored in the form of a hasmap
     * @return posts information
     */
    public LinkedHashMap<User,String> getPosts(){

        return this.posts;
    }

    /**
     * Method to change user from read to write access
     * @param user user to be changed
     */
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


    /**
     * Method to set path of json file to store page info
     * @param path path of the file
     */
    public void setPagePath(String path){

        this.pagePath = path;
    }


    /**
     * Method to get the path of the file
     * @return return path of the file
     */
    public String getPagePath(){

        return this.pagePath;
    }

    /**
     * Method to add page to a json file
     * @throws IOException
     * @throws JSONException
     */

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

    /**
     * Method to load all the pages stored in a json
     * @return s JSONObject storing all the files
     * @throws IOException
     * @throws JSONException
     */
    protected JSONObject loadPages()
            throws IOException, JSONException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(this.pagePath)));
        return new JSONObject(jsonBody);
    }



}
