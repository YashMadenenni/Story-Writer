package com.shared_communication.backend;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTests {

    User userA = new User("abc@example.com", "passA", "userA", Roles.StandardUser, "./src/main/resources/static/test.json");
    User userB = new User("abcB@example.com", "passB", "userB", Roles.StandardUser, "./src/main/resources/static/test.json");

    InformationPage pageExample = new InformationPage("Title", userB,"./src/main/resources/static/pagetestmodel.json");
    User admin = new User("admin@example.com", "admin", "admin", Roles.SystemAdmin, "./src/main/resources/static/test.json");
    User pageAuthor = new User("author@example.com", "author", "author", Roles.StandardUser, "./src/main/resources/static/test.json");
    User notAllowedUser = new User("nallowed@example.com", "na", "na", Roles.StandardUser, "./src/main/resources/static/test.json");
    InformationPage pageUser = new InformationPage("Title2", userA,"./src/main/resources/static/pagetestmodel.json");

    User readerUser = new User("readerU@example.com", "passA", "readerU", Roles.StandardUser, "./src/main/resources/static/test.json");
    User writerUser = new User("writerU@example.com", "passB", "writerU", Roles.StandardUser, "./src/main/resources/static/test.json");


    Model testModel = new Model("dummy path");

    public ModelTests() throws JSONException, IOException {
    }

    @AfterEach
    public void resetCreds() throws IOException, JSONException {

        testModel.setPagePath("./src/main/resources/static/pages.json");
        org.json.JSONObject credToAdd = new org.json.JSONObject();
        org.json.JSONObject userPass = new JSONObject();
        userPass.put("test", "test");
        credToAdd.put("test@example.com", userPass);
        FileWriter file = new FileWriter("./src/main/resources/static/test.json");
        file.write(credToAdd.toString());
        file.flush();
        file.close();
    }

    @Test
    public void testSuccesfulPageCreaion() throws JSONException, IOException {

        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        testModel.createNewPage(userC,"Test tile for new page","./src/main/resources/static/pagetestmodel.json");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/pagetestmodel.json")));
        JSONObject testPage = new JSONObject(jsonBody);
        String foundKey = null;
        for (Iterator it = testPage.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            JSONObject temp = testPage.getJSONObject(key);
            if(temp.getString("title").equals("Test tile for new page")){
                foundKey = key;
            }
        }
        JSONObject pageObj = testPage.getJSONObject(foundKey);
        assertEquals(pageObj.get("title"),"Test tile for new page");
        assertEquals(pageObj.get("admin"),userC.getEmail());
    }

    @Test
    public void testGetPagePath(){

        assertEquals(testModel.getPagePath(),"./src/main/resources/static/pages.json");
    }

    @Test
    public void testSetPagePath(){

        testModel.setPagePath("new_path");
        assertEquals(testModel.getPagePath(),"new_path");
    }
    @Test
    public void testLoadingPageSuccessful() throws JSONException, IOException {

        JSONObject  json = testModel.loadPages();
        assertNotNull(json);


    }

    @Test
    public void testGetPageUsers() throws JSONException, IOException {

        ArrayList<String> users = testModel.getPageUsers("Title");
        assertTrue(users.contains("abcB@example.com"));
    }

    @Test
    public void testGetPageTitle() throws JSONException, IOException {

        ArrayList<String> title = testModel.getAllPageTitles();
        assertTrue(title.contains("Title"));

    }

    @Test
    public void testAddPost() throws JSONException, IOException {

        testModel.addPostToPage("Title",userB,"First post by B");
        assertTrue(testModel.getPagePosts("Title").contains("First post by B"));
    }


    @Test
    public void testAddPageUsers() throws JSONException, IOException {


        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        ArrayList<String> currUsers = testModel.getPageUsers("Title");
        assertFalse(currUsers.contains(userC.getEmail()));
        testModel.addUser("Title",userC.getEmail());
        assertTrue(currUsers.contains(userC.getEmail()));
    }

    @Test
    public void testDeletePageUsers() throws JSONException, IOException {


        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        ArrayList<String> currUsers = testModel.getPageUsers("Title");
        assertFalse(currUsers.contains(userC.getEmail()));
        testModel.addUser("Title",userC.getEmail());
        assertTrue(currUsers.contains(userC.getEmail()));
        testModel.deleteUser("Title",userC.getEmail());
        assertFalse(currUsers.contains(userC.getEmail()));
    }
    @Test
    public void testGetPagePosts() throws JSONException, IOException {

        testModel.addPostToPage("Title",userB,"First post by B");
        testModel.addPostToPage("Title",userB,"Second post by B");
        testModel.addPostToPage("Title",userB,"Third post by B");
        assertEquals(testModel.getPagePosts("Title").size(),3);



    }





}
