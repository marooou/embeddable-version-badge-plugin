package org.jenkinsci.plugins.versionbadge;

import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.HttpResponse;

public class RunBadgeAction implements Action {
    private final RunBadgeActionFactory factory;
    public final Run run;
    public final AbstractProject project;

    public RunBadgeAction(RunBadgeActionFactory factory, Run run) {
        this.factory = factory;
        this.run = run;
        this.project = (AbstractProject)run.getParent();
    }

    public String getIconFileName() {
        return Jenkins.RESOURCE_PATH+"/plugin/embeddable-version-badge/images/24x24/badge-silver-icon.png";
    }

    public String getDisplayName() {
        return Messages.RunBadgeAction_DisplayName();
    }

    public String getUrlName() {
        return "versionBadge";
    }

    /**
     * Serves the badge image.
     */
    public HttpResponse doIcon() {
        return factory.getImage(run);
    }
}
