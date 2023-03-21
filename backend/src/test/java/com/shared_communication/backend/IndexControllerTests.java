package com.shared_communication.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class IndexControllerTests { 

	@Autowired
	private MockMvc mockMvc;

	//Test1
	@Test 
	public void testUserRegisteration() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/user").content("{userEmail:\"test@gmail.com\", userName:\"Test\", password:\"test\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test2
	@Test 
	public void testAdminRegisteration() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/admin").content("{adminEmail:\"test@gmail.com\", userName:\"Test\", password:\"test\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test3
	@Test 
	public void testAdminlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin").content("{adminEmail:\"test@gmail.com\", password:\"test\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test4
	@Test
	public void testUserlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userLogin").content("{userEmail:\"test@gmail.com\", password:\"test\"}")).andExpect(MockMvcResultMatchers.status().isOk());
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
	//Test7
	@Test
	public void testDeleteUserSystemLevel () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userName=test@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	//p
	//Test8
	@Test
	public void testEditUserAccessAdd() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test9
	@Test
	public void testEditUserAccessRemove() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test10
	@Test
	public void testReadUserAccessAdd() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/access?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
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
		mockMvc.perform(MockMvcRequestBuilders.put("/put").content("{userName:\"new Email\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//p
	//Test13
	public void testDeletePage() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//Test14
	public void testUsersList() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//Test15
	public void testGetUser() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user?userName=test@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test14
	//Test3
	@Test
	public void testAdminInvalidlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/adminLogin").content("{adminEmail:\"test@gmail.com\", password:\"tet\"}")).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	//Test15
	//Test5
	@Test
	public void testMultiplePagewithSameName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isOk()).andDo(result -> mockMvc.perform(MockMvcRequestBuilders.post("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isUnauthorized()));
	}

	//Test16
	//Test4
	@Test
	public void testUserInvalidlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userLogin").content("{userEmail:\"test@gmail.com\", password:\"test\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//p
	//Test17
	//Test13
	@Test
	public void testDeletePageInvalidName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete("/page?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	//Test18
	//Test15
	@Test
	public void testGetUserInvalidName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user?userName=Test@gmail.com")).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	//p
	//Test19
	//Test7
	@Test
	public void testDeleteUserSystemLevelInvalidName () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/user?userName=tes@gmail.com")).andExpect(MockMvcResultMatchers.status().isNotFound());
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
