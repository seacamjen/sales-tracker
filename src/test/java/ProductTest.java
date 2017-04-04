import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ProductTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void products_instantiatesCorrectly() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    assertTrue(testProduct instanceof Product);
  }

  @Test
  public void getName_returnsCorrectName() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    assertEquals("Jersey", testProduct.getName());
  }

  @Test
  public void getPrice_returnsCorrectPrice() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    assertEquals(10, testProduct.getPrice());
  }
  @Test
  public void getDescription_returnsCorrectDescription() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    assertEquals("Awesome Jersey", testProduct.getDescription());
  }

  @Test
  public void equals_comparesProductBasedOnNameAndEmail() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey", 1);
    Product testProduct2 = new Product("Jersey", 10, "Awesome Jersey", 1);
    assertTrue(testProduct1.equals(testProduct2));
  }

  @Test
  public void save_savesProductToDB_true() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct.save();
    assertTrue(Product.all().get(0).equals(testProduct));
  }

  @Test
  public void all_returnsAllProductsFromDB() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball", 2);
    testProduct2.save();
    assertTrue(Product.all().get(0).equals(testProduct1));
    assertTrue(Product.all().get(1).equals(testProduct2));
  }

  @Test
  public void find_returnsProductWithGivenId() {
    Product testProduct1 = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct1.save();
    Product testProduct2 = new Product("Ball", 20, "Awesome Ball", 2);
    testProduct2.save();
    assertEquals(testProduct2, Product.find(testProduct2.getId()));
  }

  @Test
  public void save_AssignsIdToProduct_true() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct.save();
    assertEquals(testProduct.getId(), Product.all().get(0).getId());
  }

  @Test
  public void getId_returnsProductId_true() {
    Product testProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    testProduct.save();
    assertTrue(testProduct.getId() > 0);
  }

  @Test
  public void update_updatesProduct_true() {
    Product newProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    newProduct.save();
    newProduct.update("Shirt", 12, "Cool Jersey", 1);
    assertEquals("Shirt", Product.find(newProduct.getId()).getName());
    assertEquals(12, Product.find(newProduct.getId()).getPrice());
    assertEquals("Cool Jersey", Product.find(newProduct.getId()).getDescription());
  }

  @Test
  public void delete_deleteProduct_true(){
    Product myProduct = new Product("Jersey", 10, "Awesome Jersey", 1);
    myProduct.save();
    int myProductId = myProduct.getId();
    myProduct.delete();
    assertEquals(null, Product.find(myProductId));
  }

  @Test
  public void search_returnsProductsWithMatchingName_true() {
    Product testProduct1 = new Product("Shoe", 10, "Awesome Shoe", 3);
    testProduct1.save();
    Product testProduct2 = new Product("Jersey", 20, "Awesome Jersey", 1);
    testProduct2.save();
    Product testProduct3 = new Product("Shirt", 12, "Cool Jersey", 1);
    testProduct3.save();
    Product[] products = new Product[] {testProduct1, testProduct3};
    assertTrue(Product.search("sh").containsAll(Arrays.asList(products)));
    assertFalse(Product.search("sh").contains(testProduct2));
  }

  @Test
  public void search_returnsProductsWithMatchingDescription_true() {
    Product testProduct1 = new Product("Shoe", 10, "Awesome Shoe", 3);
    testProduct1.save();
    Product testProduct2 = new Product("Jersey", 20, "Cool Jersey", 1);
    testProduct2.save();
    Product testProduct3 = new Product("Shirt", 12, "Awesome Jersey", 1);
    testProduct3.save();
    Product[] products = new Product[] {testProduct1, testProduct3};
    assertTrue(Product.search("awesome").containsAll(Arrays.asList(products)));
    assertFalse(Product.search("awesome").contains(testProduct2));
  }

  @Test
  public void product_instantiatesWithCategoryId() {
    Category testCategory = new Category("apparel");
    testCategory.save();
    Product newProduct = new Product("Jersey", 10, "Awesome Jersey", testCategory.getId());
    newProduct.save();
    assertEquals(testCategory.getId(), Product.find(newProduct.getId()).getCategoryId());
  }

  @Test
  public void getProducts_returnsAllProductsInCategory() {
    Category testCategory = new Category("apparel");
    testCategory.save();
    Product testProduct1 = new Product("Shoe", 10, "Awesome Shoe", testCategory.getId());
    testProduct1.save();
    Product testProduct2 = new Product("Jersey", 20, "Cool Jersey", testCategory.getId());
    testProduct2.save();
    Product testProduct3 = new Product("Shirt", 12, "Awesome Jersey", 40);
    testProduct3.save();
    Product[] products = new Product[] {testProduct1, testProduct2};
    assertTrue(Product.getCategoryProducts(testCategory.getId()).containsAll(Arrays.asList(products)));
    assertFalse(Product.getCategoryProducts(testCategory.getId()).contains(testProduct3));
  }


}
