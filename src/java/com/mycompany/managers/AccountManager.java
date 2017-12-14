/*
 * Created by Ananthavel Guruswamy on 2017.11.28  * 
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.EntityBeans.User;
import com.mycompany.EntityBeans.UserPhoto;

import com.mycompany.FacadeBeans.UserFacade;
import com.mycompany.FacadeBeans.UserPhotoFacade;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import java.util.Date;
import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import org.primefaces.json.JSONObject;

/*
---------------------------------------------------------------------------
JSF Managed Beans annotated with @ManagedBean from javax.faces.bean
is in the process of being deprecated in favor of CDI Beans. 

You should annotate the bean class with @Named from javax.inject.Named 
package to designate the bean as managed by the
Contexts and Dependency Injection (CDI) container.

Beans annotated with @Named is the preferred approach, because CDI 
enables Java EE-wide dependency injection. 

A CDI bean is a bean managed by the CDI container. 

Within JSF facelets XHTML pages, this bean will be referenced by using the
name 'accountManager'. Actually, the default name is the class name starting
with a lower case letter and value = 'accountManager' is optional;
However, we spell it out to make our code more readable and understandable.
---------------------------------------------------------------------------
 */
@Named(value = "accountManager")

/*
The @SessionScoped annotation preserves the values of the AccountManager
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/**
 *
 * @author Anand
 */

/*
--------------------------------------------------------------------------
Marking the AccountManager class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------
 */
public class AccountManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String username;
    private String password;
    private String newPassword;

    private String firstName;
    private String lastName;

    private String address;
    private String city;
    private String state;
    private int zipcode;

    private String securityQuestion;
    private String securityAnswer;

    private String email;
    
    private int userPermission;
    private Date dateCreated;
    private int isActive;
    private String profilePicture;
    private String location;
    private double userRating;

    private final String[] listOfStates = Constants.STATES;
    private Map<String, Object> security_questions;
    private String statusMessage;

    private User selected;

    /*
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;
    
    @EJB
    private UserPhotoFacade userPhotoFacade;

    // Constructor method instantiating an instance of AccountManager
    public AccountManager() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public String[] getListOfStates() {
        return listOfStates;
    }
    
    public double getUserRating()
    {
        return userRating;
    }
    
    public void setUserRating(double userRating)
    {
        this.userRating = userRating;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public String getProfilePicture()
    {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture)
    {
        this.profilePicture = profilePicture;
    }
    
    public int getIsActive()
    {
        return isActive;
    }
    
    public void setIsActive(int isActive)
    {
        this.isActive = isActive;
    }
    
    public Date getDateCreated()
    {
        return dateCreated;
    }
    
    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }
    
    public int getUserPermission()
    {
        return userPermission;
    }
    
    public void setUserPermission(int userPermission)
    {
        this.userPermission = userPermission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zip_code) {
        this.zipcode = zip_code;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }
    
    
    public String getBoard() {
        String x = "<div class=\"well\"><div class='row'><div class='col-lg-1'><button class='btn btn-sm btn-success'>upvote </button></div><div class='col-lg-3'>Username goes here</div><br /><br /><div class='row'><div class='col-lg-1 text-center'><div class='panel panel-default'><div clas='panel-body'>10</div></div></div><div class='col-lg-11'>Reply Text goes here</div></div><div class='row'><div class='col-lg-1'><button class='btn btn-sm btn-danger'> Downvote</button></div><div class='col-lg-1'><button>Reply</button></div></div></div><br /></div>";
        return x;
            
    }
    
    public UserPhotoFacade getUserPhotoFacade() {
        return userPhotoFacade;
    }
    

    /*
    private Map<String, Object> security_questions;
        String      int
        ---------   ---
        question1,  0
        question2,  1
        question3,  2
            :
    When the user selects a security question, its number (int) is stored; not its String.
    Later, given the number (int), the security question String is retrieved.
     */
    public Map<String, Object> getSecurity_questions() {

        if (security_questions == null) {
            /*
            Difference between HashMap and LinkedHashMap:
            HashMap stores key-value pairings in no particular order. 
                Values are retrieved based on their corresponding Keys.
            LinkedHashMap stores and retrieves key-value pairings
                in the order they were put into the map.
             */
            security_questions = new LinkedHashMap<>();

            for (int i = 0; i < Constants.QUESTIONS.length; i++) {
                security_questions.put(Constants.QUESTIONS[i], i);
            }
        }
        return security_questions;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public User getSelected() {

        if (selected == null) {
            /*
            user_id (database primary key) was put into the SessionMap
            in the initializeSessionMap() method below or in LoginManager.
             */
            int userPrimaryKey = (int) FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get("user_id");
            /*
            Given the primary key, obtain the object reference of the User
            object and store it into the instance variable selected.
             */
            selected = getUserFacade().find(userPrimaryKey);
        }
        // Return the object reference of the selected User object
        return selected;
    }

    public void setSelected(User selectedUser) {
        this.selected = selectedUser;
    }

    /*
    ================
    Instance Methods
    ================
     */
    // Return True if a user is logged in; otherwise, return False
    public boolean isLoggedIn() {
        
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null;
    }

    /*
    Create a new user account. Return "" if an error occurs; otherwise,
    upon successful account creation, redirect to show the SignIn page.
     */
    public String createAccount() throws NoSuchAlgorithmException, InvalidKeySpecException {

        //-----------------------------------------------------------
        // First, check if the entered username is already being used
        //-----------------------------------------------------------
        // Obtain the object reference of a User object with username
        User aUser = getUserFacade().findByUsername(username);

        if (aUser != null) {
            // A user already exists with the username entered
            username = "";
            statusMessage = "Username already exists! Please select a different one!";
            return "";
        }

        //----------------------------------
        // The entered username is available
        //----------------------------------
        if (statusMessage == null || statusMessage.isEmpty()) {
            try {
                // Instantiate a new User object
                User newUser = new User();

                /*
                Set the properties of the newly created newUser object with the values
                entered by the user in the AccountCreationForm in CreateAccount.xhtml
                 */
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setAddress(address);
                newUser.setCity(city);
                newUser.setState(state);
                newUser.setZipcode(zipcode);
                newUser.setSecurityQuestion(securityQuestion);
                newUser.setSecurityAnswer(securityAnswer);
                newUser.setEmail(email);
                newUser.setUsername(username);
                String securedPassword = PasswordHashingManager.generateStrongPasswordHash(password);
                newUser.setPassword(securedPassword);
                
                /*
                private int userPermission;
                private Date dateCreated;
                private int isActive;
                private String profilePicture;
                private String location;
                private double userRating;
                */
                
                Date currentDate = new Date();
                
                newUser.setUserPermission(1);
                newUser.setDateCreated(currentDate);
                newUser.setIsActive(1);
                newUser.setProfilePicture("Temp Profile Pic");
                String fullAddress = address + "+" + city + "+" + state + "+" + zipcode;
                //System.out.println("address: " + fullAddress);
                try {
                    //System.out.println("attempting to geocode address");
                    URL googleGeocodeAPI = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(fullAddress, "UTF-8"));
                    URLConnection con = googleGeocodeAPI.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String input;
                    String jsonObj = "";
                    while ((input = in.readLine()) != null) {
                        jsonObj += input;
                    }
                    in.close();
                    JSONObject json = new JSONObject(jsonObj);
                    JSONObject geo = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    double lat = (double)geo.get("lat");
                    double lng = (double)geo.get("lng");
                    newUser.setLocation(lat + "," + lng);
                } catch(Exception e) {
                    newUser.setLocation("37.228265,-80.421215"); //coordinates for blacksburg
                }
                
                newUser.setUserRating(5);
//                getUserFacade().createCreditAccount(newUser.getId());
                getUserFacade().create(newUser);
                
            } catch (EJBException e) {
                username = "";
                statusMessage = "Something went wrong while creating user's account! See: " + e.getMessage();
                System.out.println("Facade is: " + getUserFacade().toString());
                return "";
            }
            // Initialize the session map for the newly created User object (see the method below)
            initializeSessionMap();
            getUserFacade().createCreditAccount(getUserFacade().findByUsername(getUsername()).getId());

            /*
            The Profile page cannot be shown since the new User has not signed in yet.
            Therefore, we show the Sign In page for the new User to sign in first.
             */
            return "SignIn.xhtml?faces-redirect=true";
        }
        return "";
    }

    // Initialize the session map for the User object
    public void initializeSessionMap() {

        // Obtain the object reference of the User object
        User user = getUserFacade().findByUsername(getUsername());

        // Put the User's object reference into session map variable user
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user", user);

        // Put the User's database primary key into session map variable user_id
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

    /*
    Update the signed-in user's account profile. Return "" if an error occurs;
    otherwise, upon successful account update, redirect to show the Profile page.
     */
    public String updateAccount() {

        if (statusMessage == null || statusMessage.isEmpty()) {

            // Obtain the signed-in user's username
            String user_name = (String) FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get("username");

            // Obtain the object reference of the signed-in user
            User editUser = getUserFacade().findByUsername(user_name);

            try {
                /*
                Set the signed-in user's properties to the values entered by
                the user in the EditAccountProfileForm in EditAccount.xhtml.
                 */
                editUser.setFirstName(this.selected.getFirstName());
                editUser.setLastName(this.selected.getLastName());

                editUser.setAddress(this.selected.getAddress());
                editUser.setCity(this.selected.getCity());
                editUser.setState(this.selected.getState());
                editUser.setZipcode(this.selected.getZipcode());
                editUser.setEmail(this.selected.getEmail());

                // It is optional for the user to change his/her password
                // Note: getNewPassword() is the getter method of the newPassword
                String new_Password = getNewPassword();

                if (new_Password == null || new_Password.isEmpty()) {
                    // Do nothing. The user does not want to change the password.
                } else {
                    editUser.setPassword(new_Password);
                    // Password changed successfully!
                    // Password was first validated by invoking the validatePasswordChange method below.
                }

                // Store the changes in the CloudDriveDB database
                getUserFacade().edit(editUser);

            } catch (EJBException e) {
                username = "";
                statusMessage = "Something went wrong while editing user's profile! See: " + e.getMessage();
                return "";
            }
            // Account update is completed, redirect to show the Profile page.
            return "Profile.xhtml?faces-redirect=true";
        }
        return "";
    }

    /*
    Delete the signed-in user's account. Return "" if an error occurs; otherwise,
    upon successful account deletion, redirect to show the index (home) page.
     */
    public String deleteAccount() {

        if (statusMessage == null || statusMessage.isEmpty()) {

            // Obtain the database primary key of the signed-in User object
            int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");

            try {
                // Delete all of the photo files associated with the signed-in user whose primary key is user_id
                deleteAllUserPhotos(user_id);

                // Delete all of the user files associated with the signed-in user whose primary key is user_id
                //deleteAllUserFiles(user_id);

                // Delete the User entity, whose primary key is user_id, from the CloudDriveDB database
                getUserFacade().deleteUser(user_id);

                statusMessage = "Your account is successfully deleted!";

            } catch (EJBException e) {
                username = "";
                statusMessage = "Something went wrong while deleting user's account! See: " + e.getMessage();
                return "";
            }

            logout();
            //return "index.html?faces-redirect=true";
        }
        return "";
    }
    
    public int getNumberOfCreditsAvailable() {
        if (selected != null && selected.getId() != null) {
            return getUserFacade().getCredits(selected.getId());
        } else {
            return 0;
        }
    }
    
    /*
    Delete both uploaded and thumbnail photo files that belong to the User
    object whose database primary key is userId
     */
    public void deleteAllUserPhotos(int userId) {

        /*
        Obtain the list of Photo objects that belong to the User whose
        database primary key is userId.
         */
        List<UserPhoto> photoList = getUserPhotoFacade().findPhotosByUserID(userId);

        if (!photoList.isEmpty()) {

            // Obtain the object reference of the first Photo object in the list.
            UserPhoto photo = photoList.get(0);
            try {
                /*
                 Delete the user's photo if it exists.
                 getFilePath() is given in UserPhoto.java file.
                 */
                Files.deleteIfExists(Paths.get(photo.getPhotoFilePath()));

                /*
                 Delete the user's thumbnail image if it exists.
                 getThumbnailFilePath() is given in UserPhoto.java file.
                 */
                Files.deleteIfExists(Paths.get(photo.getThumbnailFilePath()));

                // Delete the temporary file if it exists
                Files.deleteIfExists(Paths.get(Constants.PHOTOS_ABSOLUTE_PATH + "tmp_file"));

                // Remove the user's photo's record from the CloudDriveDB database
                getUserPhotoFacade().remove(photo);

            } catch (IOException e) {
                statusMessage = "Something went wrong while deleting user's photo! See: " + e.getMessage();
            }
        }
    }

    // Validate if the entered password matches the entered confirm password
    public void validateInformation(ComponentSystemEvent event) {

        /*
        FacesContext contains all of the per-request state information related to the processing of
        a single JavaServer Faces request, and the rendering of the corresponding response.
        It is passed to, and potentially modified by, each phase of the request processing lifecycle.
         */
        FacesContext fc = FacesContext.getCurrentInstance();

        /*
        UIComponent is the base class for all user interface components in JavaServer Faces. 
        The set of UIComponent instances associated with a particular request and response are organized into
        a component tree under a UIViewRoot that represents the entire content of the request or response.
         */
        // Obtain the UIComponent instances associated with the event
        UIComponent components = event.getComponent();

        /*
        UIInput is a kind of UIComponent for the user to enter a value in.
         */
        // Obtain the object reference of the UIInput field with id="password" on the UI
        UIInput uiInputPassword = (UIInput) components.findComponent("password");

        // Obtain the password entered in the UIInput field with id="password" on the UI
        String entered_password = uiInputPassword.getLocalValue()
                == null ? "" : uiInputPassword.getLocalValue().toString();

        // Obtain the object reference of the UIInput field with id="confirmPassword" on the UI
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");

        // Obtain the confirm password entered in the UIInput field with id="confirmPassword" on the UI
        String entered_confirm_password = uiInputConfirmPassword.getLocalValue()
                == null ? "" : uiInputConfirmPassword.getLocalValue().toString();

        if (entered_password.isEmpty() || entered_confirm_password.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!entered_password.equals(entered_confirm_password)) {
            statusMessage = "Password and Confirm Password must match!";
        } else {
            statusMessage = "";
        }
    }

    // Validate the new password and new confirm password
    public void validatePasswordChange(ComponentSystemEvent event) {
        /*
        FacesContext contains all of the per-request state information related to the processing of
        a single JavaServer Faces request, and the rendering of the corresponding response.
        It is passed to, and potentially modified by, each phase of the request processing lifecycle.
         */
        FacesContext fc = FacesContext.getCurrentInstance();

        /*
        UIComponent is the base class for all user interface components in JavaServer Faces. 
        The set of UIComponent instances associated with a particular request and response are organized into
        a component tree under a UIViewRoot that represents the entire content of the request or response.
         */
        // Obtain the UIComponent instances associated with the event
        UIComponent components = event.getComponent();

        /*
        UIInput is a kind of UIComponent for the user to enter a value in.
         */
        // Obtain the object reference of the UIInput field with id="newPassword" on the UI
        UIInput uiInputPassword = (UIInput) components.findComponent("newPassword");

        // Obtain the new password entered in the UIInput field with id="newPassword" on the UI
        String new_Password = uiInputPassword.getLocalValue()
                == null ? "" : uiInputPassword.getLocalValue().toString();

        // Obtain the object reference of the UIInput field with id="newConfirmPassword" on the UI
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("newConfirmPassword");

        // Obtain the new confirm password entered in the UIInput field with id="newConfirmPassword" on the UI
        String new_ConfirmPassword = uiInputConfirmPassword.getLocalValue()
                == null ? "" : uiInputConfirmPassword.getLocalValue().toString();

        // It is optional for the user to change his/her password
        if (new_Password.isEmpty() || new_ConfirmPassword.isEmpty()) {
            // Do nothing. The user does not want to change the password.
            return;
        }

        if (!new_Password.equals(new_ConfirmPassword)) {
            statusMessage = "New Password and New Confirm Password must match!";
        } else {
            /*
            REGular EXpression (regex) for validating password strength:
            (?=.{8,31})        ==> Validate the password to be minimum 8 and maximum 31 characters long. 
            (?=.*[!@#$%^&*()]) ==> Validate the password to contain at least one special character. 
                                   (all special characters of the number keys from 1 to 0 on the keyboard)
            (?=.*[A-Z])        ==> Validate the password to contain at least one uppercase letter. 
            (?=.*[a-z])        ==> Validate the password to contain at least one lowercase letter. 
            (?=.*[0-9])        ==> Validate the password to contain at least one number from 0 to 9.
             */
            String regex = "^(?=.{8,31})(?=.*[!@#$%^&*()])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$";

            if (!new_Password.matches(regex)) {
                statusMessage = "The password must be minimum 8 "
                        + "characters long, contain at least one special character above the numbers on the keyboard, "
                        + "contain at least one uppercase letter, "
                        + "contain at least one lowercase letter, "
                        + "and contain at least one number 0 to 9.";
            } else {
                statusMessage = "";
            }
        }
    }

    // Validate if the entered password and confirm password are correct
    public void validateUserPassword(ComponentSystemEvent event) {
        /*
        FacesContext contains all of the per-request state information related to the processing of
        a single JavaServer Faces request, and the rendering of the corresponding response.
        It is passed to, and potentially modified by, each phase of the request processing lifecycle.
         */
        FacesContext fc = FacesContext.getCurrentInstance();

        /*
        UIComponent is the base class for all user interface components in JavaServer Faces. 
        The set of UIComponent instances associated with a particular request and response are organized into
        a component tree under a UIViewRoot that represents the entire content of the request or response.
         */
        // Obtain the UIComponent instances associated with the event
        UIComponent components = event.getComponent();

        /*
        UIInput is a kind of UIComponent for the user to enter a value in.
         */
        // Obtain the object reference of the UIInput field with id="password" on the UI
        UIInput uiInputPassword = (UIInput) components.findComponent("password");

        // Obtain the password entered in the UIInput field with id="password" on the UI
        String entered_password = uiInputPassword.getLocalValue()
                == null ? "" : uiInputPassword.getLocalValue().toString();

        // Obtain the object reference of the UIInput field with id="confirmPassword" on the UI
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");

        // Obtain the confirm password entered in the UIInput field with id="confirmPassword" on the UI
        String entered_confirm_password = uiInputConfirmPassword.getLocalValue()
                == null ? "" : uiInputConfirmPassword.getLocalValue().toString();

        if (entered_password.isEmpty() || entered_confirm_password.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!entered_password.equals(entered_confirm_password)) {
            statusMessage = "Password and Confirm Password must match!";
        } else {
            // Obtain the logged-in User's username
            String user_name = (String) FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get("username");

            // Obtain the object reference of the signed-in User object
            User user = getUserFacade().findByUsername(user_name);

            if (entered_password.equals(user.getPassword())) {
                // entered password = signed-in user's password
                statusMessage = "";
            } else {
                statusMessage = "Incorrect Password!";
            }
        }
    }

    /*
    UIComponent is the base class for all user interface components in JavaServer Faces. 
    The set of UIComponent instances associated with a particular request and response are organized into
    a component tree under a UIViewRoot that represents the entire content of the request or response.
     
    @param components: UIComponent instances associated with the current request and response
    @return True if entered password is correct; otherwise, return False
     */
    private boolean correctPasswordEntered(UIComponent components) {

        // Obtain the object reference of the UIInput field with id="verifyPassword" on the UI
        UIInput uiInputVerifyPassword = (UIInput) components.findComponent("verifyPassword");

        // Obtain the verify password entered in the UIInput field with id="verifyPassword" on the UI
        String verifyPassword = uiInputVerifyPassword.getLocalValue()
                == null ? "" : uiInputVerifyPassword.getLocalValue().toString();

        if (verifyPassword.isEmpty()) {
            statusMessage = "Please enter a password!";
            return false;

        } else if (verifyPassword.equals(password)) {
            // Correct password is entered
            return true;

        } else {
            statusMessage = "Invalid password entered!";
            return false;
        }
    }

    // Show the Home page
    public void showHomePage() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect("/CookToShare/index.html");
        } catch (IOException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Show the Profile page
    public String showProfile() {
        return "Profile?faces-redirect=true";
    }

    public void logout() {

        // Clear the logged-in User's session map
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();

        // Reset the logged-in User's properties
        username = password = "";
        firstName = lastName = "";
        address = city = state = securityQuestion = "";
        securityAnswer = "";
        email = statusMessage = "";
        zipcode = 0;

        // Invalidate the logged-in User's session
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            // Redirect to show the index (Home) page
            //return "/index.html?faces-redirect=true";
            ec.redirect("/CookToShare/index.html");
        } catch (IOException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String userPhoto() {

        // Obtain the signed-in user's username
        String usernameOfSignedInUser = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");

        // Obtain the object reference of the signed-in user
        User signedInUser = getUserFacade().findByUsername(usernameOfSignedInUser);

        // Obtain the id (primary key in the database) of the signedInUser object
        Integer userId = signedInUser.getId();

        List<UserPhoto> photoList = getUserPhotoFacade().findPhotosByUserID(userId);

        if (photoList.isEmpty()) {
            /*
            No user photo exists. Return defaultUserPhoto.png 
            in CloudStorage/PhotoStorage.
             */
            return Constants.DEFAULT_PHOTO_RELATIVE_PATH;
        }

        /*
        photoList.get(0) returns the object reference of the first Photo object in the list.
        getThumbnailFileName() message is sent to that Photo object to retrieve its
        thumbnail image file name, e.g., 5_thumbnail.jpeg
         */
        String thumbnailFileName = photoList.get(0).getThumbnailFileName();

        /*
        In glassfish-web.xml file, we designated the '/CloudStorage/' directory as the
        Alternate Document Root with the following statement:
        
        <property name="alternatedocroot_1" value="from=/CloudStorage/* dir=/Users/Balci" />
        
        in Constants.java file, we defined the relative photo file path as
        
        public static final String PHOTOS_RELATIVE_PATH = "CloudStorage/PhotoStorage/";
        
        Thus, JSF knows that 'CloudStorage/' is the document root directory.
         */
        String relativePhotoFilePath = Constants.PHOTOS_RELATIVE_PATH + thumbnailFileName;

        return relativePhotoFilePath;
    }

    /*
        from user_id --> to user_id
        from - credit -- to + credit
    */
    public void creditTransferFromTo(Integer from, Integer to, int cost) {
        getUserFacade().withdrawAccount(from, cost);
        getUserFacade().creditAccount(to, cost);
    }

}
