package liquibase.database.ext;

import liquibase.dbtest.AbstractIntegrationTest;
import liquibase.logging.LogFactory;


public class HanaIntegrationTest extends AbstractIntegrationTest {

  public HanaIntegrationTest() throws Exception {
      super("hana", "jdbc:sap://"+ getDatabaseServerHostname("hana") +"/?reconnect=true&autocommit=false");
      LogFactory.setLoggingLevel("debug");
  }

}
