package com.shared_communication.backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTests {

	JSONObject pageJson = null;
	JSONObject adminMessage=null;

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


	//Test1
	@Test 
	public void testUserRegisteration() throws Exception{
				mockMvc.perform(MockMvcRequestBuilders.post("/user").content("{\"userEmail\":\"testIndexregister@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndex\"}")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	

	//Test4
	@Test
	public void testUserlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userLogin").content("{\"userEmail\":\"usernew@gmail.com\", \"userName\":\"Test\", \"password\":\"userTestnew\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}
		//p
	//Test7
	@Test
	public void testDeleteUserSystemLevel () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userEmail=user3@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test2
	@Test 
	public void testAdminRegisteration() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/admin").content("{\"userEmail\":\"testIndexAdmin@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndexAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test3
	@Test 
	public void testAdminlogin() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/admin").content("{\"userEmail\":\"testLoginAdmin1@gmail.com\", \"userName\":\"Test\", \"password\":\"testLoginAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
		mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin").content("{\"userEmail\":\"testLoginAdmin1@gmail.com\", \"userName\":\"Test\", \"password\":\"testLoginAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testDeleteUserAdminSystemLevel () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userEmail=user@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test5
	@Test
	public void testCreatePage() throws Exception{

		String jsonBody = "{\"userEmail\":\"test@gmail.com\", \"pageName\":\"CheckPage\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test8
	@Test
	public void testEditUserAccessAdd() throws Exception {
		String jsonBody = "{\"userEmail\":\"user1@gmail.com\", \"pageName\":\"Title2\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/access").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test8
	@Test
	public void testReadUserAccessAddSuccessful() throws Exception {
		String jsonBody = "{\"userEmail\":\"user1@gmail.com\", \"pageName\":\"TestAdd\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/access/user").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test9
	@Test
	public void testEditUserAccessRemove() throws Exception {
		String jsonBody ="{\"userEmail\":\"xyz@gmail.com\", \"pageName\":\"Testnow\"}";
		mockMvc.perform(MockMvcRequestBuilders.delete("/page/access").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testReadUserAccessRemove() throws Exception {
		String jsonBody ="{\"userEmail\":\"xyz@gmail.com\", \"pageName\":\"TitleAdd\"}";
		mockMvc.perform(MockMvcRequestBuilders.delete("/page/access/user").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test10
	@Test
	public void testReadUserAccessAdd() throws Exception {
		String jsonBody ="{\"userEmail\":\"user@gmail.com\", \"pageName\":\"Title2\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/access/user").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	//p
	//Test11
	@Test
	public void testUserAccessRemovePageLevel() throws Exception {
		String jsonBody ="{\"userEmail\":\"xyz@gmail.com\", \"pageName\":\"Testnow\"}";
		mockMvc.perform(MockMvcRequestBuilders.delete("/page/access").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//p
	//Test12
	public void testEditProfile() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/user/update").content("{\"userEmail\":\"user@gmail.com\", \"currentRole\":\"user\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}


	@Test
	//p
	//Test12
	public void testEditProfileAdmin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/user/update").content("{\"userEmail\":\"admin1@gmail.com\", \"currentRole\":\"admin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}
//Test15
	//Test5
	@Test
	public void testMultiplePagewithSameName() throws Exception{
		String jsonBody = "{\"userEmail\":\"user1@gmail.com\", \"pageName\":\"CheckNow\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(result -> mockMvc.perform(MockMvcRequestBuilders.post("/page").content(jsonBody)).andExpect(MockMvcResultMatchers.status().is4xxClientError()));
	}
	@Test
	//Test14
	public void testUsersList() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//Test15
	public void testGetUser() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user?userEmail=user@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test14
	//Test3
	@Test
	public void testAdminInvalidlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin").content("{\"userEmail\":\"testIndexAdminInvalid@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndexAdminInvalid\"}")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	

	//Test16
	//Test4
	@Test
	public void testUserInvalidlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userLogin").content("{\"userEmail\":\"test35@gmail.com\", \"userName\":\"Test35\", \"password\":\"test35\"}")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}


	@Test
	public void testgetPagevalidNameFailed() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/page?userEmail=test34@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	public void testgetPagevalidNameSuccessful() throws Exception{

		mockMvc.perform(MockMvcRequestBuilders.get("/page?userEmail=abc@example.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}


	//Test15
	@Test
	public void testGetUserInvalidName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user?userName=Test@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	//p
	//Test19
	//Test7
	@Test
	public void testDeleteUserSystemLevelInvalidName () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userName=tes@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	//p
	//Test20
	//Test9
	@Test
	public void testEditUserAccessRemoveUserNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	//Test21
	//Test11
	@Test
	public void testUserAccessRemovePageLevelInvalidUserName() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	public void testGetAllMyReadPages() throws Exception {

		String jsonBody = "{\"userEmail\":\"user@gmail.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.get("/page/access/my").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetAllMyWritePages() throws Exception {

		String jsonBody = "{\"userEmail\":\"user@gmail.com\"}";
		mockMvc.perform(MockMvcRequestBuilders.get("/page/access/mywrite").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testShareAdminMessage() throws Exception {

		String jsonBody = "{\"userEmail\":\"admin@gmail.com\",\"message\":\"Message from admin\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/admin/message").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetAdminMessage() throws Exception {


		mockMvc.perform(MockMvcRequestBuilders.get("/page/admin/message")).andExpect(MockMvcResultMatchers.status().isOk());
	}
} 
