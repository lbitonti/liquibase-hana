package liquibase.sqlgenerator.ext;

import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.core.CreateTableStatement;
import org.junit.Test;


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
        CreateTableStatement createTableStatement = new CreateTableStatement(null, null, "table_name");
//        createSequenceStatement.setMinValue(new BigInteger("100"));
        createTableStatement.addColumn("id",
                DataTypeFactory.getInstance().fromDescription("int(11) unsigned"));
        return createTableStatement;
    }


    @Test
    public void testWithColumnWithDefaultValue() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("java.sql.Types.TIMESTAMP"),
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("java.sql.Types.TIMESTAMP", false),
                new ColumnConfig().setDefaultValue("null").getDefaultValueObject());

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" timestamp DEFAULT null)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


//    @Test
    public void testWithColumnSpecificIntType() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("int unsigned"));
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int unsigned", false));

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" INTEGER unsigned)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


//    @Test
    public void testCreateMultiColumnPrimary() throws Exception {
//        super.isValid();
        Database database = new HanaDBDatabase();

        CreateTableStatement createTableStatement = new CreateTableStatement(null, null, "table_name");
//        createTableStatement.addColumn("id", new CharType("VARCHAR"));
//        createTableStatement.addColumn("author", new CharType("VARCHAR"));
//        createTableStatement.addColumn("dateExecuted", new DateTimeType("TIMESTAMP"));
//        createTableStatement.addColumn("description", new CharType("VARCHAR"));
//        createTableStatement.addColumn("revision", new IntType("INT"));
        createTableStatement.addColumn("id",
                DataTypeFactory.getInstance().fromDescription("VARCHAR(150)"),
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(150)", false),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("author",
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(150)", false),
                DataTypeFactory.getInstance().fromDescription("VARCHAR(150)"),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("dateExecuted",
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("java.sql.Types.TIMESTAMP", false),
                DataTypeFactory.getInstance().fromDescription("java.sql.Types.TIMESTAMP"),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("description",
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(255)", false));
                DataTypeFactory.getInstance().fromDescription("VARCHAR(255)"));
        createTableStatement.addColumn("revision",
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int unsigned", false));
                DataTypeFactory.getInstance().fromDescription("int unsigned"));

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(createTableStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(createTableStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("CREATE TABLE \"table_name\" (\"id\" VARCHAR, \"author\" VARCHAR, " +
                "\"dateExecuted\" TIMESTAMP DEFAULT NULL, \"description\" VARCHAR, \"revision\" INTEGER)",
                generatedSql[0].toSql());
    }

}
