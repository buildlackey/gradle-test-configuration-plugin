import org.testng.annotations.Test
class UnitTest {
  @Test def test(): Unit = {
      System.out.println("in test: " + this.getClass().getName())
      if ( "true" == System.getenv("testShouldFail") ) {
         assert(2> 10)
      } else {
         assert(2> 1)
     }
   }
}

