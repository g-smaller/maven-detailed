package com.dxy.platform.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by guoguo on 2017/8/6.
 */
@Mojo(name = "type")
public class TypeGoalMojo extends AbstractMojo {

    /**
     *  1. properties expression="${custom.type.boolType}"
     *  2.  <configuration>
     *          <boolType>false</boolType>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.boolType", defaultValue = "true")
    private boolean boolType;

    /**
     *  1. properties expression="${custom.type.intType}"
     *  2.  <configuration>
     *          <intType>1</intType>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.intType", defaultValue = "0")
    private Integer intType;

    /**
     *  1. properties expression="${custom.type.doubleType}"
     *  2.  <configuration>
     *          <doubleType>1.0</doubleType>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.doubleType", defaultValue = "0.0")
    private Double doubleType;

    /**
     *  1. properties expression="${custom.type.dateType}"
     *  2.  <configuration>
     *          <dateType>2017-08-06 15:26:00</dateType>
     *      </configuration>
     * @see {@link java.text.DateFormat#parse(String)}
     */
    @Parameter(property = "custom.type.dateType", defaultValue = "2017-08-06 15:26:00")
    private Date dateType;

    /**
     *  1. properties expression="${custom.type.fileType}"
     *  2.  <configuration>
     *          <fileType>/Users/guoguo/</fileType>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.fileType", defaultValue = "./")
    private File fileType;


    /**
     *  1. properties expression="${custom.type.urlType}"
     *  2.  <configuration>
     *          <urlType>http://www.apache.org</urlType>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.urlType", defaultValue = "http://www.apache.org")
    private URL urlType;

    /**
     *  1. properties expression="${custom.type.enumType}"
     *  2.  <configuration>
     *          <enumType>GREEN</enumType>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.enumType", defaultValue = "RED")
    private Color enumType;


    /**
     *
     * 多值参数类型

     * 下面介绍可以在Mojo中作为参数使用的各种复合对象，及它们在POM中的解释规则。一般来说，保存参数值（和参数值中的元素）的对象的类型是按以下步骤决定的（确定有效类型的第一步）：
     *      1. 如果XML元素包含implementation属性，就使用它
     *      2. 如果XML标签包含”.”，尝试解析为类的全名
     *      3. 尝试将XML标签（首字母大写）解析为Mojo所在包中的类
     *      4. 对数组，使用数组的元素类型（例如，对String[]参数使用String类型）；对集合和映射，使用Mojo配置中指定的类；使用String作为集合类型的entry和map的值的类型。
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */

    /**
     *  1. properties expression="${custom.type.enumType}"
     *  2.  <configuration>
     *          <arrayTypes>
     *              <param>1</param>
     *              <param>2</param>
     *          </arrayTypes>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.arrayTypes", defaultValue = "default")
    private String[] arrayTypes;

    /**
     *  1. properties expression="${custom.type.listType}"
     *  2.  <configuration>
     *          <listType>
     *              <param>1</param>
     *              <param>2</param>
     *          </listType>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.listType", defaultValue = "default")
    private List listType;

    /**
     *  1. properties expression="${custom.type.mapType}"
     *  2.  <configuration>
     *          <mapType>
     *              <key1>1</key1>
     *              <key2>2</key2>
     *          </mapType>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.mapType")
    private Map<String, Object> mapType;

    /**
     *  1. properties expression="${custom.type.properties}"
     *  2.  <configuration>
     *          <properties>
     *              <property>
     *                  <name>key1</name>
     *                  <value>value1</value>
     *              </property>
     *              <property>
     *                  <name>key2</name>
     *                  <value>value2</value>
     *              </property>
     *          </properties>
     *      </configuration>
     *
     */
    @Parameter(property = "custom.type.properties")
    private Properties properties;

    /**
     * *     <configuration>
     *          <person implementation="com.dxy.platform.plugin.Person">
     *              <firstName>guo</firstName>
     *              <lastName>guo</lastName>
     *          </person>
     *      </configuration>
     */
    @Parameter
    private Person person;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("boolType -> " + boolType);
        getLog().info("intTYpe -> " + intType);
        getLog().info("doubleType -> " + doubleType);
        getLog().info("dateType -> " + dateType);
        getLog().info("fileType -> " + fileType);
        getLog().info("urlType -> " + urlType);
        getLog().info("enumType -> " + enumType);
        getLog().info("arrayType -> " + arrayTypes);
        getLog().info("listType -> " + listType);
        getLog().info("mapType -> "  + mapType);
        getLog().info("propertiesType -> " + properties);
        getLog().info("objectType -> " + person);
    }
}
