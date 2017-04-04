import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;


public class PurchaseTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void purchase_instantiatesCorrectly() {
    Purchase testPurchase = new Purchase(1, 1);
    assertTrue(testPurchase instanceof Purchase);
  }

  @Test
  public void getCustomerId_returnsCorrectCustomerId() {
    Purchase testPurchase = new Purchase(1, 1);
    assertEquals(1, testPurchase.getCustomerId());
  }

  @Test
  public void getProductId_returnsCorrectProductId() {
    Purchase testPurchase = new Purchase(1, 1);
    assertEquals(1, testPurchase.getProductId());
  }


  @Test
  public void equals_comparesPurchaseBasedOnNameAndEmail() {
    Purchase testPurchase1 = new Purchase(1, 1);
    Purchase testPurchase2 = new Purchase(1, 1);
    assertTrue(testPurchase1.equals(testPurchase2));
  }

  @Test
  public void save_savesPurchaseToDB_true() {
    Purchase testPurchase = new Purchase(1, 1);
    testPurchase.save();
    assertTrue(Purchase.all().get(0).equals(testPurchase));
  }

  @Test
  public void all_returnsAllPurchasesFromDB() {
    Purchase testPurchase1 = new Purchase(1, 1);
    testPurchase1.save();
    Purchase testPurchase2 = new Purchase(1, 2);
    testPurchase2.save();
    assertTrue(Purchase.all().get(0).equals(testPurchase1));
    assertTrue(Purchase.all().get(1).equals(testPurchase2));
  }

  @Test
  public void find_returnsPurchaseWithGivenId() {
    Purchase testPurchase1 = new Purchase(1, 1);
    testPurchase1.save();
    Purchase testPurchase2 = new Purchase(1, 2);
    testPurchase2.save();
    assertEquals(testPurchase2, Purchase.find(testPurchase2.getId()));
  }

  @Test
  public void save_AssignsIdToPurchase_true() {
    Purchase testPurchase = new Purchase(1, 1);
    testPurchase.save();
    assertEquals(testPurchase.getId(), Purchase.all().get(0).getId());
  }

  @Test
  public void getId_returnsPurchaseId_true() {
    Purchase testPurchase = new Purchase(1, 1);
    testPurchase.save();
    assertTrue(testPurchase.getId() > 0);
  }

  @Test
  public void save_recordsTimeOfCreationInDatabase() {
    Purchase testPurchase = new Purchase(1, 1);
    testPurchase.save();
    Timestamp savedPurchaseDate = Purchase.find(testPurchase.getId()).getPurchaseDate();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    System.out.println(rightNow);
    System.out.println(savedPurchaseDate);
    assertEquals(rightNow.getDay(), savedPurchaseDate.getDay());
  }

  @Test
  public void update_updatesPurchase_true() {
    Purchase newPurchase = new Purchase(1, 1);
    newPurchase.save();
    newPurchase.update(2, 2);
    assertEquals(2, Purchase.find(newPurchase.getId()).getCustomerId());
    assertEquals(2, Purchase.find(newPurchase.getId()).getProductId());
  }

  @Test
  public void delete_deletePurchase_true(){
    Purchase myPurchase = new Purchase(1, 1);
    myPurchase.save();
    int myPurchaseId = myPurchase.getId();
    myPurchase.delete();
    assertEquals(null, Purchase.find(myPurchaseId));
  }

  @Test
  public void setPurchaseDate_setsPurchaseDateToGivenDate() {
    Purchase myPurchase = new Purchase(1, 1);
    myPurchase.save();
    Timestamp newDate = Timestamp.valueOf(LocalDateTime.now().minusDays(100));
    myPurchase.setPurchaseDate();
    assertEquals(newDate.getDay(), Purchase.find(myPurchase.getId()).getPurchaseDate().getDay());
  }

  @Test
  public void monthlySales_returnsListOfAllSalesForLast30Days() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball", 2);
    testProduct2.save();
    Purchase testPurchase1 = new Purchase(1, testProduct1.getId());
    testPurchase1.save();
    Purchase testPurchase2 = new Purchase(1, testProduct2.getId());
    testPurchase2.save();
    Purchase testPurchase3 = new Purchase(2, testProduct2.getId());
    testPurchase3.save();
    testPurchase3.setPurchaseDate();

    Timestamp rightNow = new Timestamp(new Date().getTime());
    Timestamp purchase1Date = (Timestamp) Purchase.monthlySales().get(0).get("purchasedate");
    Timestamp purchase2Date = (Timestamp) Purchase.monthlySales().get(1).get("purchasedate");

    Map<String,Object> purchase3Map = new HashMap<String,Object>();
    purchase3Map.put("purchasedate", testPurchase3.getPurchaseDate());
    purchase3Map.put("name", "Ball");
    purchase3Map.put("price", 20);
    purchase3Map.put("description", "Awesome Ball");

    assertEquals(rightNow.getDay(), purchase1Date.getDay());
    assertEquals("Jersey", Purchase.monthlySales().get(0).get("name"));
    assertEquals("Awesome Jersey", Purchase.monthlySales().get(0).get("description"));
    assertEquals(10, Purchase.monthlySales().get(0).get("price"));
    assertEquals(rightNow.getDay(), purchase2Date.getDay());
    assertEquals("Ball", Purchase.monthlySales().get(1).get("name"));
    assertEquals("Awesome Ball", Purchase.monthlySales().get(1).get("description"));
    assertEquals(20, Purchase.monthlySales().get(1).get("price"));
    assertFalse(Purchase.monthlySales().contains(purchase3Map));
  }

  @Test
  public void quarterlySales_returnsListOfAllSalesForLast30Days() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball", 2);
    testProduct2.save();
    Purchase testPurchase1 = new Purchase(1, testProduct1.getId());
    testPurchase1.save();
    Purchase testPurchase2 = new Purchase(1, testProduct2.getId());
    testPurchase2.save();
    Purchase testPurchase3 = new Purchase(2, testProduct2.getId());
    testPurchase3.save();
    testPurchase3.setPurchaseDate();

    Timestamp rightNow = new Timestamp(new Date().getTime());
    Timestamp purchase1Date = (Timestamp) Purchase.quarterlySales().get(0).get("purchasedate");
    Timestamp purchase2Date = (Timestamp) Purchase.quarterlySales().get(1).get("purchasedate");

    Map<String,Object> purchase3Map = new HashMap<String,Object>();
    purchase3Map.put("purchasedate", testPurchase3.getPurchaseDate());
    purchase3Map.put("name", "Ball");
    purchase3Map.put("price", 20);
    purchase3Map.put("description", "Awesome Ball");

    assertEquals(rightNow.getDay(), purchase1Date.getDay());
    assertEquals("Jersey", Purchase.quarterlySales().get(0).get("name"));
    assertEquals("Awesome Jersey", Purchase.quarterlySales().get(0).get("description"));
    assertEquals(10, Purchase.quarterlySales().get(0).get("price"));
    assertEquals(rightNow.getDay(), purchase2Date.getDay());
    assertEquals("Ball", Purchase.quarterlySales().get(1).get("name"));
    assertEquals("Awesome Ball", Purchase.quarterlySales().get(1).get("description"));
    assertEquals(20, Purchase.quarterlySales().get(1).get("price"));
    assertFalse(Purchase.quarterlySales().contains(purchase3Map));
  }

  @Test
  public void monthlyTotalSales_returnsListOfAllSalesForLast30Days() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball", 2);
    testProduct2.save();
    Purchase testPurchase1 = new Purchase(1, testProduct1.getId());
    testPurchase1.save();
    Purchase testPurchase2 = new Purchase(1, testProduct2.getId());
    testPurchase2.save();
    Purchase testPurchase3 = new Purchase(2, testProduct2.getId());
    testPurchase3.save();
    testPurchase3.setPurchaseDate();

    assertEquals((Integer) 30, Purchase.monthlyTotalSales());
  }

  @Test
  public void quarterlyTotalSales_returnsListOfAllSalesForLast30Days() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball", 2);
    testProduct2.save();
    Purchase testPurchase1 = new Purchase(1, testProduct1.getId());
    testPurchase1.save();
    Purchase testPurchase2 = new Purchase(1, testProduct2.getId());
    testPurchase2.save();
    Purchase testPurchase3 = new Purchase(2, testProduct2.getId());
    testPurchase3.save();
    testPurchase3.setPurchaseDate();

    assertEquals((Integer) 30, Purchase.quarterlyTotalSales());
  }

}
