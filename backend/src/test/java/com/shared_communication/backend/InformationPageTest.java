package com.shared_communication.backend;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InformationPageTest {


    InformationPage pageExample = new InformationPage("Title",null);
    User userA = new User("abc@example.com","passA","userA",Roles.StandardUser,"./src/main/resources/static/test.json");
    User admin = new User("admin@example.com","admin","admin",Roles.SystemAdmin,"./src/main/resources/static/test.json");
    User pageAuthor = new User("author@example.com","author","author",Roles.StandardUser,"./src/main/resources/static/test.json");
    User notAllowedUser = new User("nallowed@example.com","na","na",Roles.StandardUser,"./src/main/resources/static/test.json");
    InformationPage pageUser = new InformationPage("Title2",userA);


    InformationPageTest() throws JSONException, IOException {
    }

    @Test
    public void testUserRegistrationNull(){

        assertEquals(pageExample.getAuthor(),null);
    }

    @Test
    public void testUserRegistrationnotNull(){

        assertEquals(pageUser.getAuthor(),userA);
    }

    @Test
    public void testGetAuthorNotNull(){

        assertEquals(pageUser.getAuthor(),userA);

    }

    @Test
    public void testGetAuthorNull(){

        assertEquals(pageExample.getAuthor(),null);

    }

    @Test
    public void testPageUsersZeroInitially(){

        assertEquals(pageExample.getPageUers().size(),0);
    }

    @Test
    public void testAddingUserSuccessful(){

        assertEquals(pageExample.getPageUers().size(),0);
        pageExample.addUser(userA);
        assertEquals(pageExample.getPageUers().size(),1);

    }