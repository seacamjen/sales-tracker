import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;

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

}
