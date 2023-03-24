package com.shared_communication.backend;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InformationPageTest {



    User userA = new User("abc@example.com", "passA", "userA", Roles.StandardUser, "./src/main/resources/static/test.json");
    User userB = new User("abcB@example.com", "passB", "userB", Roles.StandardUser, "./src/main/resources/static/test.json");

    InformationPage pageExample = new InformationPage("Title", userB);
    User admin = new User("admin@example.com", "admin", "admin", Roles.SystemAdmin, "./src/main/resources/static/test.json");
    User pageAuthor = new User("author@example.com", "author", "author", Roles.StandardUser, "./src/main/resources/static/test.json");
    User notAllowedUser = new User("nallowed@example.com", "na", "na", Roles.StandardUser, "./src/main/resources/static/test.json");
    InformationPage pageUser = new InformationPage("Title2", userA);

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
        pageExample.addUser(userA);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addWriteUser(userA);
                });
    }

    @Test
    public void testAddingReadUserErrorAlreadyAdded() {
        pageExample.addUser(userA);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addReadUser(userA);
                });
    }

    @Test
    public void testRemovingUserErrorNotInList() {

        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.removeUser(userA);
                });
    }

    @Test
    public void testRemovingSuccessfully() {

        pageExample.addUser(userA);
        pageExample.removeUser(userA);
        assertEquals(pageExample.getPageUers().size(), 0);
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
    public void testTimeAddedToPost() {

        assertEquals(pageUser.getPosts().size(), 0);
        pageUser.addPost(userA, "Post by page user");
        assertEquals(pageUser.getPosts().get(userA).size(),2);

    }

    @Test
    public void testPageWriteUsersSuccesfullAdded(){

        pageUser.addWriteUser(writerUser);
        assertEquals(pageUser.getWriteUser(),1);
    }

    @Test
    public void testPageReadUsersSuccesfullAdded(){

        pageUser.addReadUser(readerUser);
        assertEquals(pageUser.getReadUser(),1);
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

        //Initially no where
        assertFalse(pageUser.getReadUser().contains(readerUser));
        pageUser.addReadUser(readerUser);
        //Gets added to rader list
        assertTrue(pageUser.getReadUser().contains(readerUser));
        // But not in writer list
        assertFalse(pageUser.getWriteUser().contains(readerUser));
        pageUser.changeReadToWriteUser(readerUser);
        // Gets added to both the lists
        assertTrue(pageUser.getReadUser().contains(readerUser));
        assertTrue(pageUser.getWriteUser().contains(readerUser));


    }
}