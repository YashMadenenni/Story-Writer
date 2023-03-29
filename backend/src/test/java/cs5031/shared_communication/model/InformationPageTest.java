package cs5031.shared_communication.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class InformationPageTest {



    User userA = new User("abc@example.com", "passA", "userA", Roles.StandardUser, "./src/main/resources/static/test.json");
    User userB = new User("abcB@example.com", "passB", "userB", Roles.StandardUser, "./src/main/resources/static/test.json");

    InformationPage pageExample = new InformationPage("Title", userB,"./src/main/resources/static/pagetest.json");
    User admin = new User("admin@example.com", "admin", "admin", Roles.SystemAdmin, "./src/main/resources/static/test.json");
    User pageAuthor = new User("author@example.com", "author", "author", Roles.StandardUser, "./src/main/resources/static/test.json");
    User notAllowedUser = new User("nallowed@example.com", "na", "na", Roles.StandardUser, "./src/main/resources/static/test.json");
    InformationPage pageUser = new InformationPage("Title2", userA,"./src/main/resources/static/pagetest.json");

    User readerUser = new User("readerU@example.com", "passA", "readerU", Roles.StandardUser, "./src/main/resources/static/test.json");
    User writerUser = new User("writerU@example.com", "passB", "writerU", Roles.StandardUser, "./src/main/resources/static/test.json");



    InformationPageTest() throws JSONException, IOException {
    }


    @Test
    public void testUserRegistrationnotNull() {

        assertEquals(pageUser.getAuthor(), userA);
    }

    @Test
    public void testGetAuthorNotNull() {

        assertEquals(pageUser.getAuthor(), userA);

    }


    @Test
    public void testPageWiterUsersZeroInitially() {

        assertEquals(pageExample.getPageWriteUsers().size(), 0);
    }

    @Test
    public void testPageReaderUsersZeroInitially() {

        assertEquals(pageExample.getPageReadUsers().size(), 0);
    }



    @Test
    public void testAddingWriteUserErrorAlreadyAdded() {
        pageExample.addWriteUser(userA);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addWriteUser(userA);
                });
    }

    @Test
    public void testAddingReadUserErrorAlreadyAdded() {
        pageExample.addReadUser(userA);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addReadUser(userA);
                });
    }

    @Test
    public void testRemovingReadUserErrorNotInList() {

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.removeReadUser(userA);
                });
    }

    @Test
    public void testRemovingWriteUserErrorNotInList() {

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.removeWriteUser(userA);
                });
    }

    @Test
    public void testRemovingWriteUserSuccessfully() {

        pageExample.addWriteUser(userA);
        pageExample.removeWriteUser(userA);
        assertEquals(pageExample.getPageWriteUsers().size(), 0);
    }

    @Test
    public void testRemovingReadSuccessfully() {

        pageExample.addReadUser(userA);
        pageExample.removeReadUser(userA);
        assertEquals(pageExample.getPageReadUsers().size(), 0);
    }

    @Test
    public void getPostsSuccessfullyInitialZero() {

        assertEquals(pageExample.getPosts().size(), 0);
    }

    @Test
    public void testAddingPostError() {


        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addPost(userA, "This post will fail");
                    ;
                });
    }


    @Test
    public void testAddingPostSuccessfulAdmin() {

        assertEquals(pageExample.getPosts().size(), 0);
        pageExample.addPost(admin, "Post by admin");
        assertEquals(pageExample.getPosts().size(), 1);


    }

    @Test
    public void testAddingPostSuccessfulAuthor() {


        assertEquals(pageUser.getPosts().size(), 0);
        pageUser.addPost(userA, "Post by author");
        assertEquals(pageUser.getPosts().size(), 1);


    }

    @Test
    public void testAddingPostSuccessfulPageUser() {


        assertEquals(pageUser.getPosts().size(), 0);

        pageUser.addPost(userA, "Post by page user");
        assertEquals(pageUser.getPosts().size(), 1);

    }

    @Test
    public void testNotAllowedDuplicatePostsBySameUser() {


        assertEquals(pageUser.getPosts().size(), 0);

        pageUser.addPost(userA, "Post by page user");

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageUser.addPost(userA, "Post by page user");
                    ;
                });



    }

    @Test
    public void testNotAllowedDuplicatePostsByOtherUser() {


        assertEquals(pageUser.getPosts().size(), 0);

        pageUser.addPost(userA, "Post by page user");
        pageUser.addWriteUser(userB);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageUser.addPost(userB, "Post by page user");
                    ;
                });

    }

    @Test
    public void testPageWriteUsersSuccesfullAdded(){

        pageUser.addWriteUser(writerUser);
        assertEquals(pageUser.getPageWriteUsers().size(),1);
    }

    @Test
    public void testPageReadUsersSuccesfullAdded(){

        pageUser.addReadUser(readerUser);
        assertEquals(pageUser.getPageReadUsers().size(),1);
    }

    @Test
    public void testPageWriteUsersSuccesfulWrite(){

        assertEquals(pageUser.getPosts().size(),0);
        pageUser.addWriteUser(writerUser);
        pageUser.addPost(writerUser,"This will pass");
        assertEquals(pageUser.getPosts().size(),1);
    }

    @Test
    public void testPageWriteUsersFail(){

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageUser.addPost(writerUser,"This will fail");;
                    ;
                });


    }


    @Test
    public void testPageReadUsersFailWhileWrite(){

        pageUser.addReadUser(readerUser);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageUser.addPost(userB, "Post by page user");
                    ;
                });


    }

    @Test
    public void testChangeUserReadToWriteSuccesful(){

        //Initially nowhere
        assertFalse(pageUser.getPageReadUsers().contains(readerUser));
        pageUser.addReadUser(readerUser);
        //Gets added to rader list
        assertTrue(pageUser.getPageReadUsers().contains(readerUser));
        // But not in writer list
        assertFalse(pageUser.getPageWriteUsers().contains(readerUser));
        pageUser.changeReadToWriteUser(readerUser);
        // Gets added to both the lists
        assertTrue(pageUser.getPageReadUsers().contains(readerUser));
        assertTrue(pageUser.getPageWriteUsers().contains(readerUser));


    }


    @Test
    public void testWritingJsonSuccesful() throws JSONException, IOException {

        User userC = new User("userC@example.com", "passC", "usrC", Roles.StandardUser, "./src/main/resources/static/test.json");
        InformationPage page = new InformationPage("Test title user C",userC,"./src/main/resources/static/pagetest.json");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/pagetest.json")));
        JSONObject testPage = new JSONObject(jsonBody);

        String foundKey = null;


        for (Iterator it = testPage.keys(); it.hasNext(); ) {
                String key = (String) it.next();

                JSONObject temp = testPage.getJSONObject(key);

                if(temp.getString("title").equals("Test title user C")){
                    foundKey = key;
                }
            }
        JSONObject pageObj = testPage.getJSONObject(foundKey);
        assertEquals(pageObj.get("title"),"Test title user C");



    }
    @Test
    public void testSetJsonPathSuccessfull(){

        pageUser.setPagePath("test/path");
        assertEquals(pageUser.getPagePath(),"test/path");
    }

    @Test
    public void testGetJsonPathSuccessfull(){

        assertEquals(pageUser.getPagePath(),"./src/main/resources/static/pagetest.json");
    }
}