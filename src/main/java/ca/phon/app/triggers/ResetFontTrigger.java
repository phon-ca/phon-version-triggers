package ca.phon.app.triggers;

import ca.phon.app.VersionTrigger;
import ca.phon.app.log.LogUtil;
import ca.phon.plugin.IPluginExtensionFactory;
import ca.phon.plugin.IPluginExtensionPoint;
import ca.phon.ui.fonts.FontPreferences;
import ca.phon.util.PrefHelper;

public class ResetFontTrigger implements VersionTrigger, IPluginExtensionPoint<VersionTrigger> {

	private final static String PREVIOUS_VERSION_CHECK = "<3.2.0";

	public final static String RESET_FONT_PROP = ResetFontTrigger.class.getName() + ".tempResetFlag";


	@Override
	public void versionChanged(String prevVersion, String currentVersion) {
		if(!PrefHelper.getBoolean(RESET_FONT_PROP, false)) {
			LogUtil.info("Resetting all font preferences");
			FontPreferences.resetAll();
			PrefHelper.getUserPreferences().put(RESET_FONT_PROP, true);
		}
	}

	@Override
	public Class<?> getExtensionType() {
		return VersionTrigger.class;
	}

	@Override
	public IPluginExtensionFactory<VersionTrigger> getFactory() {
		return (args) -> this;
	}
}
