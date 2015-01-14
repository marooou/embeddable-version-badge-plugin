package org.jenkinsci.plugins.versionbadge;

import hudson.model.AbstractProject;
import hudson.model.Action;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.HttpResponse;

/**
* @author Kohsuke Kawaguchi
*/
public class BadgeAction implements Action {
    private final BadgeActionFactory factory;
    public final AbstractProject project;

    public BadgeAction(BadgeActionFactory factory, AbstractProject project) {
        this.factory = factory;
        this.project = project;
    }

    public String getIconFileName() {
        return Jenkins.RESOURCE_PATH+"/plugin/embeddable-version-badge/images/24x24/badge-silver-icon.png";
    }

    public String getDisplayName() {
        return Messages.BadgeAction_DisplayName();
    }

    public String getUrlName() {
        return "versionBadge";
    }

    /**
     * Serves the badge image.
     */
    public HttpResponse doIcon() {
        return factory.getImage(project);
    }
}
