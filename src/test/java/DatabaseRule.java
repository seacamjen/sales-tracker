import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/sales_tracker_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteCategoryQuery = "DELETE FROM categories *;";
      con.createQuery(deleteCategoryQuery).executeUpdate();
      String deleteCustomersQuery = "DELETE FROM customers *;";
      String deletePurchasesQuery = "DELETE FROM purchases *;";
      String deleteProductsQuery = "DELETE FROM products *;";
      con.createQuery(deleteCustomersQuery).executeUpdate();
      con.createQuery(deletePurchasesQuery).executeUpdate();
      con.createQuery(deleteProductsQuery).executeUpdate();
    }
  }

}
