package cs5031.shared_communication.api;

import java.io.IOException;
import java.util.ArrayList;

import cs5031.shared_communication.model.Model;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


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
            model.addUserEditAccess(pageName,userEmail);
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
            model.deleteUserEditAccess(pageName,userEmail);
            return ResponseEntity.ok("Success");

        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage());

        }
    }

    
    /**API Endpoint to add user to a read access list of the page
     * * @param body has the request body
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.POST, value = "/page/access/user")
    public ResponseEntity<String> readUserAccessAdd(@RequestBody String body) throws JsonProcessingException {
        System.out.println("first here");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);

        String userEmail = jsonNode.get("userEmail").asText();
        String pageName = jsonNode.get("pageName").asText();

        try{
            model.addUserReadAccess(pageName,userEmail);
            return ResponseEntity.ok("Success");

        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage());

        }
    }

    /**API Endpoint to remove a user from read access list of a page
     * @param body has the request body
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/page/access/user")
    public ResponseEntity<String> readUserRemoveAdd(@RequestBody String body) throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);

        String userEmail = jsonNode.get("userEmail").asText();
        String pageName = jsonNode.get("pageName").asText();

        try{
            model.deleteUserReadAccess(pageName,userEmail);
            return ResponseEntity.ok("Success");

        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage());

        }
    }


    /**API Endpoint to get all the pages a user can read from
     * @param body has the request body
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/access/my")
    public ResponseEntity<String> getMyReadPages(@RequestBody String body) throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        ArrayList<String> output= new ArrayList<>();

        String userEmail = jsonNode.get("userEmail").asText();
        System.out.println(userEmail);
        try{
            output = model.getMyReadPages(userEmail);
            return ResponseEntity.ok(output.toString());

        }catch (Exception e){

            return ResponseEntity.status(400).body(""+output.size());

        }
    }


    /**API Endpoint to get all the pages a user can write to
     * @param body has the request body
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/access/mywrite")
    public ResponseEntity<String> getMyWritePages(@RequestBody String body) throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        ArrayList<String> output= new ArrayList<>();

        String userEmail = jsonNode.get("userEmail").asText();
        System.out.println(userEmail);
        try{
            output = model.getMyWritePages(userEmail);
            return ResponseEntity.ok(output.toString());

        }catch (Exception e){

            return ResponseEntity.status(400).body(""+output.size());

        }
    }


    /**API Endpoint to add a message byy admin to a json file which can be shared with all the users
     * @param body has the request body
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.POST, value = "/page/admin/message")
    public ResponseEntity<String> addAdminMessage(@RequestBody String body) throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        ArrayList<String> output= new ArrayList<>();

        String userEmail = jsonNode.get("userEmail").asText();
        String userMessage = jsonNode.get("message").asText();
        try{
            model.addAdminMessage(userEmail,userMessage);
            return ResponseEntity.ok("Success");

        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage());

        }
    }

    /**API Endpoint to get message shared by admin
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/admin/message")
    public ResponseEntity<String> getAdminMessage() throws JsonProcessingException {

        try{
            String adminMessage = model.getAdminMessage();
            return ResponseEntity.ok(adminMessage.toString());

        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage());

        }
    }


    /**API Endpoint to remove users access to edit page
     * @param userEmail is the request params
     * @param pageName is the request params
     * @return status code with status message
     * @throws JsonProcessingException
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/page/access/gui", params = { "userEmail", "pageName" })
    public ResponseEntity<String> editUserAccessRemoveGui(@RequestParam String userEmail, @RequestParam String pageName) {

        try{
            model.deleteUserEditAccess(pageName,userEmail);
            return ResponseEntity.ok("Success");

        }catch (Exception e){

            return ResponseEntity.status(400).body(e.getMessage());

        }
    }

    /**API Endpoint to remove a user from read access list of a page
     * @param userEmail is the request params
     * @param pageName is the request params
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/page/access/user/gui", params = { "userEmail", "pageName" })
    public ResponseEntity<String> readUserRemoveAddGui(@RequestParam String userEmail, @RequestParam String pageName) {

        try{
            model.deleteUserReadAccess(pageName,userEmail);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**API Endpoint to get all the pages a user can read from
     * @param userEmail has the request params
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/access/my/gui", params = { "userEmail" })
    public ResponseEntity<ArrayList<String>> getMyReadPagesGui(@RequestParam String userEmail) {

        try{
            ArrayList<String> result = model.getMyReadPages(userEmail);
            return ResponseEntity.status(200).body(result);
        } catch(Exception e){
            ArrayList<String> a = new ArrayList<>();
            a.add(e.getMessage());
            return ResponseEntity.status(400).body(a); // conflit status code
        }
    }


    /**API Endpoint to get all the pages a user can write to
     * @param userEmail has the request params
     * @return status code with status message
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page/access/mywrite/gui", params = { "userEmail" })
    public ResponseEntity<ArrayList<String>> getMyWritePagesGui(@RequestParam String userEmail) {

        try{
            ArrayList<String> result = model.getMyWritePages(userEmail);
            return ResponseEntity.status(200).body(result);
        } catch(Exception e){
            ArrayList<String> a = new ArrayList<>();
            a.add(e.getMessage());
            return ResponseEntity.status(400).body(a); // conflit status code
        }
    }

    
}
