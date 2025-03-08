package com.peach.common.generator;

import javax.persistence.EntityManager;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/2/27 14:29
 */
public class GeneratorFacade {

    /**
     * 输出表名 生成实体类和mapper，输出路径默认为
     * System.getProperty("user.dir") + File.separator + "src/main/java/generator/"
     * @param tableName
     * @param outputPath
     * @throws ClassNotFoundException
     */
    public static void generate(String tableName,String outputPath) throws ClassNotFoundException {
        String className = EntityGenerator.generateEntity(tableName);
        Class<?> clazz = Class.forName(className);
        // 截取className最后DO之前的内容
        String mapperName =  className.substring(0, className.lastIndexOf("DO")).concat("Dao");
        MapperGenerator.genMapperToFile(clazz,mapperName,outputPath);
    }
}
