package liquibase.database.ext;

import liquibase.dbtest.AbstractIntegrationTest;


public class HanaIntegrationTest extends AbstractIntegrationTest {

  public HanaIntegrationTest() throws Exception {
      super("hana", "jdbc:sap://"+ getDatabaseServerHostname("hana") +"/?reconnect=true&autocommit=false");
  }

}
