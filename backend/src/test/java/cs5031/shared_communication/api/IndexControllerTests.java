package cs5031.shared_communication.api;

import cs5031.shared_communication.model.Model;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/***
 * Test class to check API calls
 */
@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTests {

	JSONObject pageJson = null;
	JSONObject adminMessage=null;

	/**
	 * Method to set environment after each test
	 * @throws IOException
	 */
	@AfterEach
	public void setjson() throws IOException {

		FileWriter pagefile = new FileWriter("./src/main/resources/static/pages.json");
		pagefile.write(pageJson.toString());
		pagefile.flush();
		pagefile.close();

		FileWriter messagefile = new FileWriter("./src/main/resources/static/usermessage.json");
		messagefile.write(adminMessage.toString());
		messagefile.flush();
		messagefile.close();

	}

	/**
	 * Method to set environment before each test
	 * @throws IOException
	 * @throws JSONException
	 */
	@BeforeEach
	public void getjson() throws IOException, JSONException {

		String jsonBody = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/pages.json")));
		pageJson = new JSONObject(jsonBody);

		String jsonBodyAdminMessage = new String(Files.readAllBytes(Paths.get("./src/main/resources/static/usermessage.json")));
		adminMessage = new JSONObject(jsonBodyAdminMessage);



	}


	@Autowired
	private MockMvc mockMvc;

    Model model = new Model("src/main/resources/static/users.json", "src/main/resources/static/admin.json");


	/**
	 * Test to check successful user registration
	 * @throws Exception
	 */
	@Test 
	public void testUserRegisteration() throws Exception{
				mockMvc.perform(MockMvcRequestBuilders.post("/user").content("{\"userEmail\":\"testIndexregister@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndex\"}")).andExpect(MockMvcResultMatchers.status().isOk());

	}


	/**
	 * Test to check successful user login
	 * @throws Exception
	 */
	@Test
	public void testUserlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userLogin").content("{\"userEmail\":\"testIndexregister@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndex\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case for deleting a user from system
	 * @throws Exception
	 */
	@Test
	public void testDeleteUserSystemLevel () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userEmail=user3@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case for registering an admin
	 * @throws Exception
	 */
	@Test 
	public void testAdminRegisteration() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/admin").content("{\"userEmail\":\"testIndexAdmin@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndexAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test for checking if admin is able to login
	 * @throws Exception
	 */
	//Test3
	@Test 
	public void testAdminlogin() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/admin").content("{\"userEmail\":\"testLoginAdmin1@gmail.com\", \"userName\":\"Test\", \"password\":\"testLoginAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
		mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin").content("{\"userEmail\":\"testLoginAdmin1@gmail.com\", \"userName\":\"Test\", \"password\":\"testLoginAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case for admin deleting a user from the system
	 * @throws Exception
	 */
	@Test
	public void testDeleteUserAdminSystemLevel () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userEmail=user@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case check page creation API
	 * @throws Exception
	 */
	@Test
	public void testCreatePage() throws Exception{

		String jsonBody = "{\"userEmail\":\"test@gmail.com\", \"pageName\":\"CheckPage\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case check page edit user API
	 * @throws Exception
	 */
	@Test
	public void testEditUserAccessAdd() throws Exception {
		String jsonBody = "{\"userEmail\":\"user1@gmail.com\", \"pageName\":\"Title2\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/access").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case check user read access granted API
	 * @throws Exception
	 */
	@Test
	public void testReadUserAccessAddSuccessful() throws Exception {
		String jsonBody = "{\"userEmail\":\"user1@gmail.com\", \"pageName\":\"TestAdd\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/access/user").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to check edit request granted API
	 * @throws Exception
	 */
	@Test
	public void testEditUserAccessRemove() throws Exception {
		String jsonBody ="{\"userEmail\":\"xyz@gmail.com\", \"pageName\":\"Testnow\"}";
		mockMvc.perform(MockMvcRequestBuilders.delete("/page/access").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to check read access revoked API
	 * @throws Exception
	 */
	@Test
	public void testReadUserAccessRemove() throws Exception {
		String jsonBody ="{\"userEmail\":\"xyz@gmail.com\", \"pageName\":\"TitleAdd\"}";
		mockMvc.perform(MockMvcRequestBuilders.delete("/page/access/user").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to check read request granted API
	 * @throws Exception
	 */
	@Test
	public void testReadUserAccessAdd() throws Exception {
		String jsonBody ="{\"userEmail\":\"user@gmail.com\", \"pageName\":\"Title2\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/access/user").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to check user to be removed from page completely
	 * @throws Exception
	 */
	@Test
	public void testUserAccessRemovePageLevel() throws Exception {
		String jsonBody ="{\"userEmail\":\"xyz@gmail.com\", \"pageName\":\"Testnow\"}";
		mockMvc.perform(MockMvcRequestBuilders.delete("/page/access").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to check if profile of the user can be updated or not
	 * @throws Exception
	 */
	@Test
	public void testEditProfile() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/user/update").content("{\"userEmail\":\"user@gmail.com\", \"currentRole\":\"user\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to update profile of admin
	 * @throws Exception
	 */
	@Test
	public void testEditProfileAdmin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/user/update").content("{\"userEmail\":\"admin1@gmail.com\", \"currentRole\":\"admin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test to check mutltiple page not allowed API
	 * @throws Exception
	 */
	@Test
	public void testMultiplePagewithSameName() throws Exception{
		String jsonBody = "{\"userEmail\":\"user1@gmail.com\", \"pageName\":\"CheckNow\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(result -> mockMvc.perform(MockMvcRequestBuilders.post("/page").content(jsonBody)).andExpect(MockMvcResultMatchers.status().is4xxClientError()));
	}

	/**
	 * Test case to get list of all users
	 * @throws Exception
	 */
	@Test
	//Test14
	public void testUsersList() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to get a particular user
	 * @throws Exception
	 */
	@Test
	//Test15
	public void testGetUser() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user?userEmail=user@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to invalidate wrong login details for admin
	 * @throws Exception
	 */
	@Test
	public void testAdminInvalidlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin").content("{\"userEmail\":\"testIndexAdminInvalid@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndexAdminInvalid\"}")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	/**
	 * Test case to invalidate wrong user login details
	 * @throws Exception
	 */
	@Test
	public void testUserInvalidlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userLogin").content("{\"userEmail\":\"test35@gmail.com\", \"userName\":\"Test35\", \"password\":\"test35\"}")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Test case to validate page details can be retrieved
	 * @throws Exception
	 */
	@Test
	public void testgetPagevalidNameFailed() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/page?userEmail=test34@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Test case to user pages
	 * @throws Exception
	 */
	@Test
	public void testgetPagevalidNameSuccessful() throws Exception{

		mockMvc.perform(MockMvcRequestBuilders.get("/page?userEmail=abc@example.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}


	/**
	 * Test case ensure API works when invalid name is provided
	 * @throws Exception
	 */
	@Test
	public void testGetUserInvalidName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user?userName=Test@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Test case to reject invalid request of deleting user who doesn't exists
	 * @throws Exception
	 */
	@Test
	public void testDeleteUserSystemLevelInvalidName () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userName=tes@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Test case to check API behaviour in case of invalid user is being tried to removed from a page
	 * @throws Exception
	 */
	@Test
	public void testEditUserAccessRemoveUserNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Test case to check API behaviour in case of invalid user is being tried to removed from a page
	 * @throws Exception
	 */
	@Test
	public void testUserAccessRemovePageLevelInvalidUserName() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Test case to check get all self read pages API
	 * @throws Exception
	 */
	@Test
	public void testGetAllMyReadPages() throws Exception {

		String jsonBody = "{\"userEmail\":\"user@gmail.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.get("/page/access/my").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to check get all self edit pages API
	 * @throws Exception
	 */
	@Test
	public void testGetAllMyWritePages() throws Exception {

		String jsonBody = "{\"userEmail\":\"user@gmail.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.get("/page/access/mywrite").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to check admin message sharing API
	 * @throws Exception
	 */
	@Test
	public void testShareAdminMessage() throws Exception {

		String jsonBody = "{\"userEmail\":\"admin@gmail.com\",\"message\":\"Message from admin\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/admin/message").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test case to get admin message API
	 * @throws Exception
	 */
	@Test
	public void testGetAdminMessage() throws Exception {


		mockMvc.perform(MockMvcRequestBuilders.get("/page/admin/message")).andExpect(MockMvcResultMatchers.status().isOk());
	}
} 
