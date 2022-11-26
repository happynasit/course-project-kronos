package use_cases.user;

import entities.User;

public interface UserDataAccessInterface {
    // Check that user exists in Database
    public abstract boolean CheckUserExists(String emailAddress);

    // Login the user, all factories available if user is logged in
    public abstract User LoginUser(String emailAddress, String password);

    // Update the database with new user object
    public abstract void UpdateUser(User user);

    // Delete user from database
    public abstract void DeleteUser(String emailAddress);

    // Add user to database
    public abstract void AddUser(User user);

    // Get current User that has been logged in, returns null if user isn't logged in
    public abstract User GetUser();
}
