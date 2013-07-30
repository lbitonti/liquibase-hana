package liquibase.datatype.ext;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.UUIDType;
import liquibase.exception.DatabaseException;

@DataTypeInfo(name="uuid", aliases = {"uniqueidentifier"},
        minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class UUIDTypeHanaDB extends UUIDType {

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof HanaDBDatabase) {
            return new DatabaseDataType("VARCHAR", 36);
        }
        return super.toDatabaseDataType(database);
    }

}
