package liquibase.sqlgenerator.ext;

import liquibase.database.ext.HanaDBDatabase;
import liquibase.statement.core.AddAutoIncrementStatement;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;


public class AddAutoIncrementGeneratorHanaDBTest {

    @Test
    public void testIsNotSupported() throws Exception {
        AddAutoIncrementGeneratorHanaDB generatorUnderTest = new AddAutoIncrementGeneratorHanaDB();

        AddAutoIncrementStatement addAutoIncrementStatement =
                new AddAutoIncrementStatement(null, "table_name", "column_name", "column_type", new BigInteger("1"), new BigInteger("1"));

        assertFalse(generatorUnderTest.supports(addAutoIncrementStatement, new HanaDBDatabase()));
    }

}
