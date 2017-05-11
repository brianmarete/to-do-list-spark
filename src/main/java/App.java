import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        staticFileLocation("/public");
        String layout = "templates/layout.vtl";

        ProcessBuilder process = new ProcessBuilder();
        Integer port;

        // This tells our app that if Heroku sets a port for us, we need to use that port.
        // Otherwise, if they do not, continue using port 4567.

        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
        }

        setPort(port);

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("categories", Category.all());
            model.put("template", "templates/index.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        get("/categories/:id/tasks/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Category category = Category.find(Integer.parseInt(req.params(":id")));
            model.put("category", category);
            model.put("template", "templates/category-tasks-form.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        get("/tasks", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("tasks", Task.all());
            model.put("template", "templates/tasks.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        get("/categories/:category_id/tasks/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Category category = Category.find(Integer.parseInt(req.params(":category_id")));
            Task task = Task.find(Integer.parseInt(req.params(":id")));
            model.put("category", category);
            model.put("task", task);
            model.put("template", "templates/task.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        post("/categories/:category_id/tasks/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Task task = Task.find(Integer.parseInt(req.params("id")));
            String description = req.queryParams("description");
            Category category = Category.find(task.getCategoryId());
            task.update(description);
            String url = String.format("/categories/%d/tasks/%d", category.getId(), task.getId());
            res.redirect(url);
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        post("/categories/:category_id/tasks/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Task task = Task.find(Integer.parseInt(req.params("id")));
            Category category = Category.find(task.getCategoryId());
            task.delete();
            model.put("category", category);
            model.put("template", "templates/category.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        post("/tasks", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Category category = Category.find(Integer.parseInt(req.queryParams("categoryId")));

            String description = req.queryParams("description");
            Task newTask = new Task(description, category.getId());
            newTask.save();

            model.put("category", category);
            model.put("template", "templates/category-tasks-success.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        get("/categories/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/category-form.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        get("/categories", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("categories", Category.all());
            model.put("template", "templates/categories.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        post("/categories", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            Category newCategory = new Category(name);
            newCategory.save();
            model.put("template", "templates/category-success.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });

        get("/categories/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Category category = Category.find(Integer.parseInt(req.params(":id")));
            model.put("category", category);
            model.put("template", "templates/category.vtl");
            return new VelocityTemplateEngine().render(new ModelAndView(model, layout));
        });
    }
}
