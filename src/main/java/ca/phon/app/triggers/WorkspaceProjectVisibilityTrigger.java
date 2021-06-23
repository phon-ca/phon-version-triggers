package ca.phon.app.triggers;

import ca.phon.app.VersionInfo;
import ca.phon.app.VersionTrigger;
import ca.phon.app.welcome.WelcomeWindow;
import ca.phon.app.workspace.Workspace;
import ca.phon.plugin.IPluginExtensionFactory;
import ca.phon.plugin.IPluginExtensionPoint;
import ca.phon.util.PrefHelper;

public class WorkspaceProjectVisibilityTrigger implements VersionTrigger, IPluginExtensionPoint<VersionTrigger> {

	private final static String PREVIOUS_VERSION_CHECK = "<3.3.1";

	@Override
	public void versionChanged(String prevVersion, String currentVersion) {
		VersionInfo pv = new VersionInfo(prevVersion);
		String currentValue = PrefHelper.get(WelcomeWindow.SHOW_WORKSPACE_PROJECTS, null);
		if(pv.check(PREVIOUS_VERSION_CHECK) && currentValue == null) {
			boolean showWorkspaceProjects =
					(Workspace.userWorkspaceFolder().exists() && Workspace.userWorkspace().getProjects().size() > 0);
			PrefHelper.getUserPreferences().putBoolean(WelcomeWindow.SHOW_WORKSPACE_PROJECTS, showWorkspaceProjects);
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
