package liquibase.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.logging.LogFactory;

public class DatabaseMetaDataAccessor {

    public static ColumnMetaData getColumnMetaData(JdbcConnection connection, 
            String catalogName, String schemaName, String tableName, String columnName) throws DatabaseException {

        ResultSet rs = null;
        ColumnMetaData metaData = null;
        try {
            rs = connection.getMetaData().getColumns(catalogName, schemaName, tableName, columnName);
            if(rs.next()) {
                metaData = new ColumnMetaData(catalogName, schemaName, tableName, columnName);
                metaData.setSqlType(rs.getInt("DATA_TYPE"));
                metaData.setTypeName(rs.getString("TYPE_NAME"));
                metaData.setColumnSize(rs.getInt("COLUMN_SIZE"));
            }
            return metaData;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            closeResultSet(rs);
        }
    }
    
    public static Set<ForeignKeyConstraintMetaData> getForeignKeyConstraintsForTable(JdbcConnection connection,
            String catalogName, String schemaName, String tableName) throws DatabaseException {
        ResultSet rs = null;
        try {
            rs = connection.getMetaData().getImportedKeys(catalogName, schemaName, tableName);
            Set<ForeignKeyConstraintMetaData> constraints = getForeignKeyConstraintsFromMetaData(rs);
            return constraints;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            closeResultSet(rs);
        }
    }

    public static Set<ForeignKeyConstraintMetaData> getForeignKeyConstraintsReferencingTable(JdbcConnection connection,
            String catalogName, String schemaName, String tableName) throws DatabaseException {

        ResultSet rs = null;
        try {
            rs = connection.getMetaData().getExportedKeys(catalogName, schemaName, tableName);
            Set<ForeignKeyConstraintMetaData> constraints = getForeignKeyConstraintsFromMetaData(rs);
            return constraints;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            closeResultSet(rs);
        }
    }

    private static Set<ForeignKeyConstraintMetaData> getForeignKeyConstraintsFromMetaData(ResultSet rs)
            throws SQLException {
        Set<ForeignKeyConstraintMetaData> constraints = new HashSet<ForeignKeyConstraintMetaData>();

        ForeignKeyConstraintMetaData fkMeta = null;
        while (rs.next()) {
            if (rs.getShort("KEY_SEQ") == 1) {
                if (fkMeta != null) {
                    constraints.add(fkMeta);
                }
                fkMeta = new ForeignKeyConstraintMetaData();
                fkMeta.setForeignKeyName(rs.getString("FK_NAME"));
                fkMeta.setPrimaryKeyName(rs.getString("PK_NAME"));
                fkMeta.setForeignKeyCatalog(rs.getString("FKTABLE_CAT"));
                fkMeta.setForeignKeySchema(rs.getString("FKTABLE_SCHEM"));
                fkMeta.setForeignKeyTable(rs.getString("FKTABLE_NAME"));
                fkMeta.appendForeignKeyColumn(rs.getString("FKCOLUMN_NAME"));
                fkMeta.setPrimaryKeyCatalog(rs.getString("PKTABLE_CAT"));
                fkMeta.setPrimaryKeySchema(rs.getString("PKTABLE_SCHEM"));
                fkMeta.setPrimaryKeyTable(rs.getString("PKTABLE_NAME"));
                fkMeta.appendPrimaryKeyColumn(rs.getString("PKCOLUMN_NAME"));
                fkMeta.setUpdateRule(rs.getShort("UPDATE_RULE"));
                fkMeta.setDeleteRule(rs.getShort("DELETE_RULE"));
                fkMeta.setDeferrability(rs.getShort("DEFERRABILITY"));
            } else {
                fkMeta.appendForeignKeyColumn(rs.getString("FKCOLUMN_NAME"));
                fkMeta.appendPrimaryKeyColumn(rs.getString("PKCOLUMN_NAME"));
            }
        }
        if (fkMeta != null) {
            constraints.add(fkMeta);
        }
        return constraints;
    }

    private static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LogFactory.getInstance().getLog().severe("SQL exception occurred", e);
            }
        }
    }
}
