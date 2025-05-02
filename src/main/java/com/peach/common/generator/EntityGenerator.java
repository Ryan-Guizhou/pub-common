package com.peach.common.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class EntityGenerator {

    private static final String URL = "jdbc:mysql://localhost:3306/peach?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String PACKAGE_NAME = "com.peach.security.entity";
    private static final String OUTPUT_PATH = System.getProperty("user.dir") + File.separator + "src/main/java/generator/";
    private static final String lineSeparator = System.lineSeparator();


    public static void main(String[] args) {
        String tableName = "PEACH_USER";
        generateEntity(tableName);
    }

    public static String generateEntity(String tableName) {
        String className = toCamelCase(tableName, true) + "DO"; // 生成 UserDO 类
        return generateEntity(tableName, className);
    }

    public static String generateEntity(String tableName, String className) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);

            Set<String> primaryKeySet = new HashSet<>();
            while (primaryKeys.next()) {
                primaryKeySet.add(primaryKeys.getString("COLUMN_NAME"));
            }

            StringBuilder fields = new StringBuilder();
            String createTime = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());



            fields.append("package ").append(PACKAGE_NAME).append(";").append(lineSeparator).append(lineSeparator);
            fields.append("import lombok.Data;").append(lineSeparator);
            fields.append("import io.swagger.annotations.ApiModelProperty;").append(lineSeparator);
            fields.append("import javax.persistence.*;").append(lineSeparator);
            fields.append("import java.io.Serializable;").append(lineSeparator).append(lineSeparator);

            fields.append("/**").append(lineSeparator);
            fields.append(" * @Author Mr Shu").append(lineSeparator);
            fields.append(" * @Version 1.0.0").append(lineSeparator);
            fields.append(" * @Description //TODO").append(lineSeparator);
            fields.append(" * @CreateTime ").append(createTime).append(lineSeparator);
            fields.append(" */").append(lineSeparator);

            fields.append("@Data").append(lineSeparator);
            fields.append("@Table(name = \"").append(formatString(tableName)).append("\")").append(lineSeparator);
            fields.append("public class ").append(className).append(" implements Serializable {").append(lineSeparator).append(lineSeparator);
            fields.append("    private static final long serialVersionUID = 1L;").append(lineSeparator).append(lineSeparator);

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                String remarks = columns.getString("REMARKS"); // 字段注释
                String javaType = sqlTypeToJavaType(columnType);
                String fieldName = toCamelCase(columnName, false);

                if (primaryKeySet.contains(columnName)) {
                    fields.append("    @Id").append(lineSeparator);
                }
                fields.append("    @Column(name = \"").append(formatString(columnName)).append("\")").append(lineSeparator);
                if (remarks != null && !remarks.isEmpty()) {
                    fields.append("    @ApiModelProperty(value = \"").append(remarks).append("\")").append(lineSeparator);
                }
                fields.append("    private ").append(javaType).append(" ").append(fieldName).append(";").append(lineSeparator).append(lineSeparator);
            }

            fields.append("    public static void main(String[] args) {").append(lineSeparator);
            fields.append(String.format("        System.out.println(MapperGenerator.genMapper(%s));",className +".class")).append(lineSeparator);
            fields.append("    }").append(lineSeparator);
            fields.append("}").append(lineSeparator);
            writeToFile(className, fields.toString());
            System.out.println("实体类 " + className + " 生成成功！");
            return className;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String formatString(String input) {
        // 如果包含 '_', 处理每个部分
        if (input.contains("_")) {
            StringBuilder result = new StringBuilder();
            for (char c : input.toCharArray()) {
                if (c == '_') {
                    result.append(c); // 下划线保持不变
                } else {
                    result.append(Character.toUpperCase(c)); // 其他字符转大写
                }
            }
            return result.toString();
        } else {
            // 没有 '_', 整个字符串转大写
            return input.toUpperCase();
        }
    }

    private static String sqlTypeToJavaType(String sqlType) {
        sqlType = sqlType.toUpperCase();
        switch (sqlType) {
            case "VARCHAR":
            case "CHAR":
            case "TEXT":
            case "LONGTEXT":
            case "JSON":
                return "String";
            case "BIGINT":
                return "Long";
            case "INT":
            case "INTEGER":
            case "SMALLINT":
            case "TINYINT":
            case "BIT":
                return "Integer";
            case "DECIMAL":
            case "NUMERIC":
                return "java.math.BigDecimal";
            case "DOUBLE":
            case "FLOAT":
                return "Double";
            case "DATE":
            case "DATETIME":
            case "TIMESTAMP":
                return "java.util.Date";
            default:
                return "String"; // 默认类型
        }
    }

    private static String toCamelCase(String name, boolean capitalizeFirst) {
        String[] parts = name.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
        }
        if (!capitalizeFirst) {
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        }
        return sb.toString();
    }

    private static void writeToFile(String className, String content) {
        File outputDir = new File(OUTPUT_PATH);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File file = new File(OUTPUT_PATH + className + ".java");
        if (file.exists()) {
            System.out.println("⚠️ 文件已存在: " + file.getAbsolutePath() + "，跳过生成。");
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            System.out.println("✅ 已生成: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
