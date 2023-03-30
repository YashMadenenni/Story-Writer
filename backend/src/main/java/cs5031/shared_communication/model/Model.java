package cs5031.shared_communication.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

/*
 * Model class is the main backend class which server interacts
 * 
 */

public class Model {
    String path;
    String pagePath = "./src/main/resources/static/pages.json";

    /**
     *@param allUsers strores all users for current model
     *@param allAdmin stores all admins for current model 
     *@param userJson stores users latest details from file users.json
     * @param adminJson stores admin latest details from file admin.json
     */
    String userPath;
    String adminPath;
    private HashMap<String, User> allUsers;
    private HashMap<String, User> allAdmins;
    JSONObject userJson = null;
    JSONObject adminJson = null;

    String adminFilePath = "src/main/resources/static/admin.json";
    String userFilePath = "src/main/resources/static/users.json";

    String adminMessagePath = "src/main/resources/static/usermessage.json";

    /**
     * Constructor method for Model class object
     * @param jsonPath path of the json file required to initiate the model
     */
    public Model(String jsonPath) {
        this.path = jsonPath;
    }

    /**
     * Constructor for model class to initiate model with bunch of json files
     * @param userJsonPath path to json storing user details
     * @param adminJsonPath path to json storing admin details
     */
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
                
                e.printStackTrace();
            }

        }
    }

    /**Method to parse through users json and store the data in allUser
     * @param userJson stores users latest details from file users.json
     * @return allUsers
     * @throws JSONException
     * @throws IOException
     */
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

    /**
     * @param adminJson stores users latest details from file admin.json
     * @return allAdmins
     * @throws JSONException
     * @throws IOException
     */
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

    /**Helper method to read json file
     * @param jsonPath path of the json file
     * @return JSONObject
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject loadInitialState(String jsonPath)
            throws IOException, JSONException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(jsonPath)));
        return new JSONObject(jsonBody);
    }

    
    /**Method to register user 
     * @param userName -stores user name
     * @param email - stores user email
     * @param password - stores user password
     * @param role - stores role of user "admin"/"user"
     * @return true if success else faliled 
     * @throws JSONException
     */
    public Boolean registerUser(String userName, String email, String password, String role) throws JSONException {
        JSONObject newUser = new JSONObject();
        
        newUser.put("userName", userName);
        newUser.put("password", password);
        User user = null;
        try {

            if (role == "admin") {
               
                
                if(!allAdmins.containsKey(email)){
                    user = new User(email,password, userName,  Roles.SystemAdmin, "src/main/resources/static/admin.json");
                allAdmins.put(email, user);
                newUser.put("adminEmail", email);
                newUser.put("role", "admin");
                JSONArray jsonAdmins = adminJson.getJSONArray("admin");
                jsonAdmins.put(newUser);
                changeDataInFile(adminFilePath, adminJson);
                }
            } else if(role=="user") {
                if(!allUsers.containsKey(email)){
                    user = new User(email,password, userName,  Roles.StandardUser, "src/main/resources/static/users.json");
                allUsers.put(email, user);

                newUser.put("userEmail", email);
                newUser.put("role", "user");

                JSONArray jsonUsers = userJson.getJSONArray("users");
                jsonUsers.put(newUser);
                changeDataInFile(userFilePath, userJson);
                

                }
            }
        } catch (IOException e) {
            
            e.printStackTrace();
        }

        if (user != null) {
            return true;
        } else {
            return false;
        }

    }

    
    /**Method for user login.
     * @param email - stores user email
     * @param password - stores user password
     * @return true if success else failed
     */
    public Boolean userLogin(String email, String password, String role) {


        boolean check = false;
        if (role == "user") {
            
            if (allUsers.containsKey(email)) {

                User user = allUsers.get(email);
                if ((user.getPassword()).equals(password)) {
                    check = true;
                }
            }
        } else if (role == "admin") {
            if (allAdmins.containsKey(email)) {
                User user = allAdmins.get(email);
                if ((user.getPassword()).equals(password)) {
                    check = true;
                }
            }
        }

        return check;
    }
    

    /** Method to delete user from system
     * @param email - stores email of the user to be deleted
     * @return true if success else failed
     * @throws JSONException
     */
    public Boolean deleteUserSystem(String email) throws JSONException {

        boolean check = false;
        
            if (allUsers.containsKey(email)) {
                allUsers.remove(email);
                check = true;
                deleteFromJsonPage("user",email);
            }
         else if (allAdmins.containsKey(email)) {
                allAdmins.remove(email);

                check = true;
                deleteFromJsonPage("admin",email);

            }
        

        return check;
    }

    /**Method TO Delete user and  update json file that stores data
     * @param userType
     * @param email - emial of the user to delete
     * @throws JSONException
     */
    private void deleteFromJsonPage(String userType, String email) throws JSONException {
        if(userType=="user"){
            JSONArray jsonUsers = userJson.getJSONArray("users");
            for (int i = 0; i < jsonUsers.length(); i++) {
                JSONObject userObject = jsonUsers.getJSONObject(i);
                if(userObject.getString("userEmail").equals(email)){
                    jsonUsers.remove(i);
                }
            }
            userJson.remove("users");
            userJson.put("users", jsonUsers);
            changeDataInFile(userFilePath,userJson);
        }else if(userType=="admin"){
            JSONArray jsonAdmin = adminJson.getJSONArray("admin");
            for (int i = 0; i < jsonAdmin.length(); i++) {
                JSONObject userObject = jsonAdmin.getJSONObject(i);
                if(userObject.getString("adminEmail").equals(email)){
                    jsonAdmin.remove(i);
                }
            }
            adminJson.remove("admin");
            adminJson.put("admin", jsonAdmin);
            changeDataInFile(adminFilePath,adminJson);
        }
    }

        
        /**Method to get the user details
         * @param email - stores the email of user to fetch
         * @return user JSONObject
         */
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

    
    /**Method to update user
     * @param email - email of user to update
     * @param role - current role of this user
     * @return true if success
     * @throws JSONException
     */
    public Boolean updateUser(String email, String role) throws JSONException {

        
        boolean check = false;
        if (role.equals( "user")) {
            if (allUsers.containsKey(email)) {
                User user = allUsers.get(email);
                
                user.setRole(Roles.SystemAdmin);

                allUsers.remove(email);
                allAdmins.put(email, user);

                deleteFromJsonPage("user", email);
                JSONArray jsonAdmins = adminJson.getJSONArray("admin");
                JSONObject newUser = new JSONObject();
                newUser.put("adminEmail", user.getEmail());
                newUser.put("password", user.getPassword());
                newUser.put("role", user.getRole());
                newUser.put("userName", user.getUserName());
                jsonAdmins.put(newUser);
                
                changeDataInFile(adminFilePath, adminJson);
                check= true;
            }
        } else if (role.equals("admin")) {
            if (allAdmins.containsKey(email)) {
                User user = allAdmins.get(email);
                user.setRole(Roles.StandardUser);
                allAdmins.remove(email);
                allUsers.put(email, user);

                deleteFromJsonPage("admin", email);
                JSONArray jsonUsers = userJson.getJSONArray("users");
                JSONObject newUser = new JSONObject();
                newUser.put("userEmail", user.getEmail());
                newUser.put("password", user.getPassword());
                newUser.put("role", user.getRole());
                newUser.put("userName", user.getUserName());
                jsonUsers.put(newUser);
                changeDataInFile(userFilePath, userJson);
                check= true;
            }
           
        }

        return check;
    }

    /**Method to change data in file 
     * @param initialFilePath - path of file
     * @param jsonDataToWrite - json data to overwrite
     */
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


    /***
     * Method to get an object of User class using user's email address
     * @param userEmail email address of the user
     * @return an object of class USer
     */
    public User getUserObj(String userEmail){

       return allUsers.get(userEmail);

    }


    /**
     * Method to create a new page using email address of creator user and title for the new page
     * @param userEmail email address of the creator
     * @param title title for the page
     * @throws JSONException
     * @throws IOException
     */
    public void createNewPage(String userEmail,String title) throws JSONException, IOException {

        User user = null;

        if(allAdmins.containsKey(userEmail)){
            user = allAdmins.get(userEmail);
        } else {
            user = allUsers.get(userEmail);
        }
        String pagePath = getPagePath();
        if(!getAllPageTitles().contains(title)) {
            InformationPage page = new InformationPage(title, user, pagePath);
        }
        else{
            throw new IllegalArgumentException("Page with this tile already exists");
        }

    }

    /**
     * Method to get all the page titles from the system
     * @return list of titles in the system.
     * @throws IOException
     * @throws JSONException
     */
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

    /**
     * Method to set path of pages json file
     * @param path path to be used for storing pages data
     */
    public void setPagePath(String path){

        this.pagePath = path;
    }

    /**
     * Method to get path of the page json
     * @return string object containing path of the page
     */
    public String getPagePath(){

        return this.pagePath;
    }

    /**
     * Method to load information of all the pages
     * @return a json object containing information of all the pages
     * @throws IOException
     */
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

    /**
     * Method to get information about  page using page title
     * @param title title of the page
     * @return page in the form of a json object
     * @throws IOException
     * @throws JSONException
     */
    public JSONObject getPageInfo(String title) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        String foundKey=null;

        for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
            String key = (String) it.next();

            JSONObject temp = jsonPages.getJSONObject(key);

            if(temp.getString("title").equals(title)){
                foundKey = key;
            }

        }
        if(foundKey==null){

            throw new IllegalArgumentException("Page with this name doesn't exists.");

        }
        JSONObject page = jsonPages.getJSONObject(foundKey);
        return page;


    }

    /**
     * Method to get users which can edit a page
     * @param title title of the page
     * @return list of user email address who can edit a page
     * @throws IOException
     * @throws JSONException
     */
    public ArrayList<String> getPageEditUsers(String title) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        String foundKey=null;
        ArrayList<String> userList = new ArrayList<>();
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

            if (users != null) {
                int len = users.length();
                for (int i=0;i<len;i++){
                    userList.add(users.get(i).toString());
                }
            }


        }
        return userList;
    }

    /**
     * Method to get list of users which have read access of a page
     * @param title title of the page
     * @return list of users email address
     * @throws IOException
     * @throws JSONException
     */
    public ArrayList<String> getPageReadUsers(String title) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        String foundKey=null;
        ArrayList<String> userList = new ArrayList<>();
        for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
            String key = (String) it.next();

            JSONObject temp = jsonPages.getJSONObject(key);

            if(temp.getString("title").equals(title)){
                foundKey = key;
            }

        }

        if(foundKey != null) {

            JSONObject page = jsonPages.getJSONObject(foundKey);
            JSONArray users = page.getJSONArray("readAccessUser");

            if (users != null) {
                int len = users.length();
                for (int i=0;i<len;i++){
                    userList.add(users.get(i).toString());
                }
            }

        }
        return userList;
    }

    /**
     * Method to get a jason array of all the pages, only to be used by admin
     * @param userEmail string containing email address
     * @return a json array containing all the pages
     * @throws IOException
     * @throws JSONException
     */
    public JSONArray getAllPages(String userEmail) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        JSONArray resultAllPages = new JSONArray();
        Iterator<String> keys = jsonPages.keys();
        if(allAdmins.containsKey(userEmail)){
            while(keys.hasNext()) {
                String key = keys.next();
                if (jsonPages.get(key) instanceof JSONObject) {
                    JSONObject temp = jsonPages.getJSONObject(key);
                    resultAllPages.put(temp);
                }
            }

            return resultAllPages;
        }else{
            throw new IllegalArgumentException("This user does not have admin privileges.");
        }

    }

    /**
     * Method to get list of pages created by a user
     * @param userEmail email of the user
     * @return json array having all the pages created by a user
     * @throws IOException
     * @throws JSONException
     */
    public JSONArray getUserCreatedPages(String userEmail) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        JSONArray resultAllPages = new JSONArray();
        Iterator<String> keys = jsonPages.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonPages.get(key) instanceof JSONObject) {
                JSONObject temp = jsonPages.getJSONObject(key);
                if(temp.getString("admin").equals(userEmail)){
                    resultAllPages.put(temp);
                }
            }
        }
        if(resultAllPages.length()!=0){

            return resultAllPages;

        }else{
            throw new IllegalArgumentException("User has not created any page.");
        }



    }

    /**
     * Method to get post of the page
     * @param title title of the page
     * @return list of posts created by user
     * @throws IOException
     * @throws JSONException
     */

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

    /**
     * Method to add a post to a page
     * @param title title of the page
     * @param userEmail email of the user adding the post
     * @param post post to eb added
     * @throws IOException
     * @throws JSONException
     */

    public void addPostToPage(String title,String userEmail, String post) throws IOException, JSONException{

        User user = getUserObj(userEmail);
        addPostToPage(title,user,post);

    }

    /**
     * Method to add a post to a page
     * @param title title of the page
     * @param user user object of the user adding the post
     * @param post post to eb added
     * @throws IOException
     * @throws JSONException
     */
    public void addPostToPage(String title,User user, String post) throws IOException, JSONException {

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
                //String content = page.getString("content");
                //ArrayList<String> currentContent = new ArrayList<>(List.of(content.split(",")));
                if(getPageEditUsers(title).contains(user.getEmail())){
                    String newContent;
                    newContent = post;
                    page.put("content",newContent);
                    jsonPages.remove(foundKey);
                    jsonPages.put(foundKey,page);
                    FileWriter file = new FileWriter(this.pagePath);
                    file.write(jsonPages.toString());
                    file.flush();
                    file.close();

                }
                else{
                    throw new IllegalArgumentException("Same post already exists on this page or user doesn't have required access");
                }
            }
            else{
                throw new IllegalArgumentException("Page with this title doesn't exists.");
            }

        }


    }


    /**
     * Method to revode edit access of a user from a page
     * @param title title of the page
     * @param user users whoe access is to be revoked
     * @throws IOException
     * @throws JSONException
     */
    public void deleteUserEditAccess(String title, String user) throws IOException, JSONException {

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
                ArrayList<String> userList = getPageEditUsers(title);
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

                }
                else{
                    throw new IllegalArgumentException("User doesn't have access to this page.");
                }

            }
            else{
                throw new IllegalArgumentException("Page with this title doesn't exists.");
            }

        }


    }

    /**
     * MEthod to revoke read access of a user from a page
     * @param title title of the page
     * @param user user whose access is to be revoked
     * @throws IOException
     * @throws JSONException
     */

    public void deleteUserReadAccess(String title, String user) throws IOException, JSONException {

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
                JSONArray  usersEdit = page.getJSONArray("editAccessUser");
                JSONArray  usersRead = page.getJSONArray("readAccessUser");
                ArrayList<String> userListEdit = getPageEditUsers(title);
                ArrayList<String> userListRead = getPageReadUsers(title);
                if(userListRead.contains(user)){
                    userListRead.remove(user);
                    JSONArray updatedUsers = new JSONArray(userListRead);
                    page.put("readAccessUser",updatedUsers);
                    if(userListEdit.contains(user)){
                        userListEdit.remove(user);
                        JSONArray updatedUsersEdit = new JSONArray(userListEdit);
                        page.put("editAccessUser",updatedUsers);
                    }
                    jsonPages.remove(foundKey);
                    jsonPages.put(foundKey,page);
                    FileWriter file = new FileWriter(this.pagePath);
                    file.write(jsonPages.toString());
                    file.flush();
                    file.close();

                }
                else{
                    throw new IllegalArgumentException("User doesn't have access to this page.");
                }

            }
            else{
                throw new IllegalArgumentException("Page with this title doesn't exists.");
            }

        }


    }

    /**
     * Method to add edit access to a page
     * @param title title of the page to which access is to be added
     * @param userEmail email to be rewarded edit access
     * @return boolean indicating is successfully done
     * @throws IOException
     * @throws JSONException
     */
    public boolean addUserEditAccess(String title, String userEmail) throws IOException, JSONException {
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
                JSONArray  usersEdit = page.getJSONArray("editAccessUser");
                JSONArray  usersRead = page.getJSONArray("readAccessUser");
                ArrayList<String> userListEdit = getPageEditUsers(title);
                ArrayList<String> userListRead = getPageReadUsers(title);
                if(!userListEdit.contains(userEmail)){
                    userListEdit.add(userEmail);
                    JSONArray updatedUsers = new JSONArray(userListEdit);
                    page.put("editAccessUser",updatedUsers);
                    if(!userListRead.contains(userEmail)){
                        userListRead.add(userEmail);
                        JSONArray updatedUsersRead = new JSONArray(userListRead);
                        page.put("readAccessUser",updatedUsersRead);
                    }
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

    /**
     * Method to add read access to a page
     * @param title title of the page
     * @param userEmail email of the user to be rewarded access
     * @return boolean insidcating if access is rewarded
     * @throws IOException
     * @throws JSONException
     */
    public boolean addUserReadAccess(String title, String userEmail) throws IOException, JSONException {
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
                JSONArray  usersRead = page.getJSONArray("readAccessUser");
                ArrayList<String> userListRead = getPageReadUsers(title);
                if(!userListRead.contains(userEmail)){

                    userListRead.add(userEmail);
                    JSONArray updatedUsers = new JSONArray(userListRead);
                    page.put("readAccessUser",updatedUsers);
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

    /**
     * Method to add message by admin to the system
     * @param email email of the amdin
     * @param Message message to be added by admin
     * @return boolean indicating if process was successful
     * @throws IOException
     * @throws JSONException
     */

    public boolean addAdminMessage(String email,String Message) throws IOException, JSONException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(this.adminMessagePath)));
        JSONObject json = null;

        try {
            json = new JSONObject(jsonBody);
        } catch (JSONException e){

            throw new IllegalArgumentException("File with this name doesn't exists.");
        }




        if(json!=null) {

            JSONArray messageArray = json.getJSONArray("message");
            for (int i = 0; i < messageArray.length(); i++) {
                JSONObject element = messageArray.getJSONObject(i);

                if(element.get("admin").equals(email)){

                    messageArray.remove(i);
                }
            }
            JSONObject newMessage = new JSONObject();
            newMessage.put("admin",email);
            newMessage.put("message",Message);
            messageArray.put(newMessage);
            JSONObject newMessagefile = new JSONObject();
            newMessagefile.put("message",messageArray);
            FileWriter file = new FileWriter(this.adminMessagePath);
            file.write(newMessagefile.toString());
            file.flush();
            file.close();
            return true;
        }
        else{
            return false;
        }


    }

    /**
     * Method to get messages shared by amdin
     * @return messages by admin
     * @throws IOException
     */
    public String getAdminMessage() throws IOException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(this.adminMessagePath)));
        JSONObject json = null;
        try {
            json = new JSONObject(jsonBody);
            return json.toString();
        } catch (JSONException e){

            throw new IllegalArgumentException("File with this name doesn't exists.");
        }

    }

    /***
     * Method for a user to identify which pages they can read
     * @param userEmail email of the user
     * @return list of titles of the page they can read
     * @throws IOException
     * @throws JSONException
     */
    public ArrayList<String> getMyReadPages(String userEmail) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        ArrayList<String> myReadPages = new ArrayList<String>();
        if (jsonPages != null) {

            for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
                String key = (String) it.next();

                JSONObject temp = jsonPages.getJSONObject(key);
                ArrayList<String> userListRead = getPageReadUsers(temp.getString("title"));
                if (userListRead.contains(userEmail)) {
                    myReadPages.add(temp.getString("title"));
                }

            }


        }
        return myReadPages;

    }

    /**
     * MEthod to get list of pages a user can write to
     * @param userEmail email of the user
     * @return list of pages titles where a user can write.
     * @throws IOException
     * @throws JSONException
     */
    public ArrayList<String> getMyWritePages(String userEmail) throws IOException, JSONException {

        JSONObject jsonPages = loadPages();
        ArrayList<String> myWritePages = new ArrayList<String>();
        if(jsonPages != null) {

            String foundKey=null;
            for (Iterator it = jsonPages.keys(); it.hasNext(); ) {
                String key = (String) it.next();

                JSONObject temp = jsonPages.getJSONObject(key);
                ArrayList<String> userListRead = getPageEditUsers(temp.getString("title"));
                if(userListRead.contains(userEmail)){
                    myWritePages.add(temp.getString("title"));
                }

            }

        }
        return myWritePages;
}
}
