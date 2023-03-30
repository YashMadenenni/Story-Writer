package cs5031.shared_communication.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class User{

    /**
     *@param email - user email
     *@param password - user password
     *@param userName- user name
     *@param mePages - user pages
     *@param role - user role
     */
    private String email;
    private String password;
    private String userName;
    private ArrayList<InformationPage> myPages = new ArrayList<>();
    private Roles role;

    //constructor
    public User(String email, String password, String userName,Roles role, String jsonPath) throws JSONException, IOException {

        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = role;
        //addCredentialsToFile(jsonPath,email,userName,password); Changing the user file when called
    }

    /**Method get password
     * @return
     */
    public String getPassword() {
        return this.password;
    }
    /**Method validate password match
     * @param password
     * @return
     */
    public boolean passMatch(String password){

        if(password.equals(this.password)){

            return true;
        }
        return false;
    }

    /**Method validate user names
     * @param userName
     * @return
     */
    public boolean userNameMatch(String userName){

        if(userName.equals(this.userName)){

            return true;
        }

        else {
            return false;
        }
    }

    /**Method get user name
     * @return
     */
    public String getUserName(){

        return this.userName;
    }

    /**Method get email
     * @return
     */
    public String getEmail(){

        return this.email;
    }

    /**Add user to file
     * @param jsonPath - user file path
     * @param email
     * @param userName
     * @param passWord
     * @throws IOException
     * @throws JSONException
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

    protected static JSONObject loadCredentials(String jsonPath)
            throws IOException, JSONException {

        String jsonBody = new String(Files.readAllBytes(Paths.get(jsonPath)));
        return new JSONObject(jsonBody);
    }

    /** Method to get role
     * @return
     */
    public Roles getRole(){

        return this.role;
    }

    /**Method to set role
     * @param role
     */
    public void setRole(Roles role){

        this.role = role;
    }

    /**method to return email
     * @return
     */
    protected String getUserEmail(){

        return this.email;
    }

    /**Helper method to comare email
     * @param user
     * @return
     */
    public boolean userEquals(User user){

        if(this.email.equals(user.getUserEmail())){
            return true;
        }
        else{
            return false;
        }
    }

    /**Get Pages method
     * @return list of pages
     */
    public ArrayList<InformationPage> getMyPages(){

        return this.myPages;
    }

    /**Add page method
     * @param page
     */
    public void addMyPage(InformationPage page){

        this.myPages.add(page);
    }



}