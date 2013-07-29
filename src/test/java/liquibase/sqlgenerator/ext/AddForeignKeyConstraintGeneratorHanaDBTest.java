package liquibase.sqlgenerator.ext;

import liquibase.database.ext.HanaDBDatabase;
import liquibase.statement.core.AddAutoIncrementStatement;
import liquibase.statement.core.AddForeignKeyConstraintStatement;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertFalse;


public class AddForeignKeyConstraintGeneratorHanaDBTest {

    @Test
    public void testIsNotSupported() throws Exception {
        AddForeignKeyConstraintGeneratorHanaDB generatorUnderTest = new AddForeignKeyConstraintGeneratorHanaDB();

        AddForeignKeyConstraintStatement addForeignKeyConstraintStatement =
                new AddForeignKeyConstraintStatement("constraint_name", "base_catalog_name", "base_schema_name", "base_table_name",
                        "base_column_name", "ref_catalog_name", "ref_schema_name", "ref_table_name", "ref_column_name");

        assertFalse(generatorUnderTest.supports(addForeignKeyConstraintStatement, new HanaDBDatabase()));
    }

}
