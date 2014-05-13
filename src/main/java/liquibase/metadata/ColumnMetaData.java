package liquibase.metadata;

import java.sql.Types;

public class ColumnMetaData {

    private String catalog;
    private String schema;
    private String table;
    private String column;
    private int sqlType;
    private String typeName;
    private int columnSize;
    
    public ColumnMetaData() {
    }
    
    public ColumnMetaData(String catalog, String schema, String table, String column) {
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
        this.column = column;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }
    
    public boolean requiresSizeForDefinition() {
        switch (sqlType) {
            case Types.VARCHAR:
            case Types.NVARCHAR:
            case Types.VARBINARY:
                return true;
            default:
                return false;
        }
    }
    
}
