package com.shared_communication.backend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.NoSuchFileException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.minidev.json.JSONObject;

public class ModelTests {
    private Model testModel;

    @BeforeEach
    public void setup(){
        testModel = new Model("src/test/resources/pages.json");

    }

    @Test
    public void shouldNotOpenJson(){
        NoSuchFileException thrown = assertThrowsExactly(NoSuchFileException.class, ()->{testModel.loadFile("wrongpath.json");});
    }

    @Test
    public void shouldOpenJson(){
        JSONObject result = null;
        try {
            result = testModel.loadFile("src/test/resources/test.json");
        } catch (Exception e) {
            // TODO: handle exception
        }
        assertNotNull(result);
    }

    @Test
    public void testAdminLoginSuccess(){
        assertTrue(testModel.checkAdminLogin("admin1@gmail.com","Admin1","adminpassword"));
    }

    @Test
    public void testAdminLoginFail(){
        assertFalse(testModel.checkAdminLogin("admin2@gmail.com","Admin2","adminwrong"));
    }

    @Test
    public void testUserLoginSuccess(){
        assertTrue(testModel.checkAdminLogin("user@gmail.com","User","userpassword"));
    }

    @Test
    public void testUserLoginFail(){
        assertFalse(testModel.checkAdminLogin("User2@gmail.com","User2","userwrong"));
    }

    @Test
    public void removeUserFromSystem(){
        assertTrue(testModel.removeUserFromSystem("user3@gmail.com"));
    }

    @Test
    public void removeUserFromSystemFail(){
        assertFalse(testModel.removeUserFromSystem("user5@gmail.com"));
    }
}
