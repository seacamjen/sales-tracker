import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;
import java.text.DateFormat;

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
      model.put("monthlyReport", Purchase.monthlySales());
      model.put("quarterlyReport", Purchase.quarterlySales());
      model.put("monthlyTotal", Purchase.monthlyTotalSales());
      model.put("quarterlyTotal", Purchase.quarterlyTotalSales());
      model.put("datetime", DateFormat.getDateTimeInstance());
      model.put("user", request.session().attribute("user"));
      model.put("template", "templates/admin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/products", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String[] categories = request.queryParamsValues("category");
      List<Product> allProducts = new ArrayList<Product>();
      if (!(categories == null)) {
        for (String category : categories) {
          allProducts.addAll(Product.getCategoryProducts(Integer.parseInt(category)));
        }
        model.put("products", allProducts);
      } else {
        response.redirect("/");
      }
      model.put("categories", Category.all());
      model.put("user", request.session().attribute("user"));
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/products/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("product", Product.find(Integer.parseInt(request.params(":id"))));
      model.put("user", request.session().attribute("user"));
      model.put("template", "templates/product.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/products/:id/purchase", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Customer customer = request.session().attribute("user");
      customer.purchase(Integer.parseInt(request.params(":id")));
      String url = String.format("/customers/%d", customer.getId());
      response.redirect(url);
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
      response.redirect(request.headers("Referer"));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/customers/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Customer customer = Customer.find(Integer.parseInt(request.params(":id")));
      model.put("customer", customer);
      model.put("datetime", DateFormat.getDateTimeInstance());
      model.put("user", request.session().attribute("user"));
      model.put("template", "templates/customer.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/customers/:id/makeAdmin", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Customer customer = Customer.find(Integer.parseInt(request.params(":id")));
      customer.setAdmin();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/customers/:id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Customer customer = Customer.find(Integer.parseInt(request.params(":id")));
      String name = request.queryParams("name");
      String email = request.queryParams("email");
      customer.update(email, name);
      response.redirect(request.headers("Referer"));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/search", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String search = request.queryParams("searchInput");
      Customer customer = request.session().attribute("user");
      if ((customer != null) && (customer.getAdmin())) {
        model.put("customers", Customer.search(search));
      }
      model.put("products", Product.search(search));
      model.put("user", request.session().attribute("user"));
      model.put("template", "templates/results.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
