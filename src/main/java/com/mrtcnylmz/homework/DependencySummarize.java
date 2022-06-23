package com.mrtcnylmz.homework;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "summarize", defaultPhase = LifecyclePhase.COMPILE)
public class DependencySummarize extends AbstractMojo{
	
	
	@Parameter(defaultValue = "${project}", required = true)
	private MavenProject project;
	
	@Parameter(defaultValue = "${project.build.directory}", readonly = true)
	private File target;
	
	@Parameter(defaultValue = "outputFile", required = true)
	private String outputFile;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
	List<Dependency> dependencies = project.getDependencies();
	getLog().info("Number of Dependencies: " + dependencies.size());
	List<Developer> devs = project.getDevelopers();
	getLog().info("Number of Devs: " + devs.size());
	List<org.apache.maven.model.Plugin> plugins = project.getPluginManagement().getPlugins();
	getLog().info("Number of Plugins: " + plugins.size());
	
	String devList = "";
	for(Developer dev : devs) {
			devList = devList + "Developer " + (devs.indexOf(dev)+1) + " Name: " + dev.getName() + " \n" ;
	}
	
	String dependenciesList = "";
	for(Dependency dev : dependencies) {
		dependenciesList = dependenciesList + "Dependency: " + dev.getGroupId() + "." + dev.getArtifactId() + " \n" ;
	}
	
	String pluginList = "";
	for (org.apache.maven.model.Plugin plugin : plugins) {
		pluginList = pluginList + "Plugin: " + plugin.getArtifactId() + "\n";
	}
	
	String summaryText = "";
	summaryText = summaryText + "Project Info: " + project.getGroupId() + "." + project.getArtifactId() + "." + project.getVersion() + "\n" + 
	"Developers: " + "\n" + devList +
			"Release Date: " + project.getProperties().get("release.date") + "\n" +
	"Dependencies " + "\n" + dependenciesList + 
			"Plugins" + "\n" + pluginList;
	
	File newFile = new File(target, outputFile+".txt");
	try {
		newFile.createNewFile();
		Files.write(newFile.toPath(), summaryText.getBytes(), StandardOpenOption.APPEND);
		getLog().info("Summary File Generated to: " + newFile.getAbsolutePath());
	} catch (IOException e) {
		getLog().error("Failed to Generate Summary File!", e);
		throw new MojoExecutionException("Failed to Generate Summary File!", e);
	}
	}
}
