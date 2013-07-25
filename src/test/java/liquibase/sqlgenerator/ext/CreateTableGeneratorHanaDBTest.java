package liquibase.sqlgenerator.ext;

import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.database.structure.type.CharType;
import liquibase.database.structure.type.DataType;
import liquibase.database.structure.type.DateTimeType;
import liquibase.database.structure.type.IntType;
import liquibase.database.typeconversion.TypeConverterFactory;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.core.CreateSequenceStatement;
import liquibase.statement.core.CreateTableStatement;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class CreateTableGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<CreateTableStatement> {

    public CreateTableGeneratorHanaDBTest() throws Exception {
        this(new CreateTableGeneratorHanaDB());
    }

    protected CreateTableGeneratorHanaDBTest(SqlGenerator<CreateTableStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected CreateTableStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        CreateTableStatement createTableStatement = new CreateTableStatement(null, "table_name");
//        createSequenceStatement.setMinValue(new BigInteger("100"));
        createTableStatement.addColumn("id",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int unsigned", false));
        return createTableStatement;
    }


    @Test
    public void testWithColumnWithDefaultValue() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("java.sql.Types.TIMESTAMP", false),
                new ColumnConfig().setDefaultValue("null").getDefaultValueObject());

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" TIMESTAMP DEFAULT null)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


//    @Test
    public void testWithColumnSpecificIntType() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int unsigned", false));

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" INTEGER unsigned)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


//    @Test
    public void testCreateMultiColumnPrimary() throws Exception {
//        super.isValid();
        Database database = new HanaDBDatabase();

        CreateTableStatement createTableStatement = new CreateTableStatement(null, "table_name");
//        createTableStatement.addColumn("id", new CharType("VARCHAR"));
//        createTableStatement.addColumn("author", new CharType("VARCHAR"));
//        createTableStatement.addColumn("dateExecuted", new DateTimeType("TIMESTAMP"));
//        createTableStatement.addColumn("description", new CharType("VARCHAR"));
//        createTableStatement.addColumn("revision", new IntType("INT"));
        createTableStatement.addColumn("id",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(150)", false),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("author",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(150)", false),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("dateExecuted",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("java.sql.Types.TIMESTAMP", false),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("description",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(255)", false));
        createTableStatement.addColumn("revision",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int unsigned", false));

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(createTableStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(createTableStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("CREATE TABLE \"table_name\" (\"id\" VARCHAR, \"author\" VARCHAR, " +
                "\"dateExecuted\" TIMESTAMP DEFAULT NULL, \"description\" VARCHAR, \"revision\" INT)",
                generatedSql[0].toSql());
    }

}
