package com.shared_communication.backend;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InformationPageTest {


    InformationPage pageExample = new InformationPage("Title", null);
    User userA = new User("abc@example.com", "passA", "userA", Roles.StandardUser, "./src/main/resources/static/test.json");
    User admin = new User("admin@example.com", "admin", "admin", Roles.SystemAdmin, "./src/main/resources/static/test.json");
    User pageAuthor = new User("author@example.com", "author", "author", Roles.StandardUser, "./src/main/resources/static/test.json");
    User notAllowedUser = new User("nallowed@example.com", "na", "na", Roles.StandardUser, "./src/main/resources/static/test.json");
    InformationPage pageUser = new InformationPage("Title2", userA);


    InformationPageTest() throws JSONException, IOException {
    }

    @Test
    public void testUserRegistrationNull() {

        assertEquals(pageExample.getAuthor(), null);
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
    public void testGetAuthorNull() {

        assertEquals(pageExample.getAuthor(), null);

    }

    @Test
    public void testPageUsersZeroInitially() {

        assertEquals(pageExample.getPageUers().size(), 0);
    }

    @Test
    public void testAddingUserSuccessful() {

        assertEquals(pageExample.getPageUers().size(), 0);
        pageExample.addUser(userA);
        assertEquals(pageExample.getPageUers().size(), 1);

    }


    @Test
    public void testAddingUserErrorAlreadyAdded() {
        pageExample.addUser(userA);
        IllegalArgumentException thrown =
                assertThrowsExactly(IllegalArgumentException.class, () -> {
                    pageExample.addUser(userA);
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
        assertEquals(pageExample.getPosts().size(), 1);


    }

    @Test
    public void testAddingPostSuccessfulPageUser() {


        assertEquals(pageUser.getPosts().size(), 0);
        pageExample.addUser(userA);
        pageUser.addPost(userA, "Post by page user");
        assertEquals(pageExample.getPosts().size(), 1);

    }
}