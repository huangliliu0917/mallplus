package com.zscat.mallplus.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.*;
import com.zscat.mallplus.bo.ColumnInfo;
import com.zscat.mallplus.sys.entity.GenConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成
 *
 * @author mallplus
 * @date 2019-01-02
 */
@Slf4j
public class GenUtil {

    private static final String TIMESTAMP = "Timestamp";

    private static final String BIGDECIMAL = "BigDecimal";

    private static final String PK = "PRI";

    private static final String EXTRA = "auto_increment";

    /**
     * 获取后端代码模板名称
     *
     * @return List
     */
    private static List<String> getAdminTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("Entity");

        templateNames.add("Mapper");

        templateNames.add("Service");
        templateNames.add("ServiceImpl");
        templateNames.add("menu.sql");
        templateNames.add("Controller");
        return templateNames;
    }

    /**
     * 获取前端代码模板名称
     *
     * @return List
     */
    private static List<String> getFrontTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("api");
        templateNames.add("index");
        templateNames.add("eForm");
        templateNames.add("add");
        templateNames.add("update");
        return templateNames;
    }
    /**
     * 获取前端代码模板名称
     *
     * @return List
     */
    private static List<String> getFrontTemplateNames1() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("api");
        templateNames.add("index");
        templateNames.add("eForm");
        templateNames.add("add");
        templateNames.add("update");
        return templateNames;
    }
    /**
     * 生成代码
     *
     * @param columnInfos 表元数据
     */
    public static void generatorCode(List<ColumnInfo> columnInfos, GenConfig genConfig, String tableName) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("package", genConfig.getPack());
        map.put("moduleName", genConfig.getModuleName());
        map.put("author", genConfig.getAuthor());
        map.put("date", LocalDate.now().toString());
        map.put("tableName", tableName);
        map.put("prefix", genConfig.getPrefix());

        String className = StringUtils.toCapitalizeCamelCase(tableName);
        String changeClassName = StringUtils.toCamelCase(tableName);

        // 判断是否去除表前缀
        if (StringUtils.isNotEmpty(genConfig.getPrefix())) {
            className = StringUtils.toCapitalizeCamelCase(StrUtil.removePrefix(tableName, genConfig.getPrefix()));
            changeClassName = StringUtils.toCamelCase(StrUtil.removePrefix(tableName, genConfig.getPrefix()));
        }
        map.put("className", className);
        map.put("upperCaseClassName", className.toUpperCase());
        map.put("changeClassName", changeClassName);
        map.put("hasTimestamp", false);
        map.put("queryHasTimestamp", false);
        map.put("queryHasBigDecimal", false);
        map.put("hasBigDecimal", false);
        map.put("hasQuery", false);
        map.put("auto", false);

        List<Map<String, Object>> columns = new ArrayList<>();
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        for (ColumnInfo column : columnInfos) {
            Map<String, Object> listMap = new HashMap<>();
            listMap.put("columnComment", column.getColumnComment());
            listMap.put("columnKey", column.getColumnKey());

            String colType = ColUtil.cloToJava(column.getColumnType().toString());
            String changeColumnName = StringUtils.toCamelCase(column.getColumnName().toString());
            String capitalColumnName = StringUtils.toCapitalizeCamelCase(column.getColumnName().toString());
            if (PK.equals(column.getColumnKey())) {
                map.put("pkColumnType", colType);
                map.put("pkChangeColName", changeColumnName);
                map.put("pkCapitalColName", capitalColumnName);
            }
            if (TIMESTAMP.equals(colType)) {
                map.put("hasTimestamp", true);
            }
            if (BIGDECIMAL.equals(colType)) {
                map.put("hasBigDecimal", true);
            }
            if (EXTRA.equals(column.getExtra())) {
                map.put("auto", true);
            }
            listMap.put("columnType", colType);
            listMap.put("columnName", column.getColumnName());
            listMap.put("isNullable", column.getIsNullable());
            listMap.put("columnShow", column.getColumnShow());
            listMap.put("changeColumnName", changeColumnName);
            listMap.put("capitalColumnName", capitalColumnName);

            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if (!StringUtils.isBlank(column.getColumnQuery())) {
                listMap.put("columnQuery", column.getColumnQuery());
                map.put("hasQuery", true);
                if (TIMESTAMP.equals(colType)) {
                    map.put("queryHasTimestamp", true);
                }
                if (BIGDECIMAL.equals(colType)) {
                    map.put("queryHasBigDecimal", true);
                }
                queryColumns.add(listMap);
            }
            columns.add(listMap);
        }
        map.put("columns", columns);
        map.put("queryColumns", queryColumns);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));

        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/admin/" + templateName + ".ftl");
            String filePath = getAdminFilePath(templateName, genConfig, className);

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, map);
        }

        // 生成前端代码
        List<String>  templates1 = getFrontTemplateNames();
        for (String templateName : templates1) {
            Template template = engine.getTemplate("generator/front/" + templateName + ".ftl");
            String filePath = getFrontFilePath(templateName, genConfig, map.get("changeClassName").toString());

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, map);
        }
        // 生成前端代码2
        List<String> templates2 = getFrontTemplateNames1();
        for (String templateName : templates2) {
            Template template = engine.getTemplate("generator/front1/" + templateName + ".ftl");
            String filePath = getFrontFilePath1(templateName, genConfig, map.get("changeClassName").toString());

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, map);
        }
    }

    /**
     * 定义后端文件路径以及名称
     */
    private static String getAdminFilePath(String templateName, GenConfig genConfig, String className) {

        String packagePath = genConfig.getPath();
        if ("Entity".equals(templateName)) {
            return packagePath + File.separator + "entity" + File.separator + className + ".java";
        }

        if ("Controller".equals(templateName)) {
            return packagePath + File.separator + "controller" + File.separator + className + "Controller.java";
        }

        if ("Service".equals(templateName)) {
            return packagePath + File.separator + "service" + File.separator + "I" + className + "Service.java";
        }

        if ("ServiceImpl".equals(templateName)) {
            return packagePath + File.separator + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if ("menu.sql".equals(templateName)) {
            return packagePath + File.separator + className + ".sql";
        }


        if ("Mapper".equals(templateName)) {
            return packagePath + File.separator + "mapper" + File.separator + className + "Mapper.java";
        }


        return null;
    }

    /**
     * 定义前端文件路径以及名称
     */
    private static String getFrontFilePath(String templateName, GenConfig genConfig, String apiName) {
        String path = genConfig.getPath();

        if ("api".equals(templateName)) {
            return path + File.separator + apiName + ".js";
        }

        if ("index".equals(templateName)) {
            return path + File.separator + apiName + File.separator + "index.vue";
        }

        if ("eForm".equals(templateName)) {
            return path + File.separator + apiName + File.separator + "components" + File.separator + "detail.vue";
        }
        if ("add".equals(templateName)) {
            return path + File.separator + apiName + File.separator + File.separator + "add.vue";
        }
        if ("update".equals(templateName)) {
            return path + File.separator + apiName + File.separator + File.separator + "update.vue";
        }
        return null;
    }
    /**
     * 定义前端文件路径以及名称
     */
    private static String getFrontFilePath1(String templateName, GenConfig genConfig, String apiName) {
        String path = genConfig.getPath();

        if ("api".equals(templateName)) {
            return path + File.separator +"shop"+ File.separator+ apiName + ".text";
        }

        if ("index".equals(templateName)) {
            return path + File.separator +"shop"+ File.separator+ apiName + File.separator + "index.vue";
        }

        if ("eForm".equals(templateName)) {
            return path + File.separator +"shop"+ File.separator+ apiName + File.separator + "components" + File.separator + "reduction.less";
        }
        if ("add".equals(templateName)) {
            return path + File.separator +"shop"+ File.separator+ apiName + File.separator + "components" + File.separator + "add.vue";
        }
        if ("update".equals(templateName)) {
            return path + File.separator +"shop"+ File.separator+ apiName + File.separator + "components" + File.separator + "edit.vue";
        }
        return null;
    }
    private static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        // 生成目标文件
        Writer writer = null;
        try {
            System.out.println(file);
            FileUtil.touch(file);
            System.out.println(file.getName());
            writer = new FileWriter(file);
            template.render(map, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert writer != null;
            writer.close();
        }
    }
}
