import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ProductTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void products_instantiatesCorrectly() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey");
    assertTrue(testProduct instanceof Product);
  }

  @Test
  public void getName_returnsCorrectName() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey");
    assertEquals("Jersey", testProduct.getName());
  }

  @Test
  public void getPrice_returnsCorrectPrice() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey");
    assertEquals(10, testProduct.getPrice());
  }
  @Test
  public void getDescription_returnsCorrectDescription() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey");
    assertEquals("Awesome Jersey", testProduct.getDescription());
  }

  @Test
  public void equals_comparesProductBasedOnNameAndEmail() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey");
    Product testProduct2 = new Product("Jersey", 10, "Awesome Jersey");
    assertTrue(testProduct1.equals(testProduct2));
  }

  @Test
  public void save_savesProductToDB_true() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey");
    testProduct.save();
    assertTrue(Product.all().get(0).equals(testProduct));
  }

  @Test
  public void all_returnsAllProductsFromDB() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey");
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball");
    testProduct2.save();
    assertTrue(Product.all().get(0).equals(testProduct1));
    assertTrue(Product.all().get(1).equals(testProduct2));
  }

  @Test
  public void find_returnsProductWithGivenId() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey");
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball");
    testProduct2.save();
    assertEquals(testProduct2, Product.find(testProduct2.getId()));
  }

  @Test
  public void save_AssignsIdToProduct_true() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey");
    testProduct.save();
    assertEquals(testProduct.getId(), Product.all().get(0).getId());
  }

  @Test
  public void getId_returnsProductId_true() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey");
    testProduct.save();
    assertTrue(testProduct.getId() > 0);
  }

  @Test
  public void update_updatesProduct_true() {
    Product newProduct = new Product("Jersey", 10, "Awesome Jersey");
    newProduct.save();
    newProduct.update("Shirt", 12, "Cool Jersey");
    assertEquals("Shirt", Product.find(newProduct.getId()).getName());
    assertEquals(12, Product.find(newProduct.getId()).getPrice());
    assertEquals("Cool Jersey", Product.find(newProduct.getId()).getDescription());
  }

  @Test
  public void delete_deleteProduct_true(){
    Product myProduct = new Product("Jersey", 10, "Awesome Jersey");
    myProduct.save();
    int myProductId = myProduct.getId();
    myProduct.delete();
    assertEquals(null, Product.find(myProductId));
  }

  @Test
  public void search_returnsProductsWithMatchingName_true() {
    Product testProduct1 = new Product("Shoe", 10, "Awesome Shoe");
    testProduct1.save();
    Product testProduct2 = new Product("Jersey", 20, "Awesome Jersey");
    testProduct2.save();
    Product testProduct3 = new Product("Shirt", 12, "Cool Jersey");
    testProduct3.save();
    Product[] products = new Product[] {testProduct1, testProduct3};
    assertTrue(Product.search("sh").containsAll(Arrays.asList(products)));
    assertFalse(Product.search("sh").contains(testProduct2));
  }

  @Test
  public void search_returnsProductsWithMatchingDescription_true() {
    Product testProduct1 = new Product("Shoe", 10, "Awesome Shoe");
    testProduct1.save();
    Product testProduct2 = new Product("Jersey", 20, "Cool Jersey");
    testProduct2.save();
    Product testProduct3 = new Product("Shirt", 12, "Awesome Jersey");
    testProduct3.save();
    Product[] products = new Product[] {testProduct1, testProduct3};
    assertTrue(Product.search("awesome").containsAll(Arrays.asList(products)));
    assertFalse(Product.search("awesome").contains(testProduct2));
  }


}
