package com.dxy.platform.plugin;

import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 *
 * <pre>
 *     打印项目中源代码、测试代码目录以及输出目录
 * </pre>
 *
 * @goal custom
 *
 * @phase pre-integration-test
 */
@Mojo(name = "info")
public class CustomPluginMojo extends AbstractMojo {

    /**
     * <pre>
     *     ${project}
     * </pre>
     * @parameter expression="${project}"
     * @readonly
     */
    @Parameter(property = "project", defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     *
     * <pre>
     *     打印前缀
     * </pre>
     *
     * @parameter expression="${info.prefix}"
     *
     * default-value="+++"
     *
     */
    @Parameter(property = "info.prefix", defaultValue = "+++")
    private String prefix;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Build build = project.getBuild();
        String sourceDirectory = build.getSourceDirectory();
        String outputDirectory = build.getOutputDirectory();
        String testSourceDirectory = build.getTestSourceDirectory();
        String testOutputDirectory = build.getTestOutputDirectory();
        getLog().info("\n=============================\nProject Build Info:");
        String[] infos = {sourceDirectory, outputDirectory, testSourceDirectory, testOutputDirectory};
        for (String info: infos) {
            getLog().info(String.format("\t%s   %s", prefix, info));
        }
        getLog().info("==============================");

    }
}
