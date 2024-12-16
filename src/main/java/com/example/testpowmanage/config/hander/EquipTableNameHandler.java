package com.example.testpowmanage.config.hander;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import java.util.List;

public class EquipTableNameHandler implements TableNameHandler {
    private final List<String> tableNames;
    private static final ThreadLocal<String> EQUIP_NAME = new ThreadLocal<>();

    public EquipTableNameHandler(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public static void setEquipName(String equipName){
        EQUIP_NAME.set(equipName);
    }

    public static void removeEquipName(){
        EQUIP_NAME.remove();
    }

    //检查是否需要替换表名，如果tableNames中有则使用该线程重新造的名，如果tbs中无，则直接用这个
    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (tableNames.contains(tableName)){
            return EQUIP_NAME.get();
        } else {
            return tableName;
        }
    }
}