package ca.phon.app.triggers;

import ca.phon.app.prefs.EditorPrefsPanel;
import ca.phon.app.prefs.PreferencesEP;
import ca.phon.app.welcome.WelcomeWindow;
import ca.phon.extensions.Extension;
import ca.phon.extensions.ExtensionProvider;
import ca.phon.extensions.IExtendable;
import ca.phon.plugin.PluginAction;
import ca.phon.ui.MultiActionButton;
import ca.phon.ui.fonts.FontPreferences;
import ca.phon.util.PrefHelper;
import ca.phon.util.icons.IconManager;
import ca.phon.util.icons.IconSize;

import javax.swing.*;
import java.awt.*;

@Extension(WelcomeWindow.class)
public class ResetFontsExtensionProvider implements ExtensionProvider {

	public final static String SHOW_RESET_PROP = ResetFontsExtensionProvider.class.getName() + ".showResetButton";

	private MultiActionButton resetButton;

	private WelcomeWindow welcomeWindow = null;

	@Override
	public void installExtension(IExtendable obj) {
		welcomeWindow = WelcomeWindow.class.cast(obj);

		if(PrefHelper.getBoolean(SHOW_RESET_PROP, false)) {
			showResetButton();
			PrefHelper.getUserPreferences().putBoolean(SHOW_RESET_PROP, false);
		}
	}

	private void showResetButton() {
		if(resetButton == null) {
			resetButton = new MultiActionButton();

			ImageIcon icn = IconManager.getInstance().getIcon("apps/preferences-desktop-font", IconSize.SMALL);
			resetButton.getTopLabel().setIcon(icn);
			resetButton.getTopLabel().setFont(FontPreferences.getTitleFont());
			resetButton.getTopLabel().setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
			resetButton.setBackground(new Color(255, 255, 200));
			resetButton.getTopLabel().setText("Font Preferences Reset");
			resetButton.getBottomLabel().setText("<p>Font preferences have been reset, click here to review changes.</p>");

			final PluginAction pluginAction = new PluginAction(PreferencesEP.EP_NAME);
			pluginAction.putArg("prefpanel", "Fonts");
			pluginAction.putValue(PluginAction.NAME, "Font preferences");
			pluginAction.putValue(PluginAction.SHORT_DESCRIPTION, "Show font preferences");
			pluginAction.putValue(PluginAction.LARGE_ICON_KEY, icn);
			pluginAction.putValue(PluginAction.SMALL_ICON, icn);
			resetButton.setDefaultAction(pluginAction);

			resetButton.setDisplayDefaultAction(false);
		}

		welcomeWindow.getActionList().add(resetButton);
		welcomeWindow.getActionList().revalidate();
	}

	public void showContextMenu() {

	}

}
