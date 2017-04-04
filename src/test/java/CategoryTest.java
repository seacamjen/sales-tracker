import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class CategoryTest {

  @Rule
  public DatabaseRule databse = new DatabaseRule();

  @Test
  public void Category_instantiatesCorrectly() {
    Category testCategory = new Category("apparel");
    assertTrue(testCategory instanceof Category);
  }

  @Test
  public void getName_returnsCorrectName() {
    Category testCategory = new Category("apparel");
    assertEquals("apparel", testCategory.getName());
  }

  @Test
  public void equals_comparesCategoryBasedOnNameAndEmail() {
    Category testCategory1 = new Category("apparel");
    Category testCategory2 = new Category("apparel");
    assertTrue(testCategory1.equals(testCategory2));
  }

  @Test
  public void save_savesCategoryToDB_true() {
    Category testCategory = new Category("apparel");
    testCategory.save();
    assertTrue(Category.all().get(0).equals(testCategory));
  }

  @Test
  public void all_returnsAllCategorysFromDB() {
    Category testCategory1 = new Category("apparel");
    testCategory1.save();
    Category testCategory2 = new Category("footwear");
    testCategory2.save();
    assertTrue(Category.all().get(0).equals(testCategory1));
    assertTrue(Category.all().get(1).equals(testCategory2));
  }

  @Test
  public void find_returnsCategoryWithGivenId() {
    Category testCategory1 = new Category("apparel");
    testCategory1.save();
    Category testCategory2 = new Category("footwear");
    testCategory2.save();
    assertEquals(testCategory2, Category.find(testCategory2.getId()));
  }

  @Test
  public void save_AssignsIdToCategory_true() {
    Category testCategory = new Category("apparel");
    testCategory.save();
    assertEquals(testCategory.getId(), Category.all().get(0).getId());
  }

  @Test
  public void getId_returnsCategoryId_true() {
    Category testCategory = new Category("apparel");
    testCategory.save();
    assertTrue(testCategory.getId() > 0);
  }



}
