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

    @RequestMapping(method = RequestMethod.POST, value = "/userLogin")
    public ResponseEntity<String> userLogin(@RequestBody String user)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);

        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();

        Boolean check = model.userLogin(userEmail, password, "user");

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(401).body("Failed"); // conflit status code
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user", params = { "userEmail"})
    public ResponseEntity<String> deleteUserFromSystem(@RequestParam String userEmail) throws JSONException {

        Boolean check = model.deleteUserSystem(userEmail);

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user", params = { "userName", "pageName" })
    public ResponseEntity<String> deleteUserFromAPage(@RequestParam String userName, @RequestParam String pageName) {
        Boolean check = false;

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public String getUserList() throws IOException, JSONException {
        JSONObject json = Model.loadInitialState("src/main/resources/static/users.json");
        String data = json.toString();
        return data;
    }

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

    @RequestMapping(method = RequestMethod.POST, value = "/page", params = { "pageName" })
    public ResponseEntity<String> createPage(@RequestParam String pageName) {

        Boolean check = false;

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }

    }

    // get all pages
    @RequestMapping(method = RequestMethod.GET, value = "/page", params = { "userName" })
    public ResponseEntity<String> getUserPages(@RequestParam String userName) {
        Boolean check = false;

        if (check) {
            return ResponseEntity.ok("Success");// attach body from model response
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }
    }

    // write to page
    @RequestMapping(method = RequestMethod.PUT, value = "/page")
    public ResponseEntity<String> updatePageContent(@RequestBody String user)
            throws JsonMappingException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);

        String userName = jsonNode.get("userName").asText();
        String userEmail = jsonNode.get("userEmail").asText();
        String password = jsonNode.get("password").asText();

        Boolean check = false;

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }

    }

    // editUserAccessAdd
    @RequestMapping(method = RequestMethod.PUT, value = "/page", params = { "pageName", "userName" })
    public ResponseEntity<String> editUserAccessAdd(@RequestParam String pageName, @RequestParam String userName) {
        Boolean check = false;

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }

    }

    // editUserAccessRemove
    @RequestMapping(method = RequestMethod.DELETE, value = "/page", params = { "pageName", "userName" })
    public ResponseEntity<String> editUserAccessRemove(@RequestParam String userName, @RequestParam String pageName) {
        Boolean check = false;

        if (check) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(400).body("Failed"); // conflit status code
        }
    }

    // ReadAccessAdd/add to page
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
