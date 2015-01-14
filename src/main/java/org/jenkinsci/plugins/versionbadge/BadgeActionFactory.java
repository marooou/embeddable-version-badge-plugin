package org.jenkinsci.plugins.versionbadge;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class BadgeActionFactory extends TransientProjectActionFactory {

    private final ImageResolver iconResolver;
    
    public BadgeActionFactory() throws IOException {
        iconResolver = new ImageResolver();
    }

    @Override
    public Collection<? extends Action> createFor(AbstractProject target) {
        return Collections.singleton(new BadgeAction(this,target));
    }

    public VersionImage getImage(AbstractProject project) {
        return iconResolver.getImage(project.getLastBuild());
    }

}