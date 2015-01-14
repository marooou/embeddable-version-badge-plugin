package org.jenkinsci.plugins.versionbadge;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marek on 2015-01-14.
 */
public class GenerateVersionAction extends Notifier {

    private static final int VERSION_MAX_LENGTH = 15;
    private final String filePath;
    private final String regex;
    private final boolean failOnError;
    private final boolean parseFilePath;

    @DataBoundConstructor
    public GenerateVersionAction(String filePath, String regex, boolean parseFilePath, boolean failOnError) {
        this.filePath = filePath;
        this.regex = regex;
        this.failOnError = failOnError;
        this.parseFilePath = parseFilePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getRegex() {
        return regex;
    }

    public boolean isFailOnError() {
        return failOnError;
    }

    public boolean isParseFilePath() {
        return parseFilePath;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        listener.getLogger().print("Generating the build version badge... ");
        try {
            BuildVersion buildVersion = new BuildVersion(build);
            buildVersion.setVersion(this.parseVersionFile(build, listener));
            buildVersion.save();
            listener.getLogger().println("OK");
            return true;
        } catch (IOException ex) {
            if (this.failOnError) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                listener.getLogger().println("\r\nERROR: " + sw.toString());
                return false;
            }
            else {
                listener.getLogger().println("ERROR: " + ex.getMessage());
                return true;
            }
        }
    }

    private String parseVersionFile(AbstractBuild<?, ?> build, BuildListener listener) throws IOException, InterruptedException {
        assert this.filePath != null;
        EnvVars envVars = build.getEnvironment(listener);
        FilePath workspaceDir = build.getWorkspace().child(envVars.get("WORKSPACE"));
        if (this.filePath.equals("")) {
            throw new IOException("Version file path cannot be an empty string");
        }
        FilePath versionFile = workspaceDir.child(this.filePath);
        if (!versionFile.exists()) {
            throw new FileNotFoundException(versionFile.getRemote() + " could not be found");
        }
        if (!this.parseFilePath && versionFile.isDirectory()) {
            throw new IOException("Expected version file, directory given (" + versionFile.getRemote() + ")");
        }

        String versionContent = this.parseFilePath
                ? versionFile.getRemote()
                : FileUtils.readTextFile(versionFile);
        if (this.regex != null && !this.regex.equals("")) {
            Pattern pattern = Pattern.compile(this.regex);
            Matcher matcher = pattern.matcher(versionContent);
            if (matcher.find() ) {
                if (matcher.groupCount() > 0) {
                    versionContent = matcher.group(1);
                } else {
                    throw new IOException("Version regex must define at least one group.");
                }
            }
            else {
                throw new IOException("Regex " + this.regex + " found nothing in the " + versionFile.getRemote() + " file.");
            }
        }

        if (versionContent.length() > VERSION_MAX_LENGTH) {
            return versionContent.substring(0, VERSION_MAX_LENGTH - 1);
        }
        return versionContent;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public DescriptorImpl() {
            super(GenerateVersionAction.class);
        }

        public String getDisplayName() {
            return "Generate the build version badge";
        }

        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
