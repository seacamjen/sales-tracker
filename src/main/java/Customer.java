import org.sql2o.*;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Customer {
  private String email;
  private String name;
  private int id;

  public Customer(String email, String name) {
    this.email = email;
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherCustomer) {
    if (!(otherCustomer instanceof Customer)) {
      return false;
    } else {
      Customer newCustomer = (Customer) otherCustomer;
      return this.getEmail().equals(newCustomer.getEmail()) &&
             this.getName().equals(newCustomer.getName()) &&
             this.getId() == newCustomer.getId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO customers (email, name) VALUES (:email, :name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("email", email)
        .addParameter("name", name)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Customer> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM customers;";
      return con.createQuery(sql)
        .executeAndFetch(Customer.class);
    }
  }

  public static Customer find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM customers WHERE id = :id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Customer.class);
    }
  }

  public static Customer findByEmail(String email) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM customers WHERE email = :email;";
      Customer foundCustomer = con.createQuery(sql)
        .addParameter("email", email)
        .executeAndFetchFirst(Customer.class);
      if (foundCustomer == null) {
        throw new NoSuchElementException("No customer with that email.");
      }
      return foundCustomer;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM customers WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void update(String email, String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE customers SET (email, name) = (:email, :name) WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("email", email)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public static List<Customer> search(String input) {
    String newInput = "%" + input + "%";
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM customers WHERE lower(name) LIKE lower(:newInput) OR lower(email) LIKE lower(:newInput);";
      return con.createQuery(sql)
        .addParameter("newInput", newInput)
        .executeAndFetch(Customer.class);
    }
  }

  public void purchase(int productId) {
    Purchase newPurchase = new Purchase(this.id, productId);
    newPurchase.save();
  }

  public List<Map<String,Object>> getHistory() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT products.name, products.price, purchases.purchaseDate FROM products INNER JOIN purchases ON products.id = purchases.productId WHERE purchases.customerId = :id;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetchTable().asList();
    }
  }


}
