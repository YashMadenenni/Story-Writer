package cs5031.shared_communication.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * USer class object
 */
public class User{

    private String email;
    private String password;
    private String userName;
    private ArrayList<InformationPage> myPages = new ArrayList<>();
    private Roles role;

    /**
     * Contructor for user class
     * @param email email of the user
     * @param password password of the user
     * @param userName username of the user
     * @param role role of the user
     * @param jsonPath json file path to store user details
     */
    public User(String email, String password, String userName,Roles role, String jsonPath) throws JSONException, IOException {

        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = role;
        //addCredentialsToFile(jsonPath,email,userName,password); Changing the user file when called
    }

    /**
     * Method to get password of the user
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Method to password of the user
     * @param password password to match with
     * @return boolean indicating if password matches or not
     */
    public boolean passMatch(String password){

        if(password.equals(this.password)){

            return true;
        }
        return false;
    }

    /**
     * Method to match user name of the user
     * @param userName username of the user to match
     * @return boolean indicating if username matches or not
     */
    public boolean userNameMatch(String userName){

        if(userName.equals(this.userName)){

            return true;
        }

        else {
            return false;
        }
    }

    /**
     * Method to get user name of the user
     * @return
     */
    public String getUserName(){

        return this.userName;
    }

    /**
     * Method to get email of the user
     * @return
     */
    public String getEmail(){

        return this.email;
    }

    /**
     * Method to add user credentials to a file
     * @param jsonPath json file to store credentials
     * @param email email of the user to store
     * @param userName username of the user to store
     * @param passWord password of the user to store
     */
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

    /**
     * Method to load user crendetial from a json file
     * @param jsonPath path of the json file
     * @return Jsonobject having all the user details
     */
    protected static JSONObject loadCredentials(String jsonPath)
            throws IOException, JSONException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(jsonPath)));
        return new JSONObject(jsonBody);
    }

    /**
     * Method to get role of the user
     * @return Role of roles enum
     */
    public Roles getRole(){

        return this.role;
    }

    /**
     * Method to set role of the user
     * @param role role of the user
     */
    public void setRole(Roles role){

        this.role = role;
    }

    /**
     * Method to get email of the user
     * @return email of the user
     */
    protected String getUserEmail(){

        return this.email;
    }

    /**
     * Method to check it other user is same as this user
     * @param user user object to check with
     * @return boolean indicating if a user object is same as this
     */
    public boolean userEquals(User user){

        if(this.email.equals(user.getUserEmail())){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Method to get information pages associated with a user
     * @return
     */
    public ArrayList<InformationPage> getMyPages(){

        return this.myPages;
    }

    /**
     * Method to add a information pages to list of pages created by user.
     * @param page
     */
    public void addMyPage(InformationPage page){

        this.myPages.add(page);
    }



}