package cs5031.shared_communication.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for user class and methods
 */
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

    /**
     * Test to validate the credentials are successfully writeen to a file
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void writeCredentialSuccessfullyUserName() throws JSONException, IOException {


        User userC = new User("userC@example.com", "passC", "usrC", Roles.StandardUser, "./src/main/resources/static/test.json");
        userC.addCredentialsToFile("./src/main/resources/static/test.json",userC.getEmail(),userC.getUserName(),"nullpass");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/test.json")));
        JSONObject testCreds = new JSONObject(jsonBody);
        assertTrue(testCreds.getJSONObject("userC@example.com").has("usrC"));


    }

    /**
     * Test to check when credentials are not added
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testCredentialNotAdded() throws JSONException, IOException {

        User userC = new User("userC@example.com", "passC", "usrC", Roles.StandardUser, "./src/main/resources/static/test.json");
        String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/test.json")));
        JSONObject testCreds = new JSONObject(jsonBody);
        assertFalse(testCreds.has("userD@example.com"));


    }

    /**
     * Test to ensure multiple credentials can be added tot he file
     * @throws JSONException
     * @throws IOException
     */
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

    /**
     * Test to check corrent Role enum returned
     */
    @Test
    public void testCorrectEnumStdUser() {

        assertEquals(userA.getRole(), Roles.StandardUser);
    }

    /***
     * Test to check correct enum returned
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testCorrectEnumAdmin() throws JSONException, IOException {

        User admin = new User("admin@example.com", "admin", "admin", Roles.SystemAdmin, "./src/main/resources/static/test.json");
        assertEquals(admin.getRole(), Roles.SystemAdmin);
    }

    /**
     * Test for setting user role
     */
    @Test
    public void testSettingRole() {

        userA.setRole(Roles.SystemAdmin);
        assertEquals(userA.getRole(), Roles.SystemAdmin);
    }

    /**
     * Test for getting user email
     */
    @Test
    public void testUserEmailFetching() {


        assertEquals(userA.getEmail(), "emailA@example.com");
    }

    /**
     * Test for checking if users are equals
     */
    @Test
    public void testUserEqual() {


        assertTrue(userA.userEquals(userA));
    }

    /**
     * Test to ensure different users are not equals
     */
    @Test
    public void testUserNotEqual() {


        assertFalse(userA.userEquals(userB));
    }

    /**
     * Test to get oages for a users
     */
    @Test
    public void testGetPages() {

        assertEquals(userA.getMyPages(), new ArrayList<InformationPage>());
    }

    /**
     * Test to ensure no pages initially
     */
    @Test
    public void testMyPagesInitialZero() {

        assertEquals(userA.getMyPages().size(), 0);

    }

    /**
     * Test to get pages for user successfully
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testGetMyPagesSuccessfulTwoPages() throws JSONException, IOException {

        InformationPage page1 = new InformationPage("title 1",userA,"./src/main/resources/static/pagetest.json");
        assertEquals(userA.getMyPages().size(),1);


    }

    /**
     * Test to get multiple pages successfully.
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testMyPagesSuccessfulTwoPages() throws JSONException, IOException {

        InformationPage page1 = new InformationPage("title 1",userA,"./src/main/resources/static/pagetest.json");
        InformationPage page2 = new InformationPage("title 1",userA,"./src/main/resources/static/pagetest.json");
        assertEquals(userA.getMyPages().size(),2);


    }
}