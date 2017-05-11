import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;

public class CategoryTest {
    
    private Category testCategory;

    @Rule
    public DatabaseRule database = new DatabaseRule();

    @Before
    public void setUp() {
        testCategory = new Category("Home");
    }

    @Test
    public void category_instantiatesCorrectly_true() {
        assertTrue(testCategory instanceof Category);
    }

    @Test
    public void getName_categoryInstantiatesWithName_Home() {
        assertEquals("Home", testCategory.getName());
    }

    @Test
    public void all_returnsAllInstancesOfCategory_true() {
        Category firstCategory = new Category("Home");
        firstCategory.save();
        Category secondCategory = new Category("Work");
        secondCategory.save();
        assertTrue(Category.all().get(0).equals(firstCategory));
        assertTrue(Category.all().get(1).equals(secondCategory));
    }

    @Test
    public void getId_categoriesInstantiateWithAnId_1() {
        testCategory.save();
        assertTrue(testCategory.getId() > 0);
    }

    @Test
    public void find_returnsCategoryWithSameId_secondCategory() {
        Category firstCategory = new Category("Home");
        firstCategory.save();
        Category secondCategory = new Category("Work");
        secondCategory.save();
        assertEquals(Category.find(secondCategory.getId()), secondCategory);
    }

    @Test
    public void getTasks_initiallyReturnsEmptyList_ArrayList() {
        assertEquals(0, testCategory.getTasks().size());
    }

    @Test
    public void getTasks_retrievesAllTasksFromDatabase_tasksList() {
        Category myCategory = new Category("Household chores");
        myCategory.save();
        Task firstTask = new Task("Mow the lawn", myCategory.getId());
        firstTask.save();
        Task secondTask = new Task("Do the dishes", myCategory.getId());
        secondTask.save();
        Task[] tasks = new Task[] { firstTask, secondTask };
        assertTrue(myCategory.getTasks().containsAll(Arrays.asList(tasks)));
    }

    @Test
    public void find_returnsNullWhenNoTaskFound_null() {
        assertNull(Category.find(999));
    }

    @Test
    public void equals_returnsTrueIfNamesAretheSame() {
        Category firstCategory = new Category("Household chores");
        Category secondCategory = new Category("Household chores");
        assertTrue(firstCategory.equals(secondCategory));
    }

    @Test
    public void save_savesIntoDatabase_true() {
        Category myCategory = new Category("Household chores");
        myCategory.save();
        assertTrue(Category.all().get(0).equals(myCategory));
    }

    @Test
    public void save_assignsIdToObject() {
        Category myCategory = new Category("Household chores");
        myCategory.save();
        Category savedCategory = Category.all().get(0);
        assertEquals(myCategory.getId(), savedCategory.getId());
    }

    @Test
    public void save_savesCategoryIdIntoDB_true() {
        Category myCategory = new Category("Household chores");
        myCategory.save();
        Task myTask = new Task("Mow the lawn", myCategory.getId());
        myTask.save();
        Task savedTask = Task.find(myTask.getId());
        assertEquals(savedTask.getCategoryId(), myCategory.getId());
    }
}
