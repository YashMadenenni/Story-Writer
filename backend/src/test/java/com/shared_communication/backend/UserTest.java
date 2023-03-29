package com.shared_communication.backend;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    public User userA = new User("emailA@example.com", "passA", "usrA", Roles.StandardUser, "./src/main/resources/static/test.json");
    public User userB = new User("emailB@example.com", "passB", "usrB", Roles.StandardUser, "./src/main/resources/static/test.json");

    UserTest() throws JSONException, IOException {
    }


    /**
     * Test case to validate the password matching method is working by matching a password
     */
    @Test
    public void matchPassSuccessful() {

        String passA = "passA";
        assertTrue(userA.passMatch(passA));
    }

    @AfterEach
    public void resetCreds() throws IOException, JSONException {

        JSONObject credToAdd = new JSONObject();
        JSONObject userPass = new JSONObject();
        userPass.put("test", "test");
        credToAdd.put("test@example.com", userPass);
        FileWriter file = new FileWriter("./src/main/resources/static/test.json");
        file.write(credToAdd.toString());
        file.flush();
        file.close();
    }

    /**
     * Test case to validate the password matching method is working by not identifying the password
     */
    @Test
    public void matchPassFailed() {

        String passA = "passB";
        assertFalse(userA.passMatch(passA));
    }

    /**
     * Test case to validate the username matching method is working by matching a user name
     */
    @Test
    public void matchUserNameSuccessful() {

        String uName = "usrA";
        assertTrue(userA.userNameMatch(uName));
    }

    /**
     * Test case to validate the username matching method is working by not matching a user name
     */
    @Test
    public void matchUserNameFailed() {

        String uName = "passB";
        assertFalse(userA.userNameMatch(uName));
    }

    /**
     * Test case to validate the get email method is working by returning correct email
     */
    @Test
    public void getEmailSuccessful() {

        String email = "emailA@example.com";
        assertEquals(userA.getEmail(), email);
    }

    /**
     * Test case to validate the get email method is working by returning wrong email
     */
    @Test
    public void getEmailFailed() {

        String email = "emailB@example.com";
        assertNotEquals(userA.getEmail(), email);
    }

    /**
     * Test case to validate the username matching method is working by returning a user name
     */
    @Test
    public void getUsrNameSuccessful() {

        String uName = "usrA";
        assertEquals(userA.getUserName(), uName);
    }

    /**
     * Test case to validate the username matching method is working by returning wrong email
     */
    @Test
    public void getNameFailed() {

        String uName = "usrB";
        assertNotEquals(userA.getEmail(), uName);
    }

    /**
     * Test case to validate how to write credentials to a JSON file
     */
    @Test
    public void writeCredentialSuccessfullyEmail() throws JSONException, IOException {


        User userC = new User("userC@example.com", "passC", "usrC", Roles.StandardUser, "./src/main/resources/static/test.json");
        userC.addCredentialsToFile("./src/main/resources/static/test.json", userC.getEmail(), userC.getUserName(), "nullpass");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/test.json")));
        JSONObject testCreds = new JSONObject(jsonBody);
        assertTrue(testCreds.has("userC@example.com"));


    }

    @Test
    public void writeCredentialSuccessfullyUserName() throws JSONException, IOException {


        User userC = new User("userC@example.com", "passC", "usrC", Roles.StandardUser, "./src/main/resources/static/test.json");
        userC.addCredentialsToFile("./src/main/resources/static/test.json",userC.getEmail(),userC.getUserName(),"nullpass");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/test.json")));
        JSONObject testCreds = new JSONObject(jsonBody);
        assertTrue(testCreds.getJSONObject("userC@example.com").has("usrC"));


    }


    @Test
    public void testCredentialNotAdded() throws JSONException, IOException {

        User userC = new User("userC@example.com", "passC", "usrC", Roles.StandardUser, "./src/main/resources/static/test.json");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/test.json")));
        JSONObject testCreds = new JSONObject(jsonBody);
        assertFalse(testCreds.has("userD@example.com"));


    }

    @Test
    public void testCredentialMultipleAdd() throws JSONException, IOException {

        User userC = new User("userC@example.com", "passC", "usrC", Roles.StandardUser, "./src/main/resources/static/test.json");
        User userE = new User("userE@example.com", "passE", "usrE", Roles.StandardUser, "./src/main/resources/static/test.json");
        userC.addCredentialsToFile("./src/main/resources/static/test.json",userC.getEmail(),userC.getUserName(),"nullpass");
        userE.addCredentialsToFile("./src/main/resources/static/test.json",userE.getEmail(),userE.getUserName(),"nullpass");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/test.json")));
        JSONObject testCreds = new JSONObject(jsonBody);
        assertTrue(testCreds.has("userC@example.com"));
        assertTrue(testCreds.has("userE@example.com"));


    }


    @Test
    public void testCorrectEnumStdUser() {

        assertEquals(userA.getRole(), Roles.StandardUser);
    }

    @Test
    public void testCorrectEnumAdmin() throws JSONException, IOException {

        User admin = new User("admin@example.com", "admin", "admin", Roles.SystemAdmin, "./src/main/resources/static/test.json");
        assertEquals(admin.getRole(), Roles.SystemAdmin);
    }

    @Test
    public void testSettingRole() {

        userA.setRole(Roles.SystemAdmin);
        assertEquals(userA.getRole(), Roles.SystemAdmin);
    }


    @Test
    public void testUserEmailFetching() {


        assertEquals(userA.getEmail(), "emailA@example.com");
    }


    @Test
    public void testUserEqual() {


        assertTrue(userA.userEquals(userA));
    }

    @Test
    public void testUserNotEqual() {


        assertFalse(userA.userEquals(userB));
    }

    @Test
    public void testGetPages() {

        assertEquals(userA.getMyPages(), new ArrayList<InformationPage>());
    }

    @Test
    public void testMyPagesInitialZero() {

        assertEquals(userA.getMyPages().size(), 0);

    }

    @Test
    public void testGetMyPagesSuccessfulTwoPages() throws JSONException, IOException {

        InformationPage page1 = new InformationPage("title 1",userA,"./src/main/resources/static/pagetest.json");
        assertEquals(userA.getMyPages().size(),1);


    }

    @Test
    public void testMyPagesSuccessfulTwoPages() throws JSONException, IOException {

        InformationPage page1 = new InformationPage("title 1",userA,"./src/main/resources/static/pagetest.json");
        InformationPage page2 = new InformationPage("title 1",userA,"./src/main/resources/static/pagetest.json");
        assertEquals(userA.getMyPages().size(),2);


    }
}