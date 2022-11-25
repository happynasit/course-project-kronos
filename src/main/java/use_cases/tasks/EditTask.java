package use_cases.tasks;

import entities.Task;
import entities.User;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class EditTask {
    // Edit properties of a task. Properties include: name, completion status, reminder setting, visibility, category,
    // and deadline.

    /**
     * Edit the name of the task.
     * @param userA - The target user.
     * @param t - Target task.
     * @param name - The name of the task.
     */
    public void editName(User userA, Task t, String name){
        //userA.getTaskCollection.getItem().setName(name);
        t.setName(name);
    }

    /**
     * Edit the completion status of the task. If the task is currently completed, then it will be marked as incomplete.
     * If not, then the task will be marked as completed.
     *
     * @param t - Target task.
     */
    public void editStatus(User userA, Task t){
        if (t.completeStatus == TRUE){
            t.markAsIncomplete();
        }else {t.markAsComplete();}
    }

    /**
     * Edit the name of the task.
     * @param t - Target task.
     * @param visible - The target visibility setting after the edit.
     */
    public void editVisibility(Task t, boolean visible){
        t.setVisibility(visible);
    }

    public void editCategory(Task t, String category){


    }



    // Database needs to be updated.

}
