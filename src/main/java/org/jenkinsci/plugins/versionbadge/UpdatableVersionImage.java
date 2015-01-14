package org.jenkinsci.plugins.versionbadge;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Marek on 2015-01-14.
 */
public class UpdatableVersionImage extends VersionImage {

    private static final String DEFAULT_VERSION_LABEL = "ver";
    private byte[] originalPayload;

    UpdatableVersionImage(String fileName) throws IOException {
        super(fileName);
    }

    public void updateVersion(BuildVersion version) {
        this.updateVersion(null, version);
    }

    public void updateVersion(String label, BuildVersion version) {
        if (originalPayload == null) {
            originalPayload = this.getPayload();
        }
        if (label == null || label.equals("")) {
            label = DEFAULT_VERSION_LABEL;
        }
        String currentVer = null;
        try {
            currentVer = new String(originalPayload, "UTF-8");
            currentVer = currentVer.replaceAll("\\$version\\$", version.getVersion())
                                   .replaceAll("\\$label\\$", label);
            this.setPayload(currentVer.getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
