import org.sql2o.*;
import java.time.LocalDateTime;
import org.junit.*;
import static org.junit.Assert.*;

public class TaskTest {
    Task myTask;

    @Rule
    public DatabaseRule database = new DatabaseRule();

    @Before
    public void setUp() {
        myTask = new Task("Mow the lawn", 1);
    }

    @Test
    public void Task_instantiatesCorrectly_true() {
        assertTrue(myTask instanceof Task);
    }

    @Test
    public void Task_instantiatesWithDescription_String() {
        assertEquals("Mow the lawn", myTask.getDescription());
    }

    @Test
    public void isCompleted_isFalseAfterInstantiation_false() {
        assertFalse(myTask.isCompleted());
    }

    @Test
    public void getCreatedAt_instantiatesWithCurrentTime_today() {
        assertEquals(LocalDateTime.now().getDayOfWeek(), myTask.getCreatedAt().getDayOfWeek());
    }

    @Test
    public void all_returnsAllInstancesOfTask_true() {
        Task firstTask = new Task("Mow the lawn", 1);
        firstTask.save();
        Task secondTask = new Task("Buy groceries", 1);
        secondTask.save();
        assertTrue(Task.all().get(0).equals(firstTask));
        assertTrue(Task.all().get(1).equals(secondTask));
    }

    @Test
    public void getId_tasksInstantiateWithAnID_1() {
        myTask.save();
        assertTrue(myTask.getId() > 0);
    }

    @Test
    public void find_returnsTaskWithSameId_secondTask() {
        Task firstTask = new Task("Mow the lawn", 1);
        firstTask.save();
        Task secondTask = new Task("Buy groceries", 1);
        secondTask.save();
        assertEquals(Task.find(secondTask.getId()), secondTask);
    }

    @Test
    public void equals_returnsTrueIfDescriptionsAreTheSame() {
        Task firstTask = new Task("Mow the lawn", 1);
        Task secondTask = new Task("Mow the lawn", 1);
        assertTrue(firstTask.equals(secondTask));
    }

    @Test
    public void save_returnsTrueIfDescriptionsAreTheSame() {
        myTask.save();
        assertTrue(Task.all().get(0).equals(myTask));
    }

    @Test
    public void save_assignsIdToObject() {
        myTask.save();
        Task savedTask = Task.all().get(0);
        assertEquals(myTask.getId(), savedTask.getId());
    }

    @Test
    public void update_updatesTaskDescription_true() {
        myTask.save();
        myTask.update("Take a nap");
        assertEquals("Take a nap", Task.find(myTask.getId()).getDescription());
    }

    @Test
    public void delete_deletesTask_true() {
        myTask.save();
        int myTaskId = myTask.getId();
        myTask.delete();
        assertNull(Task.find(myTaskId));
    }
}
