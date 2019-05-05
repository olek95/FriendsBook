package friendsbook.config.db;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class PhysicalNamingStrategyImplTest {
    private final PhysicalNamingStrategy namingStrategy = new PhysicalNamingStrategyImpl();
    
    @Test
    public void testOneWordTableName() {
        String tableName = "table";
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier(tableName, false), null).getText(), tableName);
    }
    
    @Test
    public void testMoreThanOneWordTableName() {
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier("tableName", false), null).getText(), "table_name");
    }
    
    @Test
    public void testMoreThanOneWordTableNameWithUnderscores() {
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier("some_table_name", false), null).getText(), "some_table_name");
    }
    
    @Test
    public void testMoreThanOneWordTableNameWithMixedUnderscores() {
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier("some_tableName_1", false), null).getText(), "some_table_name_1");
    }
    
    @Test
    public void testWholeTableNameUpperCase() {
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier("SOMETABLENAME", false), null).getText(), "sometablename");
    }
    
    @Test
    public void testOneWordColumnName() {
        String columnName = "column";
        assertEquals(namingStrategy.toPhysicalColumnName(new Identifier(columnName, false), null).getText(), columnName);
    }
    
    @Test
    public void testMoreThanOneWordColumnName() {
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier("columnName", false), null).getText(), "column_name");
    }
    
    @Test
    public void testMoreThanOneWordColumnNameWithUnderscores() {
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier("some_column_name", false), null).getText(), "some_column_name");
    }
    
    @Test
    public void testMoreThanOneWordColumnNameWithMixedUnderscores() {
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier("some_columnName_1", false), null).getText(), "some_column_name_1");
    }
    
    @Test
    public void testWholeColumnNameUpperCase() {
        assertEquals(namingStrategy.toPhysicalTableName(new Identifier("SOMECOLUMNNAME", false), null).getText(), "somecolumnname");
    }
}
