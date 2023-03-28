package com.shared_communication.backend;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shared_communication.backend.Model;


@RestController
public class IndexController {
    Model model = new Model("src/main/resources/static/users.json", "src/main/resources/static/admin.json");

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String welcome() {
        return "welcome";
    }

    /**API Endpoint to register user.
     * @param user stores request body
     * @return status code and message
     * @throws JsonProcessingException
     * @throws JSONException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/user")
    public ResponseEntity<String>  userRegister(@RequestBody String user) throws JsonProcessingException, JSONException {


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);

        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();
        
        
        System.out.println(user);
        System.out.println(userName);

        Boolean check = model.registerUser(userName, userEmail, password, "user");

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }

    }

    /**API Endpoint to register Admin
     * @param user stores request body
     * @return status code and message
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws JSONException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/admin")
    public ResponseEntity<String> adminRegister(@RequestBody String user)
            throws JsonMappingException, JsonProcessingException, JSONException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);

        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();

        Boolean check = false;
        
            check = model.registerUser(userName, userEmail, password, "admin");
        

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }
    }

    /**API Endpoint to verify admin login 
     * @param user stores request body
     * @return status code with message 
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/adminLogin")
    public ResponseEntity<String> adminLogin(@RequestBody String user)
            throws JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);

        // String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();
            // System.out.println(userEmail);
            // System.out.println(password);

        Boolean check = model.userLogin(userEmail, password, "admin");

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(401).body("Failed"); // conflit status code
        }
    }

    /**API Endpoint to verify admin login 
     * @param user stores request body
     * @return status code with message 
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/userLogin")
    public ResponseEntity<String> userLogin(@RequestBody String user)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);

//        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();

        Boolean check = model.userLogin(userEmail, password, "user");

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(401).body("Failed"); // conflit status code
        }
    }

    /**API Endpoint to delete user from system
     * @param userEmail the user that is to be deleted
     * @return
     * @throws JSONException
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/user", params = { "userEmail"})
    public ResponseEntity<String> deleteUserFromSystem(@RequestParam String userEmail) throws JSONException {

        Boolean check = model.deleteUserSystem(userEmail);

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }

    }

    /** API Endpoint to delete user from a page
     * @param userName stores user name of user to be deleted
     * @param pageName stores page name from which user has to be deleted
     * @return status message with status
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/user", params = { "userName", "pageName" })
    public ResponseEntity<String> deleteUserFromAPage(@RequestParam String userName, @RequestParam String pageName) {
        Boolean check = false;

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }
    }

    /**API Endpoint to get all users 
     * @return all the users in the system except admins
     * @throws IOException
     * @throws JSONException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public String getUserList() throws IOException, JSONException {
        JSONObject json = Model.loadInitialState("src/main/resources/static/users.json");
        String data = json.toString();
        return data;
    }

    /**API to get a particular user details
     * @param userEmail stores the email of the user to be returned
     * @return the user details as JSON string 
     */
    @RequestMapping(method = RequestMethod.GET, value = "/user", params = { "userEmail" })
    public ResponseEntity<String> getUser(@RequestParam String userEmail) {

        //System.out.println(model.getUser(userEmail).length());
        String user = (model.getUser(userEmail)).toString();
        
        if (user.length() != 0) {
            return ResponseEntity.status(200).body(user); // attach response from model
        } else {
            return ResponseEntity.status(404).body(user); // conflit status code
        }
    }

    /**API Endpoint to update user
     * @param body stores request body 
     * @return status code with status message
     * @throws JSONException
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.POST , value = "/user/update")
    public ResponseEntity<String> updateUser(@RequestBody String body) throws JSONException, JsonMappingException, JsonProcessingException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        
        String userEmail = jsonNode.get("userEmail").asText();
        String role = jsonNode.get("currentRole").asText();
        

        Boolean check = model.updateUser(userEmail, role);

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }
    }

    /**API End point to create page
     * @param body stores request body
     * @return status code with status message
     * @throws IOException
     * @throws JSONException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public ResponseEntity<String> createPage(@RequestBody String body) throws IOException, JSONException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);

        String userName = jsonNode.get("userEmail").asText();
        String pageName = jsonNode.get("pageName").asText();

        try{
            model.createNewPage(userName,pageName);
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    
    /**API End point to get all pages for a user
     * @param userEmail the user email to identify user
     * @return list of all pages as JSON string
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page", params = { "userEmail" })
    public ResponseEntity<String> getUserPages(@RequestParam String userEmail) {

        try{

            String result = model.getUserCreatedPages(userEmail).toString();
            return ResponseEntity.status(200).body(result);
        } catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage()); // conflit status code
        }

    }

    /**API End point to get all pages from the system
     * @param userEmail the user email of an admin
     * @return list of all pages as JSON string
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/admin", params = { "userEmail" })
    public ResponseEntity<String> getAllPages(@RequestParam String userEmail) {

        try{

            String result = model.getAllPages(userEmail).toString();
            return ResponseEntity.status(200).body(result);
        } catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage()); // conflit status code
        }

    }

    /**API End point to get details of a page
     * @param title of the page for which details to be fetched
     * @return list of all pages as JSON string
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/info", params = { "title" })
    public ResponseEntity<String> getPageDetail(@RequestParam String title) {

        try{

            JSONObject page = model.getPageInfo(title);
            return ResponseEntity.status(200).body(page.toString());
        } catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage()); // conflit status code
        }

    }

    
    /**API Endpoint to update page content
     * @param body stores the request body with content to be added
     * @return status code with status message
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/page/content")
    public ResponseEntity<String> updatePageContent(@RequestBody String body)
            throws JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);

        String content = jsonNode.get("content").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String title = jsonNode.get("pageName").asText();

        try{
            model.addPostToPage(title, userEmail,content);
            return ResponseEntity.ok("Success");
        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage()); // conflit status code
        }


    }

    
    /**API Endpoinnt to add a user to edit access
     * @param body had the request body 
     * @return status code with status Message
     * @throws JSONException
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/page/access")
    public ResponseEntity<String> editUserAccessAdd(@RequestBody String body) throws JSONException, IOException {


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);

        String userEmail = jsonNode.get("userEmail").asText();
        String pageName = jsonNode.get("pageName").asText();

        try{
            model.addUser(pageName,userEmail);
            return ResponseEntity.ok("Success");

        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage());

        }
    }

   
    /**API Endpoint to remove users access to edit page
     * @param body has the request body 
     * @return status code with status message
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/page/access")
    public ResponseEntity<String> editUserAccessRemove(@RequestBody String body) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);

        String userEmail = jsonNode.get("userEmail").asText();
        String pageName = jsonNode.get("pageName").asText();


        try{
            model.deleteUser(pageName,userEmail);
            return ResponseEntity.ok("Success");

        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage());

        }
    }

    
    /**API Endpoint to add user to a page
     * @param userName the user to be added 
     * @param pageName the page the user had to be added
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/page", params = { "pageName", "userName" })
    public ResponseEntity<String> UserAccessAdd(@RequestParam String userName, @RequestParam String pageName) {
        Boolean check = false;

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }
    }

}
