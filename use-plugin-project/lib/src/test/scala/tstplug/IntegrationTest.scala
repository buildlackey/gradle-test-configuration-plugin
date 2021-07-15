
import org.testng.annotations.Test
class IntegrationTest {
  @Test(groups = Array("integrationTest")) def test(): Unit = {
      System.out.println("in test: " + this.getClass().getName())
      if ( "true" == System.getenv("integrationTestShouldFail") ) {
         assert(2> 10)
      } else {
         assert(2> 1)
     }
   }
}

