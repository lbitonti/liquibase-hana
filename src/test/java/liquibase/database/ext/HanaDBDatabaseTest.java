package liquibase.database.ext;

import liquibase.database.AbstractDatabaseTest;
import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Tests for {@link liquibase.database.ext.HanaDBDatabase}
 */
public class HanaDBDatabaseTest extends AbstractDatabaseTest {

    public HanaDBDatabaseTest() throws Exception {
        super(new HanaDBDatabase());
    }

    @Override
    protected String getProductNameString() {
      return "HDB";
    }

    @Override
    @Test
    public void supportsInitiallyDeferrableColumns() {
        assertFalse(getDatabase().supportsInitiallyDeferrableColumns());
    }



    @Override
    @Test
    public void getCurrentDateTimeFunction() {
        Assert.assertEquals("CURRENT_TIMESTAMP", getDatabase().getCurrentDateTimeFunction());
    }

    public void testGetDefaultDriver() {
        Database database = new MySQLDatabase();

        assertEquals("com.sap.db.jdbc.Driver", database.getDefaultDriver("jdbc:sap://localhost/liquibase"));

        assertNull(database.getDefaultDriver("jdbc:db2://localhost;databaseName=liquibase"));
    }

    @Override
    @Test
    public void escapeTableName_noSchema() {
        Database database = getDatabase();
        assertEquals("\"tableName\"", database.escapeTableName(null, "tableName"));
    }

    @Override
    @Test
    public void escapeTableName_withSchema() {
        Database database = getDatabase();
        assertEquals("\"schemaName\".\"tableName\"", database.escapeTableName("schemaName", "tableName"));
    }

}
