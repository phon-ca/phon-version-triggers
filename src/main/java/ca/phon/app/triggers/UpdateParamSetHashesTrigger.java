/*
 * Copyright (C) 2012-2018 Gregory Hedlund & Yvan Rose
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.phon.app.triggers;

import java.io.File;
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
	
	private final static String PREVIOUS_VERSION_CHECK = "<3.1.0";

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
		
		if(pv.check(PREVIOUS_VERSION_CHECK)) {
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
					File historyFile = QueryHistoryManager.queryHistoryFile(queryScript);
					if(historyFile.exists()) {
						QueryHistoryManager queryHistory = QueryHistoryManager.newInstance(queryScript);
						if(queryHistory.size() > 0) {
							LogUtil.info("Fixing hashes for query " + queryScript.getExtension(QueryName.class).getName());
							queryHistory.fixHashes(queryScript);
							QueryHistoryManager.save(queryHistory, queryScript);
						}
					}
				} catch (IOException | PhonScriptException e) {
					LogUtil.warning(e);
				}
			}
		}
	}
	
}
