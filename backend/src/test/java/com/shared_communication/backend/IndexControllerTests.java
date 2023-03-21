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
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userRegisteration").content("{userEmail:\"test@gmail.com\", userName:\"Test\", password:\"test\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test2
	@Test 
	public void testAdminRegisteration() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/adminRegisteration").content("{adminEmail:\"test@gmail.com\", userName:\"Test\", password:\"test\"}")).andExpect(MockMvcResultMatchers.status().isOk());
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

	//Test5
	@Test
	public void testCreatePage() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/createPage?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test6
	@Test
	public void testWriteToFile() throws Exception{

		String jsonBody = "{user:\"test@gmail.com\" , content : \"Writing to file from test case\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/updateFile").content(jsonBody)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test7
	@Test
	public void testDeleteUserSystemLevel () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteUser?userName=test@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	//Test8
	@Test
	public void testEditUserAccessAdd() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/updatePageEditAccess/add?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test9
	@Test
	public void testEditUserAccessRemove() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/updatePageEditAccess/remove?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test10
	@Test
	public void testReadUserAccessAdd() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/updatePageReadAccess/add?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	//Test11
	@Test
	public void testUserAccessRemovePageLevel() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/removeAccess/remove?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//Test12
	public void testEditProfile() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/updateProfile?userName=test@gmail.com").content("{userName:\"new Email\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//Test13
	public void testDeletePage() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete("/deletePage?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//Test14
	public void testUsersList() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/getUsersList")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	//Test15
	public void testGetUser() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/getUser?userName=test@gmail.com")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Worng

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
		mockMvc.perform(MockMvcRequestBuilders.get("/createPage?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isOk()).andDo(result -> mockMvc.perform(MockMvcRequestBuilders.get("/createPage?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isUnauthorized()));
	}

	//Test16
	//Test4
	@Test
	public void testUserInvalidlogin() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.post("/userLogin").content("{userEmail:\"test@gmail.com\", password:\"test\"}")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	//Test17
	//Test13
	@Test
	public void testDeletePageInvalidName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete("/deletePage?pageName=Communication")).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	//Test18
	//Test15
	@Test
	public void testGetUserInvalidName() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/getUser?userName=Test@gmail.com")).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	//Test19
	//Test7
	@Test
	public void testDeleteUserSystemLevelInvalidName () throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/deleteUser?userName=tes@gmail.com")).andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	//Test20
	//Test9
	@Test
	public void testEditUserAccessRemoveUserNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/updatePageEditAccess/remove?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	//Test21
	//Test11
	@Test
	public void testUserAccessRemovePageLevelInvalidUserName() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/removeAccess/remove?pageName=Communication&userName=test2@gmail.com")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
} 
