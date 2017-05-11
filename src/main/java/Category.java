import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Category {
    private String name;
    private int id;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Category> all() {
        String sql = "SELECT id, name FROM categories;";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Category.class);
        }
    }

    public int getId() {
        return id;
    }

    public static Category find(int id) {
        String sql = "SELECT * FROM categories WHERE id=:id;";
        try(Connection con = DB.sql2o.open()) {
            Category category = con.createQuery(sql)
                .addParameter("id", id)
                .executeAndFetchFirst(Category.class);
            return category;
        }
    }

    public List<Task> getTasks() {
        String sql = "SELECT * FROM tasks WHERE categoryId=:id";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql)
                .addParameter("id", this.id)
                .executeAndFetch(Task.class);
        }
    }

    @Override
    public boolean equals(Object otherCategory) {
        if (otherCategory instanceof Category) {
            Category newCategory = (Category) otherCategory;
            return this.getName().equals(newCategory.getName()) &&
                this.getId() == newCategory.getId();
        }

        return false;
    }

    public void save() {
        String sql = "INSERT INTO categories (name) VALUES (:name);";
        try(Connection con = DB.sql2o.open()) {
            this.id = (int) con.createQuery(sql, true)
                .addParameter("name", this.name)
                .executeUpdate()
                .getKey();
        } 
    }
}
