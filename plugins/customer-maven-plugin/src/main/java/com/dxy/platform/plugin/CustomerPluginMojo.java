package com.dxy.platform.plugin;

import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * @goal custom
 *
 * @phase pre-integration-test
 */
@Mojo(name = "custom")
public class CustomerPluginMojo extends AbstractMojo {

    /**
     * @parameter expression="${project}"
     * @readonly
     */
    @Parameter
    private MavenProject project;

    /**
     *
     * @parameter expression="${custom.prefix}"
     * default-value="+++"
     *
     */
    @Parameter(property = "custom.prefix", defaultValue = "+++")
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
