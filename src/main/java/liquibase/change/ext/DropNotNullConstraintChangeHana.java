package liquibase.change.ext;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.SetNullableStatement;

@DatabaseChange(name="dropNotNullConstraint", description = "Makes a column nullable", priority = ChangeMetaData.PRIORITY_DATABASE, appliesTo = "column", since = "3.0")
public class DropNotNullConstraintChangeHana extends
		DropNotNullConstraintChange {

	@Override
	public boolean supports(Database database) {
		return database instanceof HanaDBDatabase;
	}

	@Override
	public SqlStatement[] generateStatements(Database database) {
		// local variable introduced as superclass's schemaName is used for checksum calculation
		// TODO: is this logic also needed for the inverse?
		String schemaToUse = getSchemaName();
    	if (schemaToUse == null) {
    		schemaToUse = database.getDefaultSchemaName();
    	}
    	String columnDataTypeName = getColumnDataType();
    	if (columnDataTypeName == null && schemaToUse != null && database instanceof HanaDBDatabase) {
    		columnDataTypeName = ((HanaDBDatabase) database).getColumnDataTypeName(getCatalogName(), schemaToUse, getTableName(), getColumnName());
		}
    	
    	return new SqlStatement[] { new SetNullableStatement(
                getCatalogName(),
    			schemaToUse,
    			getTableName(), getColumnName(), columnDataTypeName, true) 
    	};

	}

}
