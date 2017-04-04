import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class CustomerTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void customer_instantiatesCorrectly() {
    Customer testCustomer = new Customer("randy@email.com", "Randy");
    assertTrue(testCustomer instanceof Customer);
  }

  @Test
  public void getEmail_returnsCorrectEmail() {
    Customer testCustomer = new Customer("randy@email.com", "Randy");
    assertEquals("randy@email.com", testCustomer.getEmail());
  }

  @Test
  public void getName_returnsCorrectName() {
    Customer testCustomer = new Customer("randy@email.com", "Randy");
    assertEquals("Randy", testCustomer.getName());
  }

  @Test
  public void equals_comparesCustomerBasedOnNameAndEmail() {
    Customer testCustomer1 = new Customer("randy@email.com", "Randy");
    Customer testCustomer2 = new Customer("randy@email.com", "Randy");
    assertTrue(testCustomer1.equals(testCustomer2));
  }

  @Test
  public void save_savesCustomerToDB_true() {
    Customer testCustomer = new Customer("randy@email.com", "Randy");
    testCustomer.save();
    assertTrue(Customer.all().get(0).equals(testCustomer));
  }

  @Test
  public void all_returnsAllCustomersFromDB() {
    Customer testCustomer1 = new Customer("randy@email.com", "Randy");
    testCustomer1.save();
    Customer testCustomer2 = new Customer("adam@email.com", "Adam");
    testCustomer2.save();
    assertTrue(Customer.all().get(0).equals(testCustomer1));
    assertTrue(Customer.all().get(1).equals(testCustomer2));
  }

  @Test
  public void find_returnsCustomerWithGivenId() {
    Customer testCustomer1 = new Customer("randy@email.com", "Randy");
    testCustomer1.save();
    Customer testCustomer2 = new Customer("adam@email.com", "Adam");
    testCustomer2.save();
    assertEquals(testCustomer2, Customer.find(testCustomer2.getId()));
  }

  @Test
  public void save_AssignsIdToCustomer_true() {
    Customer testCustomer = new Customer("randy@email.com", "Randy");
    testCustomer.save();
    assertEquals(testCustomer.getId(), Customer.all().get(0).getId());
  }

  @Test
  public void getId_returnsCustomerId_true() {
    Customer testCustomer = new Customer("randy@email.com", "Randy");
    testCustomer.save();
    assertTrue(testCustomer.getId() > 0);
  }

  @Test
  public void update_updatesCustomer_true() {
    Customer newCustomer = new Customer("randy@email.com", "Randy");
    newCustomer.save();
    newCustomer.update("fred@email.com", "Fred");
    assertEquals("Fred", Customer.find(newCustomer.getId()).getName());
    assertEquals("fred@email.com", Customer.find(newCustomer.getId()).getEmail());
  }

  @Test
  public void delete_deleteCustomer_true(){
    Customer myCustomer = new Customer("randy@email.com", "Randy");
    myCustomer.save();
    int myCustomerId = myCustomer.getId();
    myCustomer.delete();
    assertEquals(null, Customer.find(myCustomerId));
  }

  @Test
  public void search_returnsCustomersWithMatchingName_true() {
    Customer testCustomer1 = new Customer("randy@email.com", "Randy");
    testCustomer1.save();
    Customer testCustomer2 = new Customer("adam@email.com", "Adam");
    testCustomer2.save();
    Customer testCustomer3 = new Customer("randal@email.com", "Randal");
    testCustomer3.save();
    Customer[] customers = new Customer[] {testCustomer1, testCustomer3};
    assertTrue(Customer.search("rand").containsAll(Arrays.asList(customers)));
    assertFalse(Customer.search("rand").contains(testCustomer2));
  }

  @Test
  public void search_returnsCustomersWithMatchingEmail_true() {
    Customer testCustomer1 = new Customer("randy@email.com", "Randy");
    testCustomer1.save();
    Customer testCustomer2 = new Customer("adam@email.com", "Adam");
    testCustomer2.save();
    Customer testCustomer3 = new Customer("brandy@email.com", "Brandy");
    testCustomer3.save();
    Customer[] customers = new Customer[] {testCustomer1, testCustomer3};
    assertTrue(Customer.search("ndy@em").containsAll(Arrays.asList(customers)));
    assertFalse(Customer.search("ndy@em").contains(testCustomer2));
  }

  @Test
  public void purchase_customerCanPurchaseAProducr_true() {
    Customer newCustomer = new Customer("randy@email.com", "Randy");
    newCustomer.save();
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct.save();
    newCustomer.purchase(testProduct.getId());
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(newCustomer.getId(), Purchase.all().get(0).getCustomerId());
    assertEquals(testProduct.getId(), Purchase.all().get(0).getProductId());
    assertEquals(rightNow.getDay(), Purchase.all().get(0).getPurchaseDate().getDay());
  }

  @Test
  public void getHistory_returnsAllProductsPurchased_List() {
    Customer newCustomer = new Customer("randy@email.com", "Randy");
    newCustomer.save();
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball", 2);
    testProduct2.save();
    newCustomer.purchase(testProduct1.getId());
    newCustomer.purchase(testProduct2.getId());
    Timestamp rightNow = new Timestamp(new Date().getTime());
    Timestamp purchase1Date = (Timestamp) newCustomer.getHistory().get(0).get("purchasedate");
    Timestamp purchase2Date = (Timestamp) newCustomer.getHistory().get(1).get("purchasedate");
    assertEquals("Jersey", newCustomer.getHistory().get(0).get("name"));
    assertEquals(10, newCustomer.getHistory().get(0).get("price"));
    assertEquals(rightNow.getDay(), purchase1Date.getDay());
    assertEquals("Ball", newCustomer.getHistory().get(1).get("name"));
    assertEquals(20, newCustomer.getHistory().get(1).get("price"));
    assertEquals(rightNow.getDay(), purchase2Date.getDay());
  }

  @Test
  public void getAdmin_returnsFalseForCustomer() {
    Customer newCustomer = new Customer("randy@email.com", "Randy");
    assertFalse(newCustomer.getAdmin());
  }

  @Test
  public void setAdmin_makesCustomerAnAdmin_true() {
    Customer newCustomer = new Customer("randy@email.com", "Randy");
    newCustomer.save();
    assertFalse(newCustomer.getAdmin());
    assertFalse(Customer.find(newCustomer.getId()).getAdmin());
    newCustomer.setAdmin();
    assertTrue(newCustomer.getAdmin());
    assertTrue(Customer.find(newCustomer.getId()).getAdmin());
  }

}
