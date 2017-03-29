package liquibase.datatype.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.BlobType;
import liquibase.util.StringUtils;


@DataTypeInfo(name="blob", aliases = {"java.sql.Types.VARBINARY", "varbinary"},
        minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class VarbinaryTypeHanaDB extends BlobType {

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof HanaDBDatabase) {
        	String originalDefinition = StringUtils.trimToEmpty(getRawDefinition());
            return new DatabaseDataType(originalDefinition);
        }
        return super.toDatabaseDataType(database);
    }

}
