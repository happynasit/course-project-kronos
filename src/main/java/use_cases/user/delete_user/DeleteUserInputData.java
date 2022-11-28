package use_cases.user.delete_user;

import entities.UserEntity;

/**
 * This class is a container for the input data related to deletion of a user, it is created by the controller and sent
 * to the interactor/use case.
 */
public class DeleteUserInputData {
    private final UserEntity user;


    /**
     * Constructor for a DeleteUserInputData object.
     * @param user - the input user which will be deleted. The input user is always existing in the database.
     */
    public DeleteUserInputData(UserEntity user, String emailaddress) {
        this.user = user;

    }

    /**
     * @return the user that will be deleted / the user attribute of a DeleteUserInputData object.
     */
    public UserEntity getUser() {
        return user;
    }
}
