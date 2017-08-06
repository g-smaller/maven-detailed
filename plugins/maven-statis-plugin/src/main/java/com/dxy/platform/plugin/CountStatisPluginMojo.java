package com.dxy.platform.plugin;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoguo on 2017/8/6.
 */

@Mojo(name = "count")
public class CountStatisPluginMojo extends AbstractMojo {

    private static final String[] INCLUDES_DEFAULT = {"java", "xml", "sql", "properties"};
    private static final String[] RATIOS_DEFAULT = {"1.0", "0.25", "0.25", "0.25"};
    private static final String DOT = ".";

    /**
     * @parameter expression="${project.basedir}"
     * @required
     * @readonly
     */
    @Parameter(property = "project.basedir", defaultValue = "./")
    private File basedir;
    /**
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     * @readonly
     */
    @Parameter(property = "project.build.sourceDirectory", defaultValue = "./src/main/java")
    private File sourcedir;
    /**
     * @parameter expression="${project.build.testSourceDirectory}"
     * @required
     * @readonly
     */
    @Parameter(property = "project.build.testSourceDirectory", defaultValue = "./src/test/java")
    private File testSourcedir;
    /**
     * @parameter expression="${project.resources}"
     * @required
     * @readonly
     */
    @Parameter(property = "project.build.resources")
    private List<Resource> resources;
    /**
     * @parameter expression="${project.testResources}"
     * @required
     * @readonly
     */
    @Parameter(property = "project.build.testResources")
    private List<Resource> testResources;
    /**
     * @parameter
     */
    @Parameter(property = "statis.includes")
    private String[] includes;
    /**
     * @parameter
     */
    @Parameter(property = "statis.ratios")
    private String[] ratios;
    //TODO 定义为double[],从xml读取时提示java.lang.ClassCastException: [D cannot be cast to [Ljava.lang.Object;
    @Parameter
    private Map<String, Double> ratioMap = new HashMap<String, Double>();
    @Parameter
    private long realTotal;
    @Parameter
    private long fakeTotal;

    public void execute() throws MojoExecutionException, MojoFailureException {
        initRatioMap();
        try {
            countDir(sourcedir);
            countDir(testSourcedir);

            for (Resource res : resources) {
                countDir(new File(res.getDirectory()));
            }
            for (Resource res : testResources) {
                countDir(new File(res.getDirectory()));
            }

            getLog().info("TOTAL LINES:" + fakeTotal + " (" + realTotal + ")");

        } catch (IOException e) {
            throw new MojoExecutionException("Unable to count lines of code", e);
        }
    }

    private void initRatioMap() throws MojoExecutionException {
        if (includes == null || includes.length == 0) {
            includes = INCLUDES_DEFAULT;
            ratios = RATIOS_DEFAULT;
        }
        if (ratios == null || ratios.length == 0) {
            ratios = new String[includes.length];
            for (int i = 0; i < includes.length; i++) {
                ratios[i] = "1.0";
            }
        }
        if (includes.length != ratios.length) {
            throw new MojoExecutionException("pom.xml error: the length of includes is inconsistent with ratios!");
        }
        ratioMap.clear();
        for (int i = 0; i < includes.length; i++) {
            ratioMap.put(includes[i].toLowerCase(), Double.parseDouble(ratios[i]));
        }
    }

    private void countDir(String dir) throws IOException {
        if (StringUtils.isNotBlank(dir)) {
            countDir(new File(dir));
        }
    }

    private void countDir(File dir) throws IOException {
        if (isAvailabilityFile(dir)) {
            List<File> collected = new ArrayList<File>();
            collectFiles(collected, dir);

            int realLine = 0;
            int fakeLine = 0;
            for (File file : collected) {
                int[] line = countLine(file);
                realLine += line[0];
                fakeLine += line[1];
            }

            String path = dir.getAbsolutePath().substring(basedir.getAbsolutePath().length());
            StringBuilder info = new StringBuilder()
                    .append(path).append(" : ")
                    .append(fakeLine)
                    .append(" (" + realLine + ")")
                    .append(" lines of code in ")
                    .append(collected.size())
                    .append(" files");
            getLog().info(info.toString());
        }
    }

    private void collectFiles(List<File> collected, File dir) throws IOException {
        if (isAvailabilityFile(dir)) {
            if (dir.isFile()) {
                collected.add(dir);
            } else {
                for (File file : dir.listFiles()) {
                    collectFiles(collected, file);
                }
            }
        }
    }

    private int[] countLine(File file) throws IOException {
        if (isAvailabilityFile(file)) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int realLine = 0;
            try{
                while(reader.ready()){
                    reader.readLine();
                    realLine ++;
                }
            }finally{
                reader.close();
            }
            int fakeLine = (int) (realLine * getRatio(file));
            realTotal += realLine;
            fakeTotal += fakeLine;

            StringBuilder info = new StringBuilder().append(file.getName()).append("  : ").append(fakeLine).append(" ("+realLine+")")
                    .append(" lines");
            getLog().debug(info.toString());

            return new int[]{realLine, fakeLine};
        }
        return new int[]{0, 0};
    }

    private double getRatio(File file){
        double ratio = 1.0;
        String type = getFileType(file);
        if(ratioMap.containsKey(type)){
            ratio = ratioMap.get(type);
        }
        return ratio;
    }

    private boolean isFileTypeInclude(File file){
        boolean result = false;
        String fileType = getFileType(file);
        if(fileType != null && ratioMap.keySet().contains(fileType.toLowerCase())){
            result = true;
        }
        return result;
    }

    private String getFileType(File file){
        String result = null;
        String fname = file.getName();
        int index = fname.lastIndexOf(DOT);
        if(index > 0){
            String type = fname.substring(index+1);
            result = type.toLowerCase();
        }
        return result;
    }



    private boolean isAvailabilityFile(File file) {
        return file != null && file.exists();
    }
}
