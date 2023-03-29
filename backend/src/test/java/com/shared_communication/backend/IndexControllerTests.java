package com.shared_communication.backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
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
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTests {

	@AfterEach
	public void resetCreds() throws IOException, JSONException {


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
		pageToAddb.put("readAccessUser",new JSONArray(List.of("abc@example.com")));
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

		JSONObject pageToAddc = new JSONObject();
		pageToAddc.put("editAccessUser",new JSONArray(List.of("abc@example.com")));
		pageToAddc.put("readAccessUser",new JSONArray(List.of("abc@example.com")));
		pageToAddc.put("admin","abc@example.com");
		pageToAddc.put("title","TitleAdd");
		pageToAddc.put("content","");
		top.put("page0",pageToAddc);

		FileWriter pagefile = new FileWriter("./src/main/resources/static/pagetestmodel.json");
		pagefile.write(top.toString());
		pagefile.flush();
		pagefile.close();


	}


	@Autowired
	private MockMvc mockMvc;

    Model model = new Model("src/main/resources/static/users.json", "src/main/resources/static/admin.json");
	

	//Test1
	@Test 
	public void testUserRegisteration() throws Exception{
				mockMvc.perform(MockMvcRequestBuilders.post("/user").content("{\"userEmail\":\"testIndex@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndex\"}")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	

	//Test4
	@Test
	public void testUserlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userLogin").content("{\"userEmail\":\"testIndex@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndex\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}
		//p
	//Test7
	@Test
	public void testDeleteUserSystemLevel () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userEmail=test@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test2
	@Test 
	public void testAdminRegisteration() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/admin").content("{\"userEmail\":\"testIndexAdmin@gmail.com\", \"userName\":\"Test\", \"password\":\"testIndexAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test3
	@Test 
	public void testAdminlogin() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/admin").content("{\"userEmail\":\"testLoginAdmin@gmail.com\", \"userName\":\"Test\", \"password\":\"testLoginAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
		mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin").content("{\"userEmail\":\"testLoginAdmin@gmail.com\", \"userName\":\"Test\", \"password\":\"testLoginAdmin\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testDeleteUserAdminSystemLevel () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userEmail=admin@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test5
	@Test
	public void testCreatePage() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test6
	@Test
	public void testWriteToFile() throws Exception{

		String jsonBody = "{user:\"test@gmail.com\" ,page:\"Communication\" content : \"Writing to file from test case\"}";
		mockMvc.perform(MockMvcRequestBuilders.put("/page").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}


	
	//p
	//Test8
	@Test
	public void testEditUserAccessAdd() throws Exception {
		String jsonBody = "{\"userEmail\":\"user1@gmail.com\", \"pageName\":\"TestAdd\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/page/access").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test9
	@Test
	public void testEditUserAccessRemove() throws Exception {
		String jsonBody ="{\"userEmail\":\"user1@gmail.com\", \"pageName\":\"TestAdd\"}";
		mockMvc.perform(MockMvcRequestBuilders.delete("/page/access").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test10
	@Test
	public void testReadUserAccessAdd() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	//p
	//Test11
	@Test
	public void testUserAccessRemovePageLevel() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//p
	//Test12
	public void testEditProfile() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/user/update").content("{\"userEmail\":\"test@gmail.com\", \"currentRole\":\"user\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//p
	//Test13
	public void testDeletePage() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isOk());
	}
//Test15
	//Test5
	@Test
	public void testMultiplePagewithSameName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isOk()).andDo(result -> mockMvc.perform(MockMvcRequestBuilders.post("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isUnauthorized()));
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

	//p
	//Test17
	//Test13
	@Test
	public void testDeletePageInvalidName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	public void testgetPagevalidName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/page?userEmail=test34@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}



	//Test18
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
} 
