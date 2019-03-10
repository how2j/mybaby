package com.how2java.sqlite;
  
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
  
public class SQLiteDialectIdentityColumnSupport extends IdentityColumnSupportImpl {
    public SQLiteDialectIdentityColumnSupport(Dialect dialect) {
        super(dialect);
    }
  
    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }
 
    @Override
    public boolean hasDataTypeInIdentityColumn() {
        return false;
    }
  
    @Override
    public String getIdentitySelectString(String table, String column, int type) {
        return "select last_insert_rowid()";
    }
  
    @Override
    public String getIdentityColumnString(int type) {
        return "integer";
    }
}