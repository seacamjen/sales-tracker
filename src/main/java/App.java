import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("products", Product.all());
      model.put("categories", Category.all());
      model.put("user", request.session().attribute("user"));
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/admin", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("products", Product.all());
      model.put("customers", Customer.all());
      model.put("categories", Category.all());
      model.put("monthlyTotal", Purchase.monthlyTotalSales());
      model.put("quarterlyTotal", Purchase.quarterlyTotalSales());
      model.put("user", request.session().attribute("user"));
      model.put("template", "templates/admin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/products", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String[] categories = request.queryParamsValues("category");
      List<Product> allProducts = new ArrayList<Product>();
      for (String category : categories) {
        allProducts.addAll(Product.getCategoryProducts(Integer.parseInt(category)));
      }
      model.put("products", allProducts);
      model.put("categories", Category.all());
      model.put("user", request.session().attribute("user"));
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/products/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String productName = request.queryParams("productName");
      int productPrice = Integer.parseInt(request.queryParams("productPrice"));
      String productDescription = request.queryParams("productDescription");
      int productCategory = Integer.parseInt(request.queryParams("productCategory"));
      Product newProduct = new Product(productName, productPrice, productDescription, productCategory);
      newProduct.save();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/logout", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      request.session().removeAttribute("user");
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/login", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String email = request.queryParams("email");
      Customer customer;
      try {
        customer = Customer.findByEmail(email);
      } catch (NoSuchElementException exception) {
        customer = new Customer(email, name);
        customer.save();
      }
      request.session().attribute("user", customer);
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/customers/:id/makeAdmin", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Customer customer = Customer.find(Integer.parseInt(request.params(":id")));
      customer.setAdmin();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
