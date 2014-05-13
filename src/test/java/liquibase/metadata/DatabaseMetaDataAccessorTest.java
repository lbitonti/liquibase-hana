package liquibase.metadata;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Set;

import liquibase.database.jvm.JdbcConnection;

import org.junit.Before;
import org.junit.Test;

public class DatabaseMetaDataAccessorTest {

    private static final String TEST_CATALOG = "MYCAT";
    private static final String TEST_SCHEMA = "MYSCHEMA";
    private static final String TEST_TABLE = "MYTABLE";
    private static final int DEFAULT_SIZE = 10;

    private JdbcConnection mockConnection;

    @Before
    public void setup() throws Exception {
        ResultSet colInteger = createNiceMock(ResultSet.class);
        expect(colInteger.next()).andReturn(true).once();
        expect(colInteger.next()).andReturn(false).once();
        expect(colInteger.getInt(eq("DATA_TYPE"))).andReturn(Types.INTEGER);
        expect(colInteger.getString(eq("TYPE_NAME"))).andReturn("INTEGER");
        expect(colInteger.getInt(eq("COLUMN_SIZE"))).andReturn(DEFAULT_SIZE);
        replay(colInteger);

        ResultSet colVarchar = createNiceMock(ResultSet.class);
        expect(colVarchar.next()).andReturn(true).once();
        expect(colVarchar.next()).andReturn(false).once();
        expect(colVarchar.getInt(eq("DATA_TYPE"))).andReturn(Types.VARCHAR);
        expect(colVarchar.getString(eq("TYPE_NAME"))).andReturn("VARCHAR");
        expect(colVarchar.getInt(eq("COLUMN_SIZE"))).andReturn(DEFAULT_SIZE);
        replay(colVarchar);

        ResultSet colNvarchar = createNiceMock(ResultSet.class);
        expect(colNvarchar.next()).andReturn(true).once();
        expect(colNvarchar.next()).andReturn(false).once();
        expect(colNvarchar.getInt(eq("DATA_TYPE"))).andReturn(Types.NVARCHAR);
        expect(colNvarchar.getString(eq("TYPE_NAME"))).andReturn("NVARCHAR");
        expect(colNvarchar.getInt(eq("COLUMN_SIZE"))).andReturn(DEFAULT_SIZE);
        replay(colNvarchar);

        ResultSet colVarbinary = createNiceMock(ResultSet.class);
        expect(colVarbinary.next()).andReturn(true).once();
        expect(colVarbinary.next()).andReturn(false).once();
        expect(colVarbinary.getInt(eq("DATA_TYPE"))).andReturn(Types.VARBINARY);
        expect(colVarbinary.getString(eq("TYPE_NAME"))).andReturn("VARBINARY");
        expect(colVarbinary.getInt(eq("COLUMN_SIZE"))).andReturn(DEFAULT_SIZE);
        replay(colVarbinary);

        ResultSet colClob = createNiceMock(ResultSet.class);
        expect(colClob.next()).andReturn(true).once();
        expect(colClob.next()).andReturn(false).once();
        expect(colClob.getInt(eq("DATA_TYPE"))).andReturn(Types.CLOB);
        expect(colClob.getString(eq("TYPE_NAME"))).andReturn("CLOB");
        expect(colClob.getInt(eq("COLUMN_SIZE"))).andReturn(DEFAULT_SIZE);
        replay(colClob);
        
        ResultSet impKeys = createNiceMock(ResultSet.class);
        expect(impKeys.next()).andReturn(true).times(3);
        expect(impKeys.next()).andReturn(false).once();
        // foreign key constraint FK_MYTABLE on column COL1
        expect(impKeys.getShort(eq("KEY_SEQ"))).andReturn((short) 1).once();
        expect(impKeys.getString(eq("FK_NAME"))).andReturn("FK_MYTABLE").once();
        expect(impKeys.getString(eq("PK_NAME"))).andReturn("PK_OTHERTABLE").once();
        expect(impKeys.getString(eq("FKTABLE_CAT"))).andReturn(TEST_CATALOG).once();
        expect(impKeys.getString(eq("FKTABLE_SCHEM"))).andReturn(TEST_SCHEMA).once();
        expect(impKeys.getString(eq("FKTABLE_NAME"))).andReturn(TEST_TABLE).once();
        expect(impKeys.getString(eq("FKCOLUMN_NAME"))).andReturn("COL1").once();
        expect(impKeys.getString(eq("PKTABLE_CAT"))).andReturn("OTHERCAT").once();
        expect(impKeys.getString(eq("PKTABLE_SCHEM"))).andReturn("OTHERSCHEMA").once();
        expect(impKeys.getString(eq("PKTABLE_NAME"))).andReturn("OTHERTABLE").once();
        expect(impKeys.getString(eq("PKCOLUMN_NAME"))).andReturn("PKCOL1").once();
        expect(impKeys.getShort(eq("UPDATE_RULE"))).andReturn((short) DatabaseMetaData.importedKeyRestrict).once();
        expect(impKeys.getShort(eq("DELETE_RULE"))).andReturn((short) DatabaseMetaData.importedKeyCascade).once();
        expect(impKeys.getShort(eq("DEFERRABILITY"))).andReturn((short) DatabaseMetaData.importedKeyNotDeferrable)
                .once();
        // foreign key constraint FK_MYTABLE_TWOCOLS on columns COL2, COL3
        expect(impKeys.getShort(eq("KEY_SEQ"))).andReturn((short) 1).once();
        expect(impKeys.getShort(eq("KEY_SEQ"))).andReturn((short) 2).once();
        expect(impKeys.getString(eq("FK_NAME"))).andReturn("FK_MYTABLE2").once();
        expect(impKeys.getString(eq("PK_NAME"))).andReturn("PK_OTHERTABLE2").once();
        expect(impKeys.getString(eq("FKTABLE_CAT"))).andReturn(TEST_CATALOG).once();
        expect(impKeys.getString(eq("FKTABLE_SCHEM"))).andReturn(TEST_SCHEMA).once();
        expect(impKeys.getString(eq("FKTABLE_NAME"))).andReturn(TEST_TABLE).once();
        expect(impKeys.getString(eq("FKCOLUMN_NAME"))).andReturn("COL2").once();
        expect(impKeys.getString(eq("FKCOLUMN_NAME"))).andReturn("COL3").once();
        expect(impKeys.getString(eq("PKTABLE_CAT"))).andReturn("OTHERCAT").once();
        expect(impKeys.getString(eq("PKTABLE_SCHEM"))).andReturn("OTHERSCHEMA").once();
        expect(impKeys.getString(eq("PKTABLE_NAME"))).andReturn("OTHERTABLE2").once();
        expect(impKeys.getString(eq("PKCOLUMN_NAME"))).andReturn("PKCOL1").once();
        expect(impKeys.getString(eq("PKCOLUMN_NAME"))).andReturn("PKCOL2").once();
        expect(impKeys.getShort(eq("UPDATE_RULE"))).andReturn((short) DatabaseMetaData.importedKeyNoAction).once();
        expect(impKeys.getShort(eq("DELETE_RULE"))).andReturn((short) DatabaseMetaData.importedKeySetNull).once();
        expect(impKeys.getShort(eq("DEFERRABILITY"))).andReturn((short) DatabaseMetaData.importedKeyInitiallyDeferred)
                .once();
        replay(impKeys);

        ResultSet expKeys = createNiceMock(ResultSet.class);
        expect(expKeys.next()).andReturn(true).once();
        expect(expKeys.next()).andReturn(false).once();
        // foreign key constraint FK_SOMETABLE referencing MYTABLE
        expect(expKeys.getShort(eq("KEY_SEQ"))).andReturn((short) 1).once();
        expect(expKeys.getString(eq("FK_NAME"))).andReturn("FK_SOMETABLE").once();
        expect(expKeys.getString(eq("PK_NAME"))).andReturn("PK_MYTABLE").once();
        expect(expKeys.getString(eq("FKTABLE_CAT"))).andReturn("SOMECAT").once();
        expect(expKeys.getString(eq("FKTABLE_SCHEM"))).andReturn("SOMESCHEMA").once();
        expect(expKeys.getString(eq("FKTABLE_NAME"))).andReturn("SOMETABLE").once();
        expect(expKeys.getString(eq("FKCOLUMN_NAME"))).andReturn("COL1").once();
        expect(expKeys.getString(eq("PKTABLE_CAT"))).andReturn(TEST_CATALOG).once();
        expect(expKeys.getString(eq("PKTABLE_SCHEM"))).andReturn(TEST_SCHEMA).once();
        expect(expKeys.getString(eq("PKTABLE_NAME"))).andReturn(TEST_TABLE).once();
        expect(expKeys.getString(eq("PKCOLUMN_NAME"))).andReturn("PKCOL1").once();
        expect(expKeys.getShort(eq("UPDATE_RULE"))).andReturn((short) DatabaseMetaData.importedKeyRestrict).once();
        expect(expKeys.getShort(eq("DELETE_RULE"))).andReturn((short) DatabaseMetaData.importedKeySetDefault).once();
        expect(expKeys.getShort(eq("DEFERRABILITY"))).andReturn((short) DatabaseMetaData.importedKeyInitiallyImmediate)
                .once();
        replay(expKeys);

        DatabaseMetaData md = createNiceMock(DatabaseMetaData.class);
        expect(md.getColumns(eq(TEST_CATALOG), eq(TEST_SCHEMA), eq(TEST_TABLE), eq("COL_INTEGER"))).andReturn(colInteger);
        expect(md.getColumns(eq(TEST_CATALOG), eq(TEST_SCHEMA), eq(TEST_TABLE), eq("COL_VARCHAR"))).andReturn(colVarchar);
        expect(md.getColumns(eq(TEST_CATALOG), eq(TEST_SCHEMA), eq(TEST_TABLE), eq("COL_NVARCHAR"))).andReturn(colNvarchar);
        expect(md.getColumns(eq(TEST_CATALOG), eq(TEST_SCHEMA), eq(TEST_TABLE), eq("COL_VARBINARY"))).andReturn(colVarbinary);
        expect(md.getColumns(eq(TEST_CATALOG), eq(TEST_SCHEMA), eq(TEST_TABLE), eq("COL_CLOB"))).andReturn(colClob);
        expect(md.getImportedKeys(eq(TEST_CATALOG), eq(TEST_SCHEMA), eq(TEST_TABLE))).andReturn(impKeys);
        expect(md.getExportedKeys(eq(TEST_CATALOG), eq(TEST_SCHEMA), eq(TEST_TABLE))).andReturn(expKeys);
        replay(md);

        mockConnection = createNiceMock(JdbcConnection.class);
        expect(mockConnection.getMetaData()).andReturn(md).anyTimes();
        replay(mockConnection);
    }

    @Test
    public void testGetColumnMetaData() throws Exception {
        String[] testColumns = new String[] {"COL_INTEGER", "COL_VARCHAR", "COL_NVARCHAR", "COL_VARBINARY", "COL_CLOB"};
        for (String column : testColumns) {
            ColumnMetaData columnData = DatabaseMetaDataAccessor.getColumnMetaData(mockConnection, TEST_CATALOG, TEST_SCHEMA, 
                    TEST_TABLE, column);
            checkColumnMetaData(columnData);
        }
        
    }

    private void checkColumnMetaData(ColumnMetaData columnData) {
        assertNotNull(columnData);
        assertEquals(TEST_CATALOG, columnData.getCatalog());
        assertEquals(TEST_SCHEMA, columnData.getSchema());
        assertEquals(TEST_TABLE, columnData.getTable());
        String columnName = columnData.getColumn();
        assertNotNull(columnName);
        int i = columnName.indexOf("COL_");
        assertTrue(i == 0);
        String expectedType = columnName.substring(i + 4);
        assertEquals(expectedType, columnData.getTypeName());
        assertEquals(mapTypeNameToSqlType(expectedType), columnData.getSqlType());
        if (requiresSize(columnData.getSqlType())) {
            assertTrue(columnData.requiresSizeForDefinition());
        } else {
            assertFalse(columnData.requiresSizeForDefinition());
        }
    }
    
    private int mapTypeNameToSqlType(String typeName) {
        if ("INTEGER".equals(typeName)) {
            return Types.INTEGER;
        }
        if ("VARCHAR".equals(typeName)) {
            return Types.VARCHAR;
        }
        if ("NVARCHAR".equals(typeName)) {
            return Types.NVARCHAR;
        }
        if ("VARBINARY".equals(typeName)) {
            return Types.VARBINARY;
        }
        if ("CLOB".equals(typeName)) {
            return Types.CLOB;
        }
        throw new AssertionError("Unexpected type name: " + typeName);
    }
    
    private boolean requiresSize(int sqlType) {
        if (sqlType == Types.VARCHAR || sqlType == Types.NVARCHAR || sqlType == Types.VARBINARY) {
            return true;
        }
        return false;
    }
    
    @Test
    public void testGetForeignKeyConstraintsForTable() throws Exception {
        Set<ForeignKeyConstraintMetaData> constraints = DatabaseMetaDataAccessor.getForeignKeyConstraintsForTable(mockConnection,
                TEST_CATALOG, TEST_SCHEMA, TEST_TABLE);
        assertTrue(constraints.size() == 2);
        
        ForeignKeyConstraintMetaData constraint = findConstraint(constraints, "FK_MYTABLE");
        assertNotNull(constraint);
        assertEquals("PK_OTHERTABLE", constraint.getPrimaryKeyName());
        assertEquals(TEST_CATALOG, constraint.getForeignKeyCatalog());
        assertEquals(TEST_SCHEMA, constraint.getForeignKeySchema());
        assertEquals(TEST_TABLE, constraint.getForeignKeyTable());
        assertTrue(constraint.getForeignKeyColumns().size() == 1);
        assertEquals("COL1", constraint.getForeignKeyColumnsAsString());
        assertEquals("OTHERCAT", constraint.getPrimaryKeyCatalog());
        assertEquals("OTHERSCHEMA", constraint.getPrimaryKeySchema());
        assertEquals("OTHERTABLE", constraint.getPrimaryKeyTable());
        assertTrue(constraint.getPrimaryKeyColumns().size() == 1);
        assertEquals("PKCOL1", constraint.getPrimaryKeyColumnsAsString());
        assertTrue(constraint.getUpdateRule() == DatabaseMetaData.importedKeyRestrict);
        assertEquals("RESTRICT", constraint.getUpdateRuleAsString());
        assertTrue(constraint.getDeleteRule() == DatabaseMetaData.importedKeyCascade);
        assertEquals("CASCADE", constraint.getDeleteRuleAsString());
        assertTrue(constraint.getDeferrability() == DatabaseMetaData.importedKeyNotDeferrable);
        assertFalse(constraint.isDeferrable());

        constraint = findConstraint(constraints, "FK_MYTABLE2");
        assertNotNull(constraint);
        assertEquals("PK_OTHERTABLE2", constraint.getPrimaryKeyName());
        assertEquals(TEST_CATALOG, constraint.getForeignKeyCatalog());
        assertEquals(TEST_SCHEMA, constraint.getForeignKeySchema());
        assertEquals(TEST_TABLE, constraint.getForeignKeyTable());
        assertTrue(constraint.getForeignKeyColumns().size() == 2);
        assertEquals("COL2, COL3", constraint.getForeignKeyColumnsAsString());
        assertEquals("OTHERCAT", constraint.getPrimaryKeyCatalog());
        assertEquals("OTHERSCHEMA", constraint.getPrimaryKeySchema());
        assertEquals("OTHERTABLE2", constraint.getPrimaryKeyTable());
        assertTrue(constraint.getPrimaryKeyColumns().size() == 2);
        assertEquals("PKCOL1, PKCOL2", constraint.getPrimaryKeyColumnsAsString());
        assertTrue(constraint.getUpdateRule() == DatabaseMetaData.importedKeyNoAction);
        assertEquals("NO ACTION", constraint.getUpdateRuleAsString());
        assertTrue(constraint.getDeleteRule() == DatabaseMetaData.importedKeySetNull);
        assertEquals("SET NULL", constraint.getDeleteRuleAsString());
        assertTrue(constraint.getDeferrability() == DatabaseMetaData.importedKeyInitiallyDeferred);
        assertTrue(constraint.isDeferrable());
        assertTrue(constraint.isInitiallyDeferred());
    }

    @Test
    public void testGetForeignKeyConstraintsReferencingTable() throws Exception {
        Set<ForeignKeyConstraintMetaData> constraints = DatabaseMetaDataAccessor.getForeignKeyConstraintsReferencingTable(mockConnection,
                TEST_CATALOG, TEST_SCHEMA, TEST_TABLE);
        assertTrue(constraints.size() == 1);
        
        ForeignKeyConstraintMetaData constraint = findConstraint(constraints, "FK_SOMETABLE");
        assertNotNull(constraint);
        assertEquals("PK_MYTABLE", constraint.getPrimaryKeyName());
        assertEquals("SOMECAT", constraint.getForeignKeyCatalog());
        assertEquals("SOMESCHEMA", constraint.getForeignKeySchema());
        assertEquals("SOMETABLE", constraint.getForeignKeyTable());
        assertTrue(constraint.getForeignKeyColumns().size() == 1);
        assertEquals("COL1", constraint.getForeignKeyColumnsAsString());
        assertEquals(TEST_CATALOG, constraint.getPrimaryKeyCatalog());
        assertEquals(TEST_SCHEMA, constraint.getPrimaryKeySchema());
        assertEquals(TEST_TABLE, constraint.getPrimaryKeyTable());
        assertTrue(constraint.getPrimaryKeyColumns().size() == 1);
        assertEquals("PKCOL1", constraint.getPrimaryKeyColumnsAsString());
        assertTrue(constraint.getUpdateRule() == DatabaseMetaData.importedKeyRestrict);
        assertEquals("RESTRICT", constraint.getUpdateRuleAsString());
        assertTrue(constraint.getDeleteRule() == DatabaseMetaData.importedKeySetDefault);
        assertEquals("SET DEFAULT", constraint.getDeleteRuleAsString());
        assertTrue(constraint.getDeferrability() == DatabaseMetaData.importedKeyInitiallyImmediate);
        assertTrue(constraint.isDeferrable());
        assertFalse(constraint.isInitiallyDeferred());
    }    
    
    private ForeignKeyConstraintMetaData findConstraint(Set<ForeignKeyConstraintMetaData> constraints, String fkName) {
        for (ForeignKeyConstraintMetaData constraint : constraints) {
            if (fkName.equals(constraint.getForeignKeyName())) {
                return constraint;
            }
        }
        return null;
    }
}
