package cs5031.shared_communication.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/** Test case for Information page class
 *
 */
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

    /**
     * Test to check if user detaisl are not null
     */
    @Test
    public void testUserRegistrationnotNull() {

        assertEquals(pageUser.getAuthor(), userA);
    }



    /**
     * Test to check if user detaisl are not null
     */
    @Test
    public void testPageWiterUsersZeroInitially() {

        assertEquals(pageExample.getPageWriteUsers().size(), 0);
    }

    /**
     * Test to check if initially users are zero or not
     */
    @Test
    public void testPageReaderUsersZeroInitially() {

        assertEquals(pageExample.getPageReadUsers().size(), 0);
    }


    /**
     * Test to check edge case of again tryng to add user
     */
    @Test
    public void testAddingWriteUserErrorAlreadyAdded() {
        pageExample.addWriteUser(userA);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addWriteUser(userA);
                });
    }

    /***
     * Test to check if again tryng to add user throws or not
     */
    @Test
    public void testAddingReadUserErrorAlreadyAdded() {
        pageExample.addReadUser(userA);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addReadUser(userA);
                });
    }

    /**
     * Test to check removing user from list
     */
    @Test
    public void testRemovingReadUserErrorNotInList() {

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.removeReadUser(userA);
                });
    }

    /**
     * Test to check removing user from edit list
     */
    @Test
    public void testRemovingWriteUserErrorNotInList() {

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.removeWriteUser(userA);
                });
    }

    /**
     * Test to check successful removal of user form edit list
     */
    @Test
    public void testRemovingWriteUserSuccessfully() {

        pageExample.addWriteUser(userA);
        pageExample.removeWriteUser(userA);
        assertEquals(pageExample.getPageWriteUsers().size(), 0);
    }
    /**
     * Test to check if user is not null
     */
    @Test
    public void testGetAuthorNotNull() {

        assertEquals(pageUser.getAuthor(), userA);

    }

    /**
     * Test to check removal of read user successfully
     */
    @Test
    public void testRemovingReadSuccessfully() {

        pageExample.addReadUser(userA);
        pageExample.removeReadUser(userA);
        assertEquals(pageExample.getPageReadUsers().size(), 0);
    }

    /**
     * Test to check intiail posts are zero
     */
    @Test
    public void getPostsSuccessfullyInitialZero() {

        assertEquals(pageExample.getPosts().size(), 0);
    }

    /***
     * Test to check adding same post
     */
    @Test
    public void testAddingPostError() {


        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addPost(userA, "This post will fail");
                    ;
                });
    }

    /**
     * Test to allow adding post when done by admin
     */

    @Test
    public void testAddingPostSuccessfulAdmin() {

        assertEquals(pageExample.getPosts().size(), 0);
        pageExample.addPost(admin, "Post by admin");
        assertEquals(pageExample.getPosts().size(), 1);


    }

    /**
     * Test to check adding post by author
     */
    @Test
    public void testAddingPostSuccessfulAuthor() {


        assertEquals(pageUser.getPosts().size(), 0);
        pageUser.addPost(userA, "Post by author");
        assertEquals(pageUser.getPosts().size(), 1);


    }

    /**
     * Test to check adding post by page user
     */
    @Test
    public void testAddingPostSuccessfulPageUser() {


        assertEquals(pageUser.getPosts().size(), 0);

        pageUser.addPost(userA, "Post by page user");
        assertEquals(pageUser.getPosts().size(), 1);

    }

    /**
     * Test to check duplicate post by user not allowed
     */
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

    /**
     * Test to not allow duplicate posts
     */
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

    /**
     * Test to write users successfully
     */
    @Test
    public void testPageWriteUsersSuccesfullAdded(){

        pageUser.addWriteUser(writerUser);
        assertEquals(pageUser.getPageWriteUsers().size(),1);
    }

    /**
     * Test to add read user succesfully
     */
    @Test
    public void testPageReadUsersSuccesfullAdded(){

        pageUser.addReadUser(readerUser);
        assertEquals(pageUser.getPageReadUsers().size(),1);
    }

    /**
     * Test to check add edit user
     */
    @Test
    public void testPageWriteUsersSuccesfulWrite(){

        assertEquals(pageUser.getPosts().size(),0);
        pageUser.addWriteUser(writerUser);
        pageUser.addPost(writerUser,"This will pass");
        assertEquals(pageUser.getPosts().size(),1);
    }

    /**
     * Test to check same post can't be added
     */
    @Test
    public void testPageWriteUsersFail(){

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageUser.addPost(writerUser,"This will fail");;
                    ;
                });


    }


    /**
     * Test to check read user can't write
     */
    @Test
    public void testPageReadUsersFailWhileWrite(){

        pageUser.addReadUser(readerUser);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageUser.addPost(userB, "Post by page user");
                    ;
                });


    }

    /**
     * Tess to change read to write user
     */
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

    /**
     * Test to write json succesfully
     * @throws JSONException
     * @throws IOException
     */

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

    /**
     * Test to check json path is valid
     */
    @Test
    public void testSetJsonPathSuccessfull(){

        pageUser.setPagePath("test/path");
        assertEquals(pageUser.getPagePath(),"test/path");
    }

    /**
     * Test to check json path macthes
     */
    @Test
    public void testGetJsonPathSuccessfull(){

        assertEquals(pageUser.getPagePath(),"./src/main/resources/static/pagetest.json");
    }
}