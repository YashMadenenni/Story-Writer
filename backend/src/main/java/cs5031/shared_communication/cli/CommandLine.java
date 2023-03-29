package cs5031.shared_communication.cli;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import cs5031.shared_communication.model.Model;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class CommandLine {
    static Model model;
    

    public static void main(String args[]) {

        Login();

    }

    public static void Login() {
        System.out.println("Enter 1 to login and 2 to register");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        sc.nextLine();
        if (option == 1) {
            loginUser();
        } else if (option == 2) {
            registerUser();
        } else {
            System.out.println("Enter 1 for User and 2 for Admin");
        }
    }

    private static void registerUser() {
        String userEmail;
        String password;
        String userName;
        RestTemplate restTemplate = new RestTemplate();

        HashMap<String, Object> requestBody = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter user email");
        userEmail = sc.nextLine();
        System.out.println("Enter " + userEmail + " password ");
        password = sc.nextLine();
        System.out.println("Enter " + userEmail + " Name ");
        userName = sc.nextLine();
        requestBody.put("userEmail", userEmail);
        requestBody.put("password", password);
        requestBody.put("userName", userName);

        System.out.println("Enter 1 if User and 2 if admin");
        int option = sc.nextInt();
        if (option == 1) {
            try {

                HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                String json = restTemplate.postForObject("http://localhost:8080/user", request, String.class);
                
                if (json.equals("Success")) {
                    mainPage(userEmail);
                } else {
                    System.out.println("Failed to Register");
                }
            } catch (Exception e) {
                System.out.println("Fail" + e.getMessage());
            }

        } else if (option == 2) {
            try {
                HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                String json = restTemplate.postForObject("http://localhost:8080/admin", request, String.class);
                if (json.equals("Success")) {
                    mainPage(userEmail);
                }
            } catch (Exception e) {
                System.out.println("Fail" + e.getMessage());
            }
        } else {
            System.out.println("enter correct option");
        }
    }

    public static void loginUser() {
        String userEmail;
        String password;
        String userName;
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, Object> requestBody = new HashMap<>();

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter user email");
        userEmail = sc.nextLine();
        System.out.println("Enter " + userEmail + " password ");
        password = sc.nextLine();
        requestBody.put("userEmail", userEmail);
        requestBody.put("password", password);
        System.out.println("Enter 1 if User and 2 if admin");
        int option = sc.nextInt();
        if (option == 1) {
            try {
                HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                String json = restTemplate.postForObject("http://localhost:8080/userLogin", request, String.class);
                if (json.equals("Success")) {
                    mainPage(userEmail);
                }
            } catch (Exception e) {
                System.out.println("Fail" + e.getMessage());
            }

        } else if (option == 2) {
            try {
                HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                String json = restTemplate.postForObject("http://localhost:8080/adminLogin", request, String.class);
                if (json.equals("Success")) {

                    System.out.println("Enter 1 for pages and 2 for users");
                    int option2 = sc.nextInt();
                    if (option2 == 1) {
                        mainPage(userEmail);
                    } else if (option2 == 2) {
                        getSystemUsers();
                    }

                }
            } catch (Exception e) {
                System.out.println("Fail" + e.getMessage());
            }
        } else {
            System.out.println("enter correct option");
        }
    }

    public static void mainPage(String userEmail) {
        System.out.println("");
        System.out.println("");
        System.out.println("Pages");

        RestTemplate restTemplate = new RestTemplate();
        // HashMap<String, Object> requestBody = new HashMap<>();
        Scanner sc = new Scanner(System.in);
        HashMap<String, String> allPages = new HashMap<>();
        HashMap<String, String> allEditUsers = new HashMap<>();
        HashMap<String, String> allReadAccessUser = new HashMap<>();
        String pageTitle = "";
        String pageContent = "";
        String editAccessUser;
        String readAccessUser;
        String newContent = "";
        Console console = System.console();
        try {
            // HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
            String json = restTemplate.getForObject("http://localhost:8080/page?userEmail=" + userEmail, String.class);
            System.out.println(json);

            JSONArray pages = new JSONArray(json);
            for (int i = 0; i < pages.length(); i++) {
                JSONObject adminObject = pages.getJSONObject(i);
                pageTitle = adminObject.getString("title");
                pageContent = adminObject.getString("content");

                editAccessUser = adminObject.getString("editAccessUser");
                readAccessUser = adminObject.getString("readAccessUser");
                allPages.put(pageTitle, pageContent);
                allEditUsers.put(pageTitle, editAccessUser);
                allReadAccessUser.put(pageTitle, readAccessUser);
                System.out.println("Title : " + pageTitle);

            }

            System.out.println("Enter page title");
            String pageToviewUser = sc.nextLine();
            if (allPages.containsKey(pageToviewUser)) {
                System.out.println("Enter 1 to view page 2 to create new page");
                int option = sc.nextInt();
                if (option == 1) {
                    System.out.println("");
                    System.out.println("");
                    System.out.println("Title : " + pageToviewUser);
                    System.out.println("Content :" + allPages.get(pageToviewUser));
                    newContent = pageContent;
                    System.out.println("");
                    System.out.println("Enter 1 to edit content "); 
                    System.out.println("Enter 2 to see users that have access ");
                    System.out.println("Enter 3 to give access to a user");
                    
                    int option2 = sc.nextInt();
                    if (option2 == 1) {

                        System.out.println("Editing..");

                        String addedContent = console.readLine(pageContent + " ");
                        newContent = newContent + addedContent;
                        System.out.println("");
                        System.out.println("New Content");

                        System.out.println(newContent);
                        postToPage(pageToviewUser, newContent, userEmail);

                    } else if (option2 == 2) {
                        System.out.println("Enter 1 to view users that have access to page");
                        System.out.println("Enter 2 to view users that have edit access to page");
                        int option3 = sc.nextInt();
                        if (option3 == 1) {
                            System.out.println("Users that have access to page");
                            JSONArray usersJson = new JSONArray(allReadAccessUser.get(pageToviewUser));
                            System.out.println(usersJson);

                        } else if (option3 == 2) {
                            System.out.println("Users that have edit access");
                            JSONArray usersJson = new JSONArray(allEditUsers.get(pageToviewUser));
                            System.out.println(usersJson);
                        }

                    }else if(option2 == 3){
                       
                        System.out.println("Enter user to be added");
                        String userAdd = console.readLine();
                        
                            HashMap<String, Object> requestBody = new HashMap<>();
                            requestBody.put("pageName",pageToviewUser);
                            requestBody.put("userEmail",userAdd);
                            HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                            String jsonPayload = restTemplate.postForObject("http://localhost:8080/page/access", request, String.class);
                        System.out.println(jsonPayload);

                    
                    }
                } else if (option == 2) {
                    System.out.println("");
                    System.out.println("");
                    System.out.println("Enter New page name");
                    String newPageName = console.readLine();
                    HashMap<String, Object> requestBody = new HashMap<>();
                    requestBody.put("userEmail", userEmail);
                    requestBody.put("pageName", newPageName);
                    if (!allPages.containsKey(newPageName) && newPageName != "") {
                        try {
                            HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                            String jsonResponse = restTemplate.postForObject("http://localhost:8080/page", request,
                                    String.class);

                            System.out.println(jsonResponse);

                        } catch (Exception e) {
                            System.out.println("Fail" + e.getMessage());
                        }
                    } else {
                        System.out.println("Duplicate page");
                    }
                }
            } else {
                System.out.println("Enter correct pagetitle");
            }

        } catch (Exception e) {
            System.out.println("Fail" + e.getMessage());
        }

    }

    private static void postToPage(String pageToviewUser, String newContent, String userEmail) {
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("userEmail", userEmail);
        requestBody.put("content", newContent);
        requestBody.put("pageName", pageToviewUser);
        try {
            HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
            String json = restTemplate.postForObject("http://localhost:8080/page/content", request, String.class);
            if (json.equals("Success")) {
                System.out.println("Success");
            }
        } catch (Exception e) {
            System.out.println("Fail" + e.getMessage());
        }

    }

    public static void getSystemUsers() {
        System.out.println("");
        System.out.println("");
        System.out.println("Users");
        RestTemplate restTemplate = new RestTemplate();
        // HashMap<String, Object> requestBody = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        try {
            // HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
            String json = restTemplate.getForObject("http://localhost:8080/users", String.class);
            // System.out.println(json);
            JSONObject usersJson = new JSONObject(json);
            JSONArray jsonAdmins = usersJson.getJSONArray("users");
            ArrayList<String> emails = new ArrayList<>();
            String userName;
            String userEmail;
            System.out.println("userEmail" + " || " + "userName" + " || " + " Role");
            for (int i = 0; i < jsonAdmins.length(); i++) {
                JSONObject adminObject = jsonAdmins.getJSONObject(i);
                userName = adminObject.getString("userName");
                userEmail = adminObject.getString("userEmail");
                emails.add(userEmail);
                System.out.println(userEmail + " || " + userName + " || " + "User");

            }
            System.out.println(" Enter userEmail to update profile");
            String userEmailToUpdate = sc.nextLine();
            if (emails.contains(userEmailToUpdate)) {
                updateProfileUser(userEmailToUpdate, "user");
            } else {

            }

        } catch (Exception e) {
            System.out.println("Fail" + e.getMessage());
        }
    }

    public static void displayUsersPage(HashMap<String, String> users) {
        System.out.println("");
        System.out.println("USERS");
        System.out.println("");

        // for (iterable_type iterable_element : iterable) {

        // }
    }

    public static void updateProfileUser(String userEmail, String role) {
        if (userEmail != "") {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HashMap<String, Object> requestBody = new HashMap<>();
                requestBody.put("userEmail", userEmail);
                if (role.equals("user")) {
                    System.out.println("Enter 1 to update role to admin");
                    requestBody.put("currentRole", "user");

                    HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                    String json = restTemplate.postForObject("http://localhost:8080/user/update", request,
                            String.class);
                    System.out.println(json);
                } else if (role.equals("admin")) {
                    System.out.println("Enter 1 to update role to user");
                    requestBody.put("currentRole", "admin");

                    HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                    String json = restTemplate.postForObject("http://localhost:8080/user/update", request,
                            String.class);
                    System.out.println(json);
                }
            } catch (Exception e) {
                System.out.println("Fail" + e.getMessage());
            }
        }
    }

}
