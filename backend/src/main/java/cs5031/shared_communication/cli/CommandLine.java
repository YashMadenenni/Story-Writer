package cs5031.shared_communication.cli;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import cs5031.shared_communication.model.Model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class CommandLine {
    static Model model;
    /**
     *@param globalUserEmail- stores the users that logged in to identify globally
     *@param admin - checks if the admin was logged in - helper variable
     */
    static String globalUserEmail;
    static String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    static String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    static String ANSI_GREY_BACKGROUND = "\u001B[90m";
    static String ANSI_RESET = "\u001B[0m";
    static String ANSI_RED = "\u001B[31m";
    static Boolean admin = false;

    /**Main method of CLI invokes login
     * @param args
     */
    public static void main(String args[]) {

        login();

    }

    //Constructor
    public CommandLine(){
        login();
    }

    /**
     * Helper method
     */
    public static void space() {
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }

    /**
     * Helper Method to log/register in
     */
    public static void login() {

        space();

        System.out.println(" Enter 1 to login and 2 to register ");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        sc.nextLine();
        if (option == 1) {
            System.out.println(ANSI_YELLOW_BACKGROUND + " LOGIN PAGE " + ANSI_RESET);
            loginUser();
        } else if (option == 2) {
            System.out.println(ANSI_YELLOW_BACKGROUND + " REGISTER PAGE " + ANSI_RESET);
            registerUser();
        } else {

            login();
        }
    }

    /**Method to register user
     * 
     */
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

        System.out.println(ANSI_YELLOW_BACKGROUND + " Enter 1 if User and 2 if admin " + ANSI_RESET);
        int option = sc.nextInt();
        if (option == 1) {
            try {

                space();
                HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                String json = restTemplate.postForObject("http://localhost:8080/user", request, String.class);
                space();
                if (json.equals("Success")) {
                    System.out.println(ANSI_GREEN_BACKGROUND + " Success " + ANSI_RESET);
                    login();
                } else {
                    System.out.println(ANSI_RED + "Failed to Register" + ANSI_RESET);
                    login();
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
                login();
            }

        } else if (option == 2) {
            try {
                space();
                HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                String json = restTemplate.postForObject("http://localhost:8080/admin", request, String.class);
                space();
                if (json.equals("Success")) {
                    System.out.println(ANSI_GREEN_BACKGROUND + "Success" + ANSI_RESET);

                    login();
                } else {
                    System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
                    login();
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
                login();
            }
        } else {
            System.out.println(ANSI_RED + "enter correct option " + ANSI_RESET);
        }
    }

    /**
     * Method to goin in user
     */
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
        System.out.println(ANSI_YELLOW_BACKGROUND + "Enter 1 if User and 2 if admin" + ANSI_RESET);
        int option = sc.nextInt();
        if (option == 1) {
            try {
                space();
                HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                String json = restTemplate.postForObject("http://localhost:8080/userLogin", request, String.class);
                space();
                if (json.equals("Success")) {
                    System.out.println(ANSI_GREEN_BACKGROUND + "Success" + ANSI_RESET);
                    globalUserEmail = userEmail;
                    mainPage(userEmail);

                } else {
                    login();
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
                space();
                login();
            }

        } else if (option == 2) {
            try {
                space();

                HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                String json = restTemplate.postForObject("http://localhost:8080/adminLogin", request, String.class);
                space();

                if (json.equals("Success")) {
                    System.out.println(ANSI_GREEN_BACKGROUND + "Success" + ANSI_RESET);
                    globalUserEmail = userEmail;
                    adminView(userEmail);
                } else {
                    System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
                    login();
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
                login();
            }
        } else {
            System.out.println("enter correct option");
            login();
        }
    }

    /**Method to view admins view
     * @param userEmail - admin email
     */
    public static void adminView(String userEmail) {
        try {

            admin = true;
            System.out.println(ANSI_YELLOW_BACKGROUND + "Enter 1 for pages and 2 for users" + ANSI_RESET);
            Scanner sc = new Scanner(System.in);
            int option2 = sc.nextInt();
            if (option2 == 1) {
                mainPage(userEmail);

                exitMethod();
            } else if (option2 == 2) {
                getSystemUsers();
            }

        } catch (Exception e) {
            System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
            login();
        }
    }

    /**Method to display all pages in the system for admin
     * @param userEmail - admin email
     */
    public static void allPagesInSystem(String userEmail) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HashMap<String, String> allPages = new HashMap<>();
            String jsonResponse = restTemplate.getForObject("http://localhost:8080/page/admin?userEmail=" + userEmail,
                    String.class);

            System.out.println(jsonResponse);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error" + ANSI_RESET);
        }

    }

    /**Method to display for users main page
     * @param userEmail 
     */
    public static void mainPage(String userEmail) {

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
        String json;
        try {
            space();
            if (!admin) {

                json = restTemplate.getForObject("http://localhost:8080/page?userEmail=" + userEmail, String.class);
                space();
                System.out.println(ANSI_YELLOW_BACKGROUND + " YOUR PAGES " + ANSI_RESET);
            } else {

                json = restTemplate.getForObject("http://localhost:8080/page/admin?userEmail=" + userEmail,
                        String.class);
                space();
                System.out.println(ANSI_YELLOW_BACKGROUND + " ALL PAGES " + ANSI_RESET);
            }

            JSONArray pages = new JSONArray(json);
            // space();

            for (int i = 0; i < pages.length(); i++) {
                JSONObject adminObject = pages.getJSONObject(i);
                pageTitle = adminObject.getString("title");
                pageContent = adminObject.getString("content");

                editAccessUser = adminObject.getString("editAccessUser");
                readAccessUser = adminObject.getString("readAccessUser");
                allPages.put(pageTitle, pageContent);
                allEditUsers.put(pageTitle, editAccessUser);
                allReadAccessUser.put(pageTitle, readAccessUser);
                System.out.println(ANSI_GREEN_BACKGROUND + " Title : " + pageTitle + "   " + ANSI_RESET);

            }
            System.out.println("");
            System.out.println(" Select a page title ");
            String pageToviewUser = sc.nextLine();
            System.out.println("");
            displayPages(pageToviewUser, allPages, userEmail, allReadAccessUser, allEditUsers);

        } catch (Exception e) {
            System.out.println(ANSI_RED + " No pages for you " + ANSI_RED);
            createNewPage(userEmail, allPages);

        }

    }

    /**Helper method to display pages 
     * @param pageToviewUser - page title 
     * @param allPages - constains all pages and content
     * @param userEmail - user that is logged in
     * @param allReadAccessUser- the read access list
     * @param allEditUsers- the edit access list
     * @throws JSONException
     */
    private static void displayPages(String pageToviewUser, HashMap<String, String> allPages, String userEmail,
            HashMap<String, String> allReadAccessUser, HashMap<String, String> allEditUsers) throws JSONException {

        Console console = System.console();
        String newContent = "";
        Scanner sc = new Scanner(System.in);

        if (allPages.containsKey(pageToviewUser)) {
            int option;
            if (!admin) {
                System.out.println(ANSI_YELLOW_BACKGROUND + "Enter 1 to view page 2 to create new page" + ANSI_RESET);
                option = sc.nextInt();
            } else {
                option = 1;
            }

            if (option == 1) {

                space();
                System.out.println(ANSI_YELLOW_BACKGROUND + "Title : " + pageToviewUser + ANSI_RESET);
                System.out.println(ANSI_GREEN_BACKGROUND + "Content :" + allPages.get(pageToviewUser) + ANSI_RESET);
                newContent = allPages.get(pageToviewUser);
                System.out.println("");
                if (!admin) {
                    System.out.println("Enter 1 to edit content ");
                    System.out.println("Enter 2 to see users that have access ");
                    System.out.println("Enter 3 to give access to a user");
                } else {
                    System.out.println("Enter 1 to see users that have access ");
                }

                int option2 = sc.nextInt();
                if (option2 == 1 && !admin) {

                    System.out.println(ANSI_YELLOW_BACKGROUND + "EDIT PAGE" + ANSI_RESET);

                    String addedContent = console.readLine(allPages.get(pageToviewUser) + " ");
                    newContent = newContent + addedContent;
                    System.out.println("");
                    System.out.println(ANSI_YELLOW_BACKGROUND + "New Content" + ANSI_RESET);

                    System.out.println(newContent);
                    postToPage(pageToviewUser, newContent, userEmail);

                } else if ((option2 == 2) || (option2 == 1 && admin)) {
                    System.out.println(ANSI_YELLOW_BACKGROUND + "USERS PAGE" + ANSI_RESET);
                    displayUserAccess(pageToviewUser, allReadAccessUser, allEditUsers);
                    exitMethod();
                } else if (option2 == 3 && !admin) {
                    System.out.println(ANSI_YELLOW_BACKGROUND + "EDIT USER ACCESS PAGE" + ANSI_RESET);
                    addUser(pageToviewUser);
                    exitMethod();

                } else {
                    exitMethod();
                }
            } else if (option == 2) {

                createNewPage(userEmail, allPages);
                exitMethod();
            } else {
                System.out.println("Enter correct pagetitle");
                exitMethod();
            }
        } else {
            System.out.println("Enter correct pagetitle");
            exitMethod();
        }
    }

    
    /**Helper Method to display all users that have display access to a page
     * @param pageToviewUser
     * @param allReadAccessUser
     * @param allEditUsers
     */
    public static void displayUserAccess(String pageToviewUser, HashMap<String, String> allReadAccessUser,
            HashMap<String, String> allEditUsers) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("");
            System.out.println("Enter 1 to view users that have access to page");
            System.out.println("Enter 2 to view users that have edit access to page");
            int option3 = sc.nextInt();
            if (option3 == 1) {
                System.out.println(ANSI_YELLOW_BACKGROUND + "Users that have access to page" + ANSI_RESET);
                JSONArray usersJson = new JSONArray(allReadAccessUser.get(pageToviewUser));
                // System.out.println(usersJson);

                if (usersJson.length() > 0) {
                    for (int i = 0; i < usersJson.length(); i++) {
                        String str = usersJson.getString(i);
                        System.out.println(str);
                    }
                }

            } else if (option3 == 2) {
                System.out.println(ANSI_YELLOW_BACKGROUND + "Users that have edit access" + ANSI_RESET);
                JSONArray usersJson = new JSONArray(allEditUsers.get(pageToviewUser));
                if (usersJson.length() > 0) {
                    for (int i = 0; i < usersJson.length(); i++) {
                        String str = usersJson.getString(i);
                        System.out.println(str);
                    }
                }

            } else {
                System.out.println(ANSI_RED + "error" + ANSI_RESET);
                exitMethod();
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error" + e + ANSI_RED);
            exitMethod();
        }
    }

    /**Method to add user
     * @param pageToviewUser - page to which user is to be added
     */
    public static void addUser(String pageToviewUser) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Console console = System.console();

            System.out.println(ANSI_YELLOW_BACKGROUND + "Enter user to be added" + ANSI_RESET);
            String userAdd = console.readLine();

            HashMap<String, Object> requestBody = new HashMap<>();
            requestBody.put("pageName", pageToviewUser);
            requestBody.put("userEmail", userAdd);

            space();
            HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
            String jsonPayload = restTemplate.postForObject("http://localhost:8080/page/access", request,
                    String.class);
            space();
            if (jsonPayload.equals("Success")) {
                System.out.println(ANSI_GREEN_BACKGROUND + jsonPayload + ANSI_RESET);
            } else {
                exitMethod();
            }

        } catch (Exception e) {
            exitMethod();
        }
    }

    /**Method to create new pagee
     * @param userEmail - user 
     * @param allPages - has all exsisting pages
     */
    public static void createNewPage(String userEmail, HashMap<String, String> allPages) {
        try {
            System.out.println(ANSI_YELLOW_BACKGROUND + " CREATE PAGE " + ANSI_RESET);
            Console console = System.console();
            RestTemplate restTemplate = new RestTemplate();

            System.out.println("Enter New page name");
            String newPageName = console.readLine();
            HashMap<String, Object> requestBody = new HashMap<>();
            requestBody.put("userEmail", userEmail);
            requestBody.put("pageName", newPageName);
            if (!allPages.containsKey(newPageName) && newPageName != "") {
                try {
                    space();
                    HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                    String jsonResponse = restTemplate.postForObject("http://localhost:8080/page", request,
                            String.class);
                    space();
                    if (jsonResponse.equals("Success")) {
                        System.out.println(ANSI_GREEN_BACKGROUND + "Success" + ANSI_RESET);
                        exitMethod();
                    } else {
                        exitMethod();
                    }

                } catch (Exception e) {
                    System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
                    exitMethod();
                }
            } else {
                System.out.println(ANSI_RED + "Duplicate page" + ANSI_RESET);
                exitMethod();
            }
        } catch (Exception e) {
            exitMethod();
        }
    }

    /**Method to add content to a page
     * @param pageToviewUser - page title
     * @param newContent - added content
     * @param userEmail - user that added
     */
    private static void postToPage(String pageToviewUser, String newContent, String userEmail) {

        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("userEmail", userEmail);
        requestBody.put("content", newContent);
        requestBody.put("pageName", pageToviewUser);

        try {
            space();
            HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
            String json = restTemplate.postForObject("http://localhost:8080/page/content", request, String.class);
            space();
            if (json.equals("Success")) {
                System.out.println(ANSI_GREEN_BACKGROUND + "Success" + ANSI_RESET);
                exitMethod();
            } else {
                exitMethod();
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Fail" + e.getMessage() + ANSI_RESET);
            exitMethod();
        }

    }

    /**Method to get ass users in the system
     * helper method for admin
     */
    public static void getSystemUsers() {
        space();
        System.out.println(ANSI_YELLOW_BACKGROUND + "Users" + ANSI_RESET);
        RestTemplate restTemplate = new RestTemplate();
        // HashMap<String, Object> requestBody = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        try {

            String json = restTemplate.getForObject("http://localhost:8080/users", String.class);
            JSONObject usersJson = new JSONObject(json);
            JSONArray jsonAdmins = usersJson.getJSONArray("users");
            ArrayList<String> emails = new ArrayList<>();
            String userName;
            String userEmail;
            space();
            System.out.println(ANSI_YELLOW_BACKGROUND + " ALL USERS PAGE " + ANSI_RESET);
            System.out.println(
                    ANSI_YELLOW_BACKGROUND + "userEmail" + " || " + "userName" + " || " + " Role" + ANSI_RESET);
            for (int i = 0; i < jsonAdmins.length(); i++) {
                JSONObject adminObject = jsonAdmins.getJSONObject(i);
                userName = adminObject.getString("userName");
                userEmail = adminObject.getString("userEmail");
                emails.add(userEmail);
                System.out.println(userEmail + " || " + userName + " || " + "User");

            }
            System.out.println("");
            System.out.println(ANSI_YELLOW_BACKGROUND+" Enter userEmail to update profile to admin"+ANSI_RESET);
            String userEmailToUpdate = sc.nextLine();
            if (emails.contains(userEmailToUpdate)) {
                updateProfileUser(userEmailToUpdate, "user");
                adminView(globalUserEmail);
            } else {
                System.out.println(ANSI_RED + "Error" + ANSI_RED);
                adminView(globalUserEmail);
            }

        } catch (Exception e) {
            System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
            adminView(globalUserEmail);
        }
    }

    /**Method to update user to admin by admin
     * @param userEmail - user to be updated 
     * @param role - currnet role
     */
    public static void updateProfileUser(String userEmail, String role) {

        if (userEmail != "") {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HashMap<String, Object> requestBody = new HashMap<>();
                requestBody.put("userEmail", userEmail);
                if (role.equals("user")) {

                    requestBody.put("currentRole", "user");
                    space();
                    HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                    String json = restTemplate.postForObject("http://localhost:8080/user/update", request,
                            String.class);
                    space();
                    if (json.equals("Success")) {
                        System.out.println(ANSI_GREEN_BACKGROUND + "Success" + ANSI_RESET);
                        exitMethod();
                    } else {
                        exitMethod();
                    }
                } else if (role.equals("admin")) {
                    System.out.println("Enter 1 to update role to user");
                    requestBody.put("currentRole", "admin");
                    space();
                    HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody);
                    String json = restTemplate.postForObject("http://localhost:8080/user/update", request,
                            String.class);
                    space();
                    if (json.equals("Success")) {
                        System.out.println(ANSI_GREEN_BACKGROUND + "Success" + ANSI_RESET);
                        exitMethod();
                    } else {
                        exitMethod();
                    }
                } else {
                    exitMethod();
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Fail" + ANSI_RESET);
                exitMethod();
            }
        }
    }

    /**Helper method to exit 
     * 
     */
    public static void exitMethod() {
        Scanner sc = new Scanner(System.in);
        space();

        if (!admin) {
            System.out.println("Enter 1 to exit 0 to continue");
            if (sc.nextInt() == 0) {
                mainPage(globalUserEmail);
            } else {
                System.exit(0);
            }
        } else {
            adminView(globalUserEmail);
        }
    }


}
