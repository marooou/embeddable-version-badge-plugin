/*
* The MIT License
*
* Copyright 2013 Kohsuke Kawaguchi, Dominik Bartholdi
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package org.jenkinsci.plugins.badge;

import hudson.model.BallColor;
import hudson.model.AbstractProject;
import hudson.model.Run;

import java.io.IOException;

public class ImageResolver {
    
    private final StatusImage activeImage;
    private final StatusImage unknownImage;
    
    public ImageResolver() throws IOException {
        activeImage = new StatusImage("version-active.svg");
        unknownImage = new StatusImage("version-unknown.svg");
    }

    public StatusImage getImage(Run run) {
        //AbstractProject project = (AbstractProject)run.getParent();
        return getBallImage(run.getIconColor());
    }

    public StatusImage getImage(AbstractProject project) {
        BallColor color = project.getIconColor();
        return getBallImage(color);
    }

    public StatusImage getBallImage(BallColor color) {
        if (!color.isAnimated() || color == BallColor.BLUE || color == BallColor.YELLOW) {
            return activeImage;
        }
        return unknownImage;
    }

}
