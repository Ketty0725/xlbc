package com.ketty.common_base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.util.*;

/**
 * @Auther: Ketty Allen
 * @Date:2023/1/15 - 0:21
 * @Description:com.ketty.common_base
 * @version: 1.0
 */
public class CodeGenerator {
    /**
     * 项目路径
     */
    private static final String PARENT_DIR = System.getProperty("user.dir");
    /**
     * 基本路径
     */
    private static final String SRC_MAIN_JAVA = "/src/main/java/";
    /**
     * xml路径
     */
    private static final String XML_PATH = PARENT_DIR + "/api/api_mapper/src/main/java/com/ketty/api_mapper/xml";
    /**
     * entity路径
     */
    private static final String ENTITY_PATH = PARENT_DIR + "/api/api_entity/src/main/java/com/ketty/api_entity";
    /**
     * mapper路径
     */
    private static final String MAPPER_PATH = PARENT_DIR + "/api/api_mapper/src/main/java/com/ketty/api_mapper";
    /**
     * service路径
     */
    private static final String SERVICE_PATH = PARENT_DIR + "/api/api_service/service/src/main/java/com/ketty/service";
    /**
     * serviceImpl路径
     */
    private static final String SERVICE_IMPL_PATH = PARENT_DIR + "/api/api_service/serviceImpl/src/main/java/com/ketty/serviceimpl";
    /**
     * controller路径
     */
    private static final String CONTROLLER_PATH = PARENT_DIR + "/api/api_web/src/main/java/com/ketty/api_web/controller";
    /**
     * 数据库url
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/chinesemedicine?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
    /**
     * 数据库用户名
     */
    private static final String USERNAME = "root";
    /**
     * 数据库密码
     */
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        // 要构建代码的表名
        String[] tableNames = {"user"};
        FastAutoGenerator.create(DB_URL, USERNAME, PASSWORD)
                // 全局配置
                .globalConfig(builder -> builder
                        .author("ketty")
                        .enableSwagger()
                        .disableOpenDir()
                        .dateType(DateType.ONLY_DATE)
                        .commentDate("yyyy-MM-dd")
                )
                // 包配置
                .packageConfig(builder -> builder
                        .parent("")
                        .xml("com.ketty.api_mapper.xml")
                        .entity("com.ketty.api_entity")
                        .mapper("com.ketty.api_mapper")
                        .service("com.ketty.service")
                        .serviceImpl("com.ketty.serviceimpl")
                        .controller("com.ketty.api_web.controller")
                        .pathInfo(getPathInfo())
                )
                // 策略配置
                .strategyConfig((scanner, builder) -> builder
                        .addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        // entity
                        .entityBuilder()
                        .enableChainModel()
                        .fileOverride()
                        .enableLombok()
                        .enableRemoveIsPrefix()
                        .enableTableFieldAnnotation()
                        .logicDeleteColumnName("deleted")
                        .idType(IdType.ASSIGN_ID)
                        .versionColumnName("version")
                        .naming(NamingStrategy.underline_to_camel)
                        .columnNaming(NamingStrategy.underline_to_camel)
                        .addTableFills(new Column("create_time", FieldFill.INSERT))
                        .addTableFills(new Property("update_time", FieldFill.INSERT_UPDATE))
                        // controller
                        .controllerBuilder()
                        .fileOverride()
                        .enableRestStyle()
                        .formatFileName("%sController")
                        // service
                        .serviceBuilder()
                        .fileOverride()
                        .superServiceClass(IService.class)
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        // mapper
                        .mapperBuilder()
                        .fileOverride()
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .superClass(BaseMapper.class)
                        .formatMapperFileName("%sMapper")
                        .formatXmlFileName("%sMapper")
                        .enableMapperAnnotation()
                )
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

    /**
     * 获取路径信息map
     *
     * @return {@link Map< OutputFile, String> }
     * @author MK
     * @date 2022/4/21 21:21
     */
    private static Map<OutputFile, String> getPathInfo() {
        Map<OutputFile, String> pathInfo = new HashMap<>(5);
        pathInfo.put(OutputFile.entity, ENTITY_PATH);
        pathInfo.put(OutputFile.mapper, MAPPER_PATH);
        pathInfo.put(OutputFile.service, SERVICE_PATH);
        pathInfo.put(OutputFile.serviceImpl, SERVICE_IMPL_PATH);
        pathInfo.put(OutputFile.controller, CONTROLLER_PATH);
        pathInfo.put(OutputFile.xml, XML_PATH);
        return pathInfo;
    }
}
