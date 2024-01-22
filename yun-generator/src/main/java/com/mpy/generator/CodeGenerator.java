package com.mpy.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Scanner;

public class CodeGenerator {
    /**
     * <p>
     *     读取控制台内容
     *     在控制太输入表名，就会自动生成所需要的文件
     * </p>
     */
    public static String scanner(String tip){
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + ":");
        System.out.println(help.toString());
        if (scanner.hasNext()){
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)){
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "! ");
    }

    public static void main(String[] args){
        //代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        // 逆向工程生成的文件，要输出到具体的目录地址 加上模块名 要不然会生成在 父工程下
        gc.setOutputDir(projectPath + "/yun-generator/src/main/java");
        //作者
        gc.setAuthor("M");
        // 输出目录
        gc.setOpen(false);

        //xml开启 BaseResultMap
        gc.setBaseResultMap(true);
        //xml 开启 BaseColumnList
        gc.setBaseColumnList(true);
        // 实体属性 Swagger2 注解，因为用到了Swagger2的接口文档
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/yun?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("ProjectManager");
        dsc.setPassword("test");
        mpg.setDataSource(dsc);

        // 包配置,生成对应类的所在的包名
        PackageConfig pc = new PackageConfig();

        pc.setParent("com.mpy").setEntity("pojo").setMapper("mapper").setController("controller").setService("service").setServiceImpl("service.impl");
        mpg.setPackageInfo(pc);

        //自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
//        String templatePath = "/templates/mapper.xml.vm";

        //自定义输出配置
        ArrayList<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath){
            @Override
            public String outputFile(TableInfo tableInfo) {
                //自定义输出名，如果 Entity 设置了前后缀，此处注意xml的名称会跟着发送变化
                return projectPath + "/yun-generator/src/main/resources/mapper/" + tableInfo.getEntityName() + "mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        //策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 数据库表映射到实体的命名策略。数据库表名有下划线，所以要转为驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 数据库表字段映射到实体的命名策略。数据库字段本来就是驼峰命名，没有下划线，不需要改变
        strategy.setColumnNaming(NamingStrategy.no_change);
        //lombok模型
        strategy.setEntityLombokModel(true);
        // 生成 @RestController 控制器。 返回前端是json字符串
        strategy.setRestControllerStyle(true);

        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        //表前缀。在生成的时候，可以省略掉前缀。
        strategy.setTablePrefix("t_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

    }
}
