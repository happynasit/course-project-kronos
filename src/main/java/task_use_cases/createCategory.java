package task_use_cases;
import entities.Category;

public class createCategory {

    //input bound will have to check that the input is valid (colour should have static FINAL options, no repeat names)
    public void createCategory(String name, String colour) {
        Category category = new Category(name, colour);
    }
}
