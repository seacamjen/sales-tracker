import org.sql2o.*;
import java.util.List;

public class Product {
  private String name;
  private int price;
  private String description;
  private int categoryId;
  private int id;

  public Product(String name, int price, String description, int categoryId) {
    this.name = name;
    this.price = price;
    this.description = description;
    this.categoryId = categoryId;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public String getDescription() {
    return description;
  }

  public int getId() {
    return id;
  }

  public int getCategoryId() {
    return categoryId;
  }

  @Override
  public boolean equals(Object otherProduct) {
    if (!(otherProduct instanceof Product)) {
      return false;
    } else {
      Product newProduct = (Product) otherProduct;
      return this.getName().equals(newProduct.getName()) &&
             this.getDescription().equals(newProduct.getDescription()) &&
             this.getPrice() == newProduct.getPrice() &&
             this.getCategoryId() == newProduct.getCategoryId() &&
             this.getId() == newProduct.getId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO products (name, price, description, categoryId) VALUES (:name, :price, :description, :categoryId);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", name)
        .addParameter("price", price)
        .addParameter("description", description)
        .addParameter("categoryId", categoryId)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Product> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products;";
      return con.createQuery(sql)
        .executeAndFetch(Product.class);
    }
  }

  public static Product find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products WHERE id = :id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Product.class);
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM products WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void update(String name, int price, String description, int categoryId) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE products SET (name, price, description, categoryId) = (:name, :price, :description, :categoryId) WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("price", price)
        .addParameter("description", description)
        .addParameter("categoryId", categoryId)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public static List<Product> search(String input) {
    String newInput = "%" + input + "%";
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products WHERE lower(name) LIKE lower(:newInput) OR lower(description) LIKE lower(:newInput);";
      return con.createQuery(sql)
        .addParameter("newInput", newInput)
        .executeAndFetch(Product.class);
    }
  }

  public static List<Product> getCategoryProducts(int categoryId) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products WHERE categoryId = :categoryId;";
      return con.createQuery(sql)
        .addParameter("categoryId", categoryId)
        .executeAndFetch(Product.class);
    }
  }

}
