package com.shared_communication.backend;

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


@RestController
public class IndexController {

    @RequestMapping(method = RequestMethod.GET,value="/")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user")
    public ResponseEntity<String> userRegister(@RequestBody String user) throws JsonProcessingException{
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);
        
        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();



        //invoke model
        Boolean check = false ;
        
        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/admin")
    public ResponseEntity<String> adminRegister(@RequestBody String user) throws JsonMappingException, JsonProcessingException{
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);
        
        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();

        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/adminLogin")
    public ResponseEntity<String> adminLogin(@RequestBody String user) throws JsonMappingException, JsonProcessingException{
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);
        
        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();
        
        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/userLogin")
    public ResponseEntity<String> userLogin(@RequestBody String user) throws JsonMappingException, JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);
        
        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();

        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }

    @RequestMapping(method = RequestMethod.DELETE,value ="/user",params = {"userName"} )
    public ResponseEntity<String> deleteUserFromSystem(@RequestParam String userName) {

        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
        
    }

    @RequestMapping(method = RequestMethod.DELETE,value ="/user",params = {"userName","pageName"} )
    public ResponseEntity<String> deleteUserFromAPage(@RequestParam String userName,@RequestParam String pageName) {
        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }

    @RequestMapping(method = RequestMethod.GET,value="/users")
    
    public String getUserList() {
        return "users.json";
    }

    @RequestMapping(method = RequestMethod.GET,value="/user",params = {"userName"})
    public ResponseEntity<String> getUser(@RequestParam String userName) {
        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success"); //attach response from model
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }

    @RequestMapping(method = RequestMethod.PUT,value="/user",params = {"userName"})
    public ResponseEntity<String> updateUser(@RequestParam String userName) {
        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }
    

    @RequestMapping(method = RequestMethod.POST, value = "/page",params = {"pageName"})
    public ResponseEntity<String> createPage(@RequestParam String pageName) {

        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
        
    }

    //get all pages
    @RequestMapping(method = RequestMethod.GET, value = "/page",params = {"userName"})
    public ResponseEntity<String> getUserPages(@RequestParam String userName) {
        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");//attach body from model response
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }

    //write to page
    @RequestMapping(method = RequestMethod.PUT, value = "/page")
    public ResponseEntity<String> updatePageContent(@RequestBody String user) throws JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);
        
        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();

        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
        
    }  

    //editUserAccessAdd
    @RequestMapping(method = RequestMethod.PUT, value = "/page",params = {"pageName","userName"})
    public ResponseEntity<String> editUserAccessAdd(@RequestParam String pageName, @RequestParam String userName) {
        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
        
    }
    //editUserAccessRemove
    @RequestMapping(method = RequestMethod.DELETE, value = "/page",params = {"pageName","userName"})
    public ResponseEntity<String> editUserAccessRemove(@RequestParam String userName,@RequestParam String pageName) {
        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }

    //ReadAccessAdd/add to page
    @RequestMapping(method = RequestMethod.POST, value = "/page",params = {"pageName","userName"})
    public ResponseEntity<String> UserAccessAdd(@RequestParam String userName,@RequestParam String pageName) {
        Boolean check = false ;

        if(check){
            return ResponseEntity.ok("Success");
        }else{
            return ResponseEntity.status(400).body("Failed"); //conflit status code
        }
    }

}
 