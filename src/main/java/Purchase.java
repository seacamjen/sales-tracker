import org.sql2o.*;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Date;

public class Purchase {
  private int id;
  private int customerId;
  private int productId;
  private Timestamp purchaseDate;

  public Purchase(int customerId, int productId) {
    this.customerId = customerId;
    this.productId = productId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public int getProductId() {
    return productId;
  }

  public int getId() {
    return id;
  }

  public Timestamp getPurchaseDate() {
    return purchaseDate;
  }

  @Override
  public boolean equals(Object otherPurchase) {
    if (!(otherPurchase instanceof Purchase)) {
      return false;
    } else {
      Purchase newPurchase = (Purchase) otherPurchase;
      return this.getCustomerId() == newPurchase.getCustomerId() &&
             this.getProductId() == newPurchase.getProductId() &&
             this.getId() == newPurchase.getId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO purchases (customerId, productId, purchaseDate) VALUES (:customerId, :productId, now());";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("customerId", this.customerId)
        .addParameter("productId", this.productId)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Purchase> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM purchases;";
      return con.createQuery(sql)
        .executeAndFetch(Purchase.class);
    }
  }

  public static Purchase find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM purchases WHERE id = :id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Purchase.class);
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM purchases WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void update(int customerId, int productId) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE purchases SET (customerId, productId) = (:customerId, :productId) WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("customerId", customerId)
        .addParameter("productId", productId)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void setPurchaseDate() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE purchases SET purchasedate = (CURRENT_DATE - INTERVAL '100 days') WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public static List<Map<String,Object>> monthlySales() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT purchases.purchaseDate, products.name, products.description, products.price FROM products INNER JOIN purchases ON products.id = purchases.productId WHERE purchases.purchaseDate > (CURRENT_DATE - INTERVAL '30 days');";
      return con.createQuery(sql).executeAndFetchTable().asList();
    }
  }

  public static List<Map<String,Object>> quarterlySales() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT purchases.purchaseDate, products.name, products.description, products.price FROM products INNER JOIN purchases ON products.id = purchases.productId WHERE purchases.purchaseDate > (CURRENT_DATE - INTERVAL '65 days');";
      return con.createQuery(sql).executeAndFetchTable().asList();
    }
  }

  public static Integer monthlyTotalSales() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT SUM(products.price) FROM products INNER JOIN purchases ON products.id = purchases.productId WHERE purchases.purchaseDate > (CURRENT_DATE - INTERVAL '30 days');";
      return con.createQuery(sql).executeScalar(Integer.class);
    }
  }

  public static Integer quarterlyTotalSales() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT SUM(products.price) FROM products INNER JOIN purchases ON products.id = purchases.productId WHERE purchases.purchaseDate > (CURRENT_DATE - INTERVAL '65 days');";
      return con.createQuery(sql).executeScalar(Integer.class);
    }
  }

}
