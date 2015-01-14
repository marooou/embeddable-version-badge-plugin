package org.jenkinsci.plugins.versionbadge;

import hudson.model.Run;

import java.io.*;

/**
 * Created by Marek on 2015-01-14.
 */
public class BuildVersion {

    private final Run run;
    private String version;

    public BuildVersion(Run run) {
        this.run = run;
    }

    public void load() throws IOException {
        this.version = FileUtils.readTextFile(this.getVersionFile());
    }

    public void save() throws IOException {
        FileUtils.writeTextFile(this.getVersionFile(), this.getVersion());
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private File getVersionFile() {
        return new File(this.run.getRootDir(), "version");
    }
}
