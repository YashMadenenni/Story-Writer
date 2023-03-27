package com.shared_communication.backend;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
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



        JSONObject pageToAdd = new JSONObject();
        JSONObject top = new JSONObject();
        pageToAdd.put("editAccessUser",new JSONArray(List.of("a@example.com")));
        pageToAdd.put("admin","a@example.com");
        pageToAdd.put("title","Notitle");
        pageToAdd.put("content","");
        top.put("page1",pageToAdd);

        JSONObject pageToAddb = new JSONObject();

        pageToAddb.put("editAccessUser",new JSONArray(List.of("abcB@example.com")));
        pageToAddb.put("admin","abcB@example.com");
        pageToAddb.put("title","Title");
        pageToAddb.put("content","");
        top.put("page2",pageToAddb);

        JSONObject pageToAdda = new JSONObject();

        pageToAdda.put("editAccessUser",new JSONArray(List.of("abc@example.com")));
        pageToAdda.put("admin","abc@example.com");
        pageToAdda.put("title","Title2");
        pageToAdda.put("content","");
        top.put("page3",pageToAdda);

        FileWriter pagefile = new FileWriter("./src/main/resources/static/pagetestmodel.json");
        pagefile.write(top.toString());
        pagefile.flush();
        pagefile.close();


    }


    @Test
    public void testSuccesfulPageCreaion() throws JSONException, IOException {

        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        testModel.createNewPage("test@gmail.com","Test tile for new page");
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
        }
    @BeforeEach
    public void setup(){
        testModel = new Model("src/test/resources/userTest.json","src/test/resources/adminTest.json");

    }

    @Test
    public void shouldNotOpenJson(){
        NoSuchFileException thrown = assertThrowsExactly(NoSuchFileException.class, ()->{Model.loadInitialState("wrongpath.json");});
    }

    @Test
    public void shouldOpenJson(){
        org.json.JSONObject result = null;
        try {
            result = Model.loadInitialState("src/test/resources/userTest.json");
        } catch (Exception e) {
            // TODO: handle exception
        }
        // JSONObject pageObj = testPage.getJSONObject(foundKey);
        // assertEquals(pageObj.get("title"),"Test tile for new page");
        // assertEquals(pageObj.get("admin"),userC.getEmail());
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

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> users = testModel.getPageUsers("Title");
        assertTrue(users.contains("abcB@example.com"));
    }

    @Test
    public void testGetPageTitle() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> title = testModel.getAllPageTitles();
        assertTrue(title.contains("Title"));

    }

    @Test
    public void testAddPost() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        testModel.addPostToPage("Title",userB,"First post by B");
        assertTrue(testModel.getPagePosts("Title").contains("First post by B"));
    }


    @Test
    public void testAddPageUsers() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        InformationPage pageUserAdd = new InformationPage("Title101", userB,"./src/main/resources/static/pagetestmodel.json");

        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        ArrayList<String> currUsers = testModel.getPageUsers("Title101");
        assertFalse(currUsers.contains(userC.getEmail()));
        testModel.addUser("Title101",userC.getEmail());
        currUsers = testModel.getPageUsers("Title101");
        assertTrue(currUsers.contains(userC.getEmail()));
    }

    @Test
    public void testDeletePageUsers() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        InformationPage pageUserAdd = new InformationPage("Title101", userB,"./src/main/resources/static/pagetestmodel.json");

        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        ArrayList<String> currUsers = testModel.getPageUsers("Title101");
        assertFalse(currUsers.contains(userC.getEmail()));
        testModel.addUser("Title101",userC.getEmail());
        currUsers = testModel.getPageUsers("Title101");
        assertTrue(currUsers.contains(userC.getEmail()));
        testModel.deleteUser("Title101",userC.getEmail());
        currUsers = testModel.getPageUsers("Title101");
        assertFalse(currUsers.contains(userC.getEmail()));
    }
    public void shouldOpenAdminJson(){
        org.json.JSONObject result = null;
        try {
            result = Model.loadInitialState("src/test/resources/adminTest.json");
        } catch (Exception e) {
            // TODO: handle exception
        }
        assertNotNull(result);
    }

    @Test
    public void testAdminRegister() throws JSONException{
        assertTrue(testModel.registerUser("newAdmin","adminNew@gmail.com","adminpassword","admin"));
    }

    @Test
    public void testAdminRegisterFail() throws JSONException{
        assertTrue(testModel.registerUser("adminTest1","admin1@gmail.com","adminpassword","admin"));
        assertFalse(testModel.registerUser("adminTest2","admin1@gmail.com","adminpassword","admin"));
    }
    @Test
    public void testAdminLoginSuccess(){
        assertTrue(testModel.userLogin("admin@gmail.com","adminpassword","admin"));
    }

    @Test
    public void testAdminLoginFail(){
        assertFalse(testModel.userLogin("admin2@gmail.com","adminwrong","Admin2"));
    }
    @Test
    public void testUserRegister() throws JSONException{
        assertTrue(testModel.registerUser("newUser","userNew@gmail.com","userpassword","user"));
    }

    @Test
    public void testUserRegisterFail() throws JSONException{
        assertTrue(testModel.registerUser("userTest1","user1@gmail.com","userTest","user"));
        assertFalse(testModel.registerUser("userTest2","user1@gmail.com","userTest","user"));
    }
    @Test
    public void testUserLoginSuccess(){
        assertTrue(testModel.userLogin("user@gmail.com","userpassword","user"));
    }

    @Test
    public void testUserLoginFail(){
        assertFalse(testModel.userLogin("User2@gmail.com","userwrong","user"));
    }

    @Test
    public void removeUserFromSystem() throws JSONException{
        assertTrue(testModel.deleteUserSystem("user3@gmail.com"));
    }

    @Test
    public void removeUserFromSystemFail() throws JSONException{
        assertFalse(testModel.deleteUserSystem("user5@gmail.com"));
    }
    @Test
    public void testGetPagePosts() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        testModel.addPostToPage("Title",userB,"First post by B");
        testModel.addPostToPage("Title",userB,"Second post by B");
        testModel.addPostToPage("Title",userB,"Third post by B");
        assertEquals(testModel.getPagePosts("Title").size(),3);



    }





}
