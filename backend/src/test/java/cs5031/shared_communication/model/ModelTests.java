package cs5031.shared_communication.model;

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
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Teest cases for model
 */
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

    /**
     * Setting environment after each test run
     * @throws IOException
     * @throws JSONException
     */
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
        pageToAdd.put("readAccessUser",new JSONArray(List.of("a@example.com")));
        pageToAdd.put("admin","a@example.com");
        pageToAdd.put("title","Notitle");
        pageToAdd.put("content","");
        top.put("page1",pageToAdd);

        JSONObject pageToAddb = new JSONObject();

        pageToAddb.put("editAccessUser",new JSONArray(List.of("abcB@example.com")));
        pageToAddb.put("readAccessUser",new JSONArray(List.of("abcB@example.com")));
        pageToAddb.put("admin","abcB@example.com");
        pageToAddb.put("title","Title");
        pageToAddb.put("content","");
        top.put("page2",pageToAddb);

        JSONObject pageToAdda = new JSONObject();

        pageToAdda.put("editAccessUser",new JSONArray(List.of("abc@example.com")));
        pageToAdda.put("readAccessUser",new JSONArray(List.of("abc@example.com")));
        pageToAdda.put("admin","abc@example.com");
        pageToAdda.put("title","Title2");
        pageToAdda.put("content","");
        top.put("page3",pageToAdda);

        FileWriter pagefile = new FileWriter("./src/main/resources/static/pagetestmodel.json");
        pagefile.write(top.toString());
        pagefile.flush();
        pagefile.close();


    }

    /**
     * Tes tot check page creation by user
     * @throws JSONException
     * @throws IOException
     */
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

    /**
     * Test to ensure page creation fail when creating same page
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testFailedPageCreation() throws JSONException, IOException {

        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        testModel.createNewPage("test@gmail.com","Test tile for new page");

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    testModel.createNewPage("test@gmail.com","Test tile for new page");;

                });

    }

    /**
     * Environment setup before running model tests
     */
    @BeforeEach
    public void setup(){
        testModel = new Model("src/test/resources/userTest.json","src/test/resources/adminTest.json");

    }

    /**
     * Test case for checking error when trying to open invalid json
     */
    @Test
    public void shouldNotOpenJson(){
        NoSuchFileException thrown = assertThrowsExactly(NoSuchFileException.class, ()->{Model.loadInitialState("wrongpath.json");});
    }

    /**
     * Test case for successful opening of a json file
     */
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

    /**
     * Test to check correct json page path returned
     */
    @Test
    public void testGetPagePath(){

        assertEquals(testModel.getPagePath(),"./src/main/resources/static/pages.json");
    }

    /**
     * Test to set correct json path is set
     */
    @Test
    public void testSetPagePath(){

        testModel.setPagePath("new_path");
        assertEquals(testModel.getPagePath(),"new_path");
    }

    /**
     * Test to check successful loading of json file
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testLoadingPageSuccessful() throws JSONException, IOException {

        JSONObject  json = testModel.loadPages();
        assertNotNull(json);


    }

    /**
     * Test to check loading fails of invalid file
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testLoadingPageFailed() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/notvalid.json");
        NoSuchFileException thrown =
                assertThrowsExactly(NoSuchFileException.class, () -> {
                    JSONObject  json = testModel.loadPages();

                });
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");



    }

    /**
     * Test to get page users
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetPageUsers() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> users = testModel.getPageEditUsers("Title");
        assertTrue(users.contains("abcB@example.com"));
    }

    /**
     * Test to get page title
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetPageTitle() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> title = testModel.getAllPageTitles();
        assertTrue(title.contains("Title"));

    }

    /**
     * Failed test to get page title
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetPageTitleFailed() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> title = testModel.getAllPageTitles();
        assertFalse(title.contains("Titl"));

    }

    /**
     * Successful page title retrieval
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetAllPageTitlesSuccessfully() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> titles = testModel.getAllPageTitles();
        assertTrue(titles.contains("Title2"));
        assertTrue(titles.contains("Title"));
    }

    /**
     * Test to get all page titles from a list
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetAllPageTitlesNotInList() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> titles = testModel.getAllPageTitles();
        assertFalse(titles.contains("does not exists"));

    }

    /**
     * Test to check post can be added to page
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testAddPost() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        //testModel.addUserEditAccess("Title","abcB@example.com");
        testModel.addPostToPage("Title",userB,"First post by B");
        assertTrue(testModel.getPagePosts("Title").contains("First post by B"));
    }

    /**
     * Test to check adding new post fails
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testAddPostFailed() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        //testModel.addUserEditAccess("Title","abcB@example.com");
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    testModel.addPostToPage("Title",userA,"First post by A");

                });
    }

    /**
     * Test to get information of a page
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetPageInfo() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        JSONObject page = testModel.getPageInfo("Title2");
        page.has("title");
    }

    /**
     * Tes to check error when loading non-existing page
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetPageInfoFailed() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    JSONObject page = testModel.getPageInfo("does not exists");

                });

    }


    /**
     * Test to add users to a page
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testAddPageUsers() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        InformationPage pageUserAdd = new InformationPage("Title101", userB,"./src/main/resources/static/pagetestmodel.json");
        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        ArrayList<String> currUsers = testModel.getPageEditUsers("Title101");
        assertFalse(currUsers.contains(userC.getEmail()));
        testModel.addUserEditAccess("Title101",userC.getEmail());
        currUsers = testModel.getPageEditUsers("Title101");
        assertTrue(currUsers.contains(userC.getEmail()));
    }

    /**
     * Test to delete page users
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testDeletePageUsers() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        InformationPage pageUserAdd = new InformationPage("Title101", userB,"./src/main/resources/static/pagetestmodel.json");

        User userC = new User("abcC@example.com", "passC", "userC", Roles.StandardUser, "./src/main/resources/static/test.json");
        ArrayList<String> currUsers = testModel.getPageEditUsers("Title101");
        assertFalse(currUsers.contains(userC.getEmail()));
        testModel.addUserEditAccess("Title101",userC.getEmail());
        currUsers = testModel.getPageEditUsers("Title101");
        assertTrue(currUsers.contains(userC.getEmail()));
        testModel.deleteUserEditAccess("Title101",userC.getEmail());
        currUsers = testModel.getPageEditUsers("Title101");
        assertFalse(currUsers.contains(userC.getEmail()));
    }

    /**
     * Test case for not allowing to open invalid Admin json
     */
    public void shouldOpenAdminJson(){
        org.json.JSONObject result = null;
        try {
            result = Model.loadInitialState("src/test/resources/adminTest.json");
        } catch (Exception e) {
            // TODO: handle exception
        }
        assertNotNull(result);
    }

    /**
     * Test case to check fucntionality of registering an admin
     * @throws JSONException
     */
    @Test
    public void testAdminRegister() throws JSONException{
        assertTrue(testModel.registerUser("newAdmin","adminNew@gmail.com","adminpassword","admin"));
    }

    /**
     * Test case to check admin registration fails when invalid details are provided
     * @throws JSONException
     */
    @Test
    public void testAdminRegisterFail() throws JSONException{
        assertTrue(testModel.registerUser("adminTest1","admin1@gmail.com","adminpassword","admin"));
        assertFalse(testModel.registerUser("adminTest2","admin1@gmail.com","adminpassword","admin"));
    }

    /**
     * Test case to check admin able to login successfully
     */
    @Test
    public void testAdminLoginSuccess(){
        assertTrue(testModel.userLogin("admin@gmail.com","adminpassword","admin"));
    }

    /**
     * Test case to check admin unable to login
     */
    @Test
    public void testAdminLoginFail(){
        assertFalse(testModel.userLogin("admin2@gmail.com","adminwrong","Admin2"));
    }

    /**
     * Test case check user registration method
     * @throws JSONException
     */
    @Test
    public void testUserRegister() throws JSONException{
        assertTrue(testModel.registerUser("newUser","userNew@gmail.com","userpassword","user"));
    }

    /**
     * Test case to check user registration fails
     * @throws JSONException
     */
    @Test
    public void testUserRegisterFail() throws JSONException{
        assertTrue(testModel.registerUser("userTest1","user1@gmail.com","userTest","user"));
        assertFalse(testModel.registerUser("userTest2","user1@gmail.com","userTest","user"));
    }

    /**
     * Test case to ensure user is able to login successfully
     */
    @Test
    public void testUserLoginSuccess(){
        assertTrue(testModel.userLogin("user@gmail.com","userpassword","user"));
    }

    /**
     * Test case to ensure use login fails
     */
    @Test
    public void testUserLoginFail(){
        assertFalse(testModel.userLogin("User2@gmail.com","userwrong","user"));
    }

    /**
     * Test case to remove user from the system
     * @throws JSONException
     */
    @Test
    public void removeUserFromSystem() throws JSONException{
        assertTrue(testModel.deleteUserSystem("user3@gmail.com"));
    }

    /**
     * Test case to remove user from system fails
     * @throws JSONException
     */
    @Test
    public void removeUserFromSystemFail() throws JSONException{
        assertFalse(testModel.deleteUserSystem("user5@gmail.com"));
    }

    /**
     * Tes to check posts added and replaced
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetPagePosts() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        testModel.addPostToPage("Title",userB,"First post by B");
        testModel.addPostToPage("Title",userB,"Second post by B");
        testModel.addPostToPage("Title",userB,"Third post by B");
        assertEquals(testModel.getPagePosts("Title").size(),1);



    }

    /**
     * Test user object returned when asked for an email address
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testgetUserObject() throws JSONException, IOException {

        User user = testModel.getUserObj("test@gmail.com");
        assertEquals(user.getUserEmail(),"test@gmail.com");
    }

    /**
     * Test to check if read access users of a paage can be retrieved
     * @throws JSONException
     * @throws IOException
     */

    @Test
    public void testReadAccess() throws JSONException, IOException {

        ArrayList<String> users = testModel.getPageReadUsers("Title2");
        users.contains("abc@example.com");

    }

    /**
     * Test to check only read access is working
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testOnlyReadAccess() throws JSONException, IOException {

        ArrayList<String> users = testModel.getPageReadUsers("Title2");
        users.contains("readabc@example.com");

    }




    /**
     * Test to add message by admin
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testaddAdminMessage() throws JSONException, IOException {

        testModel.addAdminMessage("adminnew@gmail.com","Admin's new Message");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/usermessage.json")));
        JSONObject json = null;
        json = new JSONObject(jsonBody);
        JSONObject jsonFound = null;
        JSONArray messageArray = (JSONArray) json.get("message");
        for (int i = 0; i < messageArray.length(); i++) {
            JSONObject element = messageArray.getJSONObject(i);

            if(element.get("admin").equals("adminnew@gmail.com")){

                jsonFound = (JSONObject) messageArray.get(i);
            }
        }
        assertTrue(jsonFound.get("admin").equals("adminnew@gmail.com"));


    }

    /**
     * Test to add message by admin
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testaddAdminMessageReplace() throws JSONException, IOException {

        testModel.addAdminMessage("admin@example.com","Admin's new replaced Message");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/usermessage.json")));
        JSONObject json = null;

        json = new JSONObject(jsonBody);
        JSONObject jsonFound = null;
        JSONArray messageArray = (JSONArray) json.get("message");
        for (int i = 0; i < messageArray.length(); i++) {
            JSONObject element = messageArray.getJSONObject(i);

            if(element.get("admin").equals("admin@example.com")){

                jsonFound = (JSONObject) messageArray.get(i);
            }
        }
        assertTrue(jsonFound.get("message").equals("Admin's new replaced Message"));


    }

    /**
     * Test to ensure messages by admin are retrieved
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testRetrieveAdminMessage() throws JSONException, IOException {

        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/usermessage.json")));
        JSONObject json = null;

        json = new JSONObject(jsonBody);
        assertTrue(json.has("message"));


    }

    /**
     * Test to get messages of amdin from file
     * @throws JSONException
     * @throws IOException
     */

    @Test
    public void testGetAdminMessage() throws JSONException, IOException {

        String jsonBody = testModel.getAdminMessage();
        jsonBody.contains("message");



    }


    /**
     * Testing if reading access working on edge cases
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testAddingReadAccess() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> readUsers = testModel.getPageReadUsers("Title");
        assertFalse(readUsers.contains("xyz@example.com"));
        testModel.addUserReadAccess("Title","xyz@example.com");
        ArrayList<String> readUsersnew = testModel.getPageReadUsers("Title");

        assertTrue(readUsersnew.contains("xyz@example.com"));


    }

    /**
     * Testing if write access working on edge cases
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testAddingWriteAccess() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> readUsers = testModel.getPageEditUsers("Title");
        assertFalse(readUsers.contains("xyz@example.com"));
        testModel.addUserReadAccess("Title","xyz@example.com");
        ArrayList<String> readUsersnew = testModel.getPageEditUsers("Title");
        assertFalse(readUsersnew.contains("xyz@example.com"));
        testModel.addUserEditAccess("Title","xyz@example.com");
        ArrayList<String> readUsersedit = testModel.getPageEditUsers("Title");
        assertTrue(readUsersedit.contains("xyz@example.com"));

    }

    /**
     * Testing is read and write are working on edge cases
     * @throws JSONException
     * @throws IOException
     */

    @Test
    public void testAddingWriteAddReadAccess() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        ArrayList<String> readUsers = testModel.getPageEditUsers("Title");
        assertFalse(readUsers.contains("xyz@example.com"));
        testModel.addUserEditAccess("Title","xyz@example.com");
        ArrayList<String> readUsersedit = testModel.getPageEditUsers("Title");
        assertTrue(readUsersedit.contains("xyz@example.com"));

    }

    /**
     * Testing page creation on edge cases
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testUserCreatedPages() throws JSONException, IOException {

        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        JSONArray userCreatedPages = testModel.getUserCreatedPages("abcB@example.com");
        assertNotNull(userCreatedPages);

    }

    /**
     * Testing getting all the pages only by admin
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetallPagesSuccesful() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");
        JSONArray pages = testModel.getAllPages("admin@gmail.com");
        assertEquals(pages.length(),5);
    }

    /**
     * Testing getting all the pages only by admin
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetallPagesFailed() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    JSONArray pages = testModel.getAllPages("user@gmail.com");

                });
    }

    /**
     * Test to check user's self read pages can be retrieved
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testMyReadPages() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");

        ArrayList<String> pages = testModel.getMyReadPages("abcB@example.com");
        assertEquals(pages.size(),2);
    }

    /**
     * Test to check if there sre zero read pages
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testMyReadZeroPages() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");

        ArrayList<String> pages = testModel.getMyReadPages("no@example.com");
        assertEquals(pages.size(),0);
    }

    /**
     * Test to get write pages for the user
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testMyWritePages() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");

        ArrayList<String> pages = testModel.getMyWritePages("abcB@example.com");
        assertEquals(pages.size(),2);
    }

    /**
     * Testing edge care of write pages retrieval
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testMyWriteZeroPages() throws JSONException, IOException {
        testModel.setPagePath("./src/main/resources/static/pagetestmodel.json");

        ArrayList<String> pages = testModel.getMyWritePages("np@example.com");
        assertEquals(pages.size(),0);
    }


}
