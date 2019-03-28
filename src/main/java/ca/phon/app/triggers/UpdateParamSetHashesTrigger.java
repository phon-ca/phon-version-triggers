package ca.phon.app.triggers;

import java.io.IOException;
import java.util.Iterator;

import ca.phon.app.VersionInfo;
import ca.phon.app.VersionTrigger;
import ca.phon.app.log.LogUtil;
import ca.phon.plugin.IPluginExtensionFactory;
import ca.phon.plugin.IPluginExtensionPoint;
import ca.phon.query.history.QueryHistoryManager;
import ca.phon.query.script.QueryName;
import ca.phon.query.script.QueryScript;
import ca.phon.query.script.QueryScriptLibrary;
import ca.phon.script.PhonScriptException;
import ca.phon.util.resources.ResourceLoader;

/**
 * Updates saved query history param set hashes based on current 
 * script implementations.
 * 
 * This operation needs to be perfomed if there is a change in 
 * parameters (param name, number, list, etc.) for the stock
 * query forms.
 */
public class UpdateParamSetHashesTrigger implements VersionTrigger, IPluginExtensionPoint<VersionTrigger> {

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
		VersionInfo pv = new VersionInfo(prevVersion);
		
		// execute this plug-in if the previously executed version of
		// phon was <3.0.4
		if(pv.check("<3.0.4")) {
			updateQueryHistoryHashes();
		}
	}
	
	private void updateQueryHistoryHashes() {
		QueryScriptLibrary scriptLibrary = new QueryScriptLibrary();
		
		ResourceLoader<QueryScript> stockScriptLoader = scriptLibrary.stockScriptFiles();
		Iterator<QueryScript> scriptItr = stockScriptLoader.iterator();
		
		while(scriptItr.hasNext()) {
			QueryScript queryScript = scriptItr.next();
			if(queryScript != null) {
				try {
					QueryHistoryManager queryHistory = QueryHistoryManager.newInstance(queryScript);
					if(queryHistory.size() > 0) {
						LogUtil.info("Fixing hashes for query " + queryScript.getExtension(QueryName.class).getName());
						queryHistory.fixHashes(queryScript);
						QueryHistoryManager.save(queryHistory, queryScript);
					}
				} catch (IOException | PhonScriptException e) {
					LogUtil.warning(e);
				}
			}
		}
	}
	
}
