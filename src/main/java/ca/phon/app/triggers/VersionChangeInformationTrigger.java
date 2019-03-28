package ca.phon.app.triggers;

import ca.phon.app.VersionTrigger;
import ca.phon.app.log.LogUtil;
import ca.phon.plugin.IPluginExtensionFactory;
import ca.phon.plugin.IPluginExtensionPoint;

/**
 * Print a message when Phon version changes.
 *
 */
public class VersionChangeInformationTrigger implements VersionTrigger, IPluginExtensionPoint<VersionTrigger> {

	@Override
	public Class<?> getExtensionType() {
		return VersionTrigger.class;
	}

	@Override
	public IPluginExtensionFactory<VersionTrigger> getFactory() {
		return (args) -> this;
	}

	@Override
	public void versionChanged(String prevVersion, String currentVersion) {
		LogUtil.info("Phon version has changed, last executed version of Phon was " + prevVersion);
	}

}
