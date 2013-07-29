package liquibase.sqlgenerator.ext;

import liquibase.database.ext.HanaDBDatabase;
import liquibase.statement.core.AddPrimaryKeyStatement;
import liquibase.statement.core.AddUniqueConstraintStatement;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class AddUniqueConstraintGeneratorHanaDBTest {

    @Test
    public void testIsNotSupported() throws Exception {
        AddUniqueConstraintGeneratorHanaDB generatorUnderTest = new AddUniqueConstraintGeneratorHanaDB();

        AddUniqueConstraintStatement addUniqueConstraintStatement =
                new AddUniqueConstraintStatement(null, null, "table_name", "column_name", "unique_constraint");

        assertFalse(generatorUnderTest.supports(addUniqueConstraintStatement, new HanaDBDatabase()));
    }

}
