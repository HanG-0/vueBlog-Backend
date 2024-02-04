package com.vueblog;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.*;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (!ipt.isEmpty()){
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器 (依赖generator版本过高可能报错)
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //得到当前工作路径（绝对路径）
        String projectPath = System.getProperty("user.dir");
//       指定输出目录
        gc.setOutputDir(projectPath + "\\src\\main\\java");
//       设置作者名 用于类注释和方法注释
        gc.setAuthor("Generator@Hang");
        //生成后不用打开代码所在目录
        gc.setOpen(false);
//      gc.setSwagger2(true); //实体属性 Swagger2 注解

        //设置持久层类的名称之后加上‘Dao’字符串
//        gc.setEntityName("%sDao");
        //服务类的名称之后加上‘Service’字符串
        gc.setServiceName("%sService");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        //设置数据源路径
        dsc.setUrl("jdbc:mysql://localhost:3306/vueblog?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        // dsc.setSchemaName("public");
        //链接数据库的驱动
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("admin");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(null);
        pc.setParent("com.vueblog");
        mpg.setPackageInfo(pc);

//        自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //设置表名生成策略 下划线转驼峰
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //设置字段名生成策略 下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //是否为lombok模式 为true则生成lombok注解
        strategy.setEntityLombokModel(true);
        //生成controller
        strategy.setRestControllerStyle(true);
        //需要生成的表名(注意！！这里生成的表名要与数据库中已有表名对应（没有就手动创建），否则会生成空包）
        String[] tableName = scanner("表名，多个英文逗号分割").split(",");
        strategy.setInclude(tableName);
        //controller映射地址 url驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        //去除表的前缀
        strategy.setTablePrefix("m_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}