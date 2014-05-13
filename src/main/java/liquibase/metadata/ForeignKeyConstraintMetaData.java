package liquibase.metadata;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

public class ForeignKeyConstraintMetaData {

    private String foreignKeyName;
    private String primaryKeyName;
    private String foreignKeyCatalog;
    private String foreignKeySchema;
    private String foreignKeyTable;
    private List<String> foreignKeyColumns = new ArrayList<String>(1);
    private String primaryKeyCatalog;
    private String primaryKeySchema;
    private String primaryKeyTable;
    private List<String> primaryKeyColumns = new ArrayList<String>(1);
    private short updateRule;
    private short deleteRule;
    private short deferrability;

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public String getForeignKeyCatalog() {
        return foreignKeyCatalog;
    }

    public void setForeignKeyCatalog(String foreignKeyCatalog) {
        this.foreignKeyCatalog = foreignKeyCatalog;
    }

    public String getForeignKeySchema() {
        return foreignKeySchema;
    }

    public void setForeignKeySchema(String foreignKeySchema) {
        this.foreignKeySchema = foreignKeySchema;
    }

    public String getForeignKeyTable() {
        return foreignKeyTable;
    }

    public void setForeignKeyTable(String foreignKeyTable) {
        this.foreignKeyTable = foreignKeyTable;
    }

    public List<String> getForeignKeyColumns() {
        return foreignKeyColumns;
    }

    public String getForeignKeyColumnsAsString() {
        return getEntriesAsCommaSeparatedString(foreignKeyColumns);
    }

    public void setForeignKeyColumns(List<String> foreignKeyColumns) {
        this.foreignKeyColumns = foreignKeyColumns;
    }

    public void appendForeignKeyColumn(String column) {
        foreignKeyColumns.add(column);
    }

    public String getPrimaryKeyCatalog() {
        return primaryKeyCatalog;
    }

    public void setPrimaryKeyCatalog(String primaryKeyCatalog) {
        this.primaryKeyCatalog = primaryKeyCatalog;
    }

    public String getPrimaryKeySchema() {
        return primaryKeySchema;
    }

    public void setPrimaryKeySchema(String primaryKeySchema) {
        this.primaryKeySchema = primaryKeySchema;
    }

    public String getPrimaryKeyTable() {
        return primaryKeyTable;
    }

    public void setPrimaryKeyTable(String primaryKeyTable) {
        this.primaryKeyTable = primaryKeyTable;
    }

    public List<String> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public String getPrimaryKeyColumnsAsString() {
        return getEntriesAsCommaSeparatedString(primaryKeyColumns);
    }

    public void setPrimaryKeyColumns(List<String> primaryKeyColumns) {
        this.primaryKeyColumns = primaryKeyColumns;
    }

    public void appendPrimaryKeyColumn(String column) {
        primaryKeyColumns.add(column);
    }

    public short getUpdateRule() {
        return updateRule;
    }

    public String getUpdateRuleAsString() {
        return getForeignKeyConstraintRuleAsString(updateRule);
    }

    public void setUpdateRule(short updateRule) {
        this.updateRule = updateRule;
    }

    public short getDeleteRule() {
        return deleteRule;
    }

    public String getDeleteRuleAsString() {
        return getForeignKeyConstraintRuleAsString(deleteRule);
    }

    public void setDeleteRule(short deleteRule) {
        this.deleteRule = deleteRule;
    }

    public short getDeferrability() {
        return deferrability;
    }

    public boolean isDeferrable() {
        return deferrability != DatabaseMetaData.importedKeyNotDeferrable;
    }

    public boolean isInitiallyDeferred() {
        return deferrability == DatabaseMetaData.importedKeyInitiallyDeferred;
    }

    public void setDeferrability(short deferrability) {
        this.deferrability = deferrability;
    }

    private String getEntriesAsCommaSeparatedString(List<String> list) {
        boolean first = true;
        StringBuilder result = new StringBuilder();
        for (String entry : list) {
            if (!first) {
                result.append(", ");
            }
            result.append(entry);
            first = false;
        }
        return result.toString();
    }

    private String getForeignKeyConstraintRuleAsString(int rule) {
        if (rule == DatabaseMetaData.importedKeyCascade) {
            return "CASCADE";
        } else if (rule == DatabaseMetaData.importedKeySetNull) {
            return "SET NULL";
        } else if (rule == DatabaseMetaData.importedKeySetDefault) {
            return "SET DEFAULT";
        } else if (rule == DatabaseMetaData.importedKeyRestrict) {
            return "RESTRICT";
        } else if (rule == DatabaseMetaData.importedKeyNoAction) {
            return "NO ACTION";
        }
        return "";
    }
}
