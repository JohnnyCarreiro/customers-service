package com.johnnycarreiro.crs.modules.customer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootTest
class CrsApplicationTests {

  @Test
  void contextLoads()  {
    System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");
//    Assertions.assertNotNull(new Main());
//    Main.main(new String[]{});
  }

}
