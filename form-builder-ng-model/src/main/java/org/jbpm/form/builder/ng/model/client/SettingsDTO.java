package org.jbpm.form.builder.ng.model.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsDTO {

    private Long id;
    private List<SettingsEntryDTO> entries = new ArrayList<SettingsEntryDTO>();
    private String userId;
    
    public SettingsDTO() {
    }

    public SettingsDTO(Map<String, Object> serverData) {
    	Object objUserId = serverData.remove("org.jbpm.form.builder.services.model.Settings.userId");
    	this.userId = objUserId == null ? null : String.valueOf(objUserId);
    	Object objId = serverData.remove("org.jbpm.form.builder.services.model.Settings.id");
    	this.id = objId == null ? null : Long.valueOf(String.valueOf(objId));
    	for (Map.Entry<String, Object> entry : serverData.entrySet()) {
    		if (entry.getKey().endsWith("@org.jbpm.form.builder.services.model.SettingsEntry.id")) {
    			continue;
    		}
    		String value = entry.getValue() == null ? null : String.valueOf(entry.getValue());
    		Object objEntryId = serverData.get(entry.getKey() + "@org.jbpm.form.builder.services.model.SettingsEntry.id"); 
    		Long entryId = objEntryId == null ? null : Long.valueOf(String.valueOf(objEntryId));
    		SettingsEntryDTO entryDto = new SettingsEntryDTO(entry.getKey(), entryId, value);
    		entries.add(entryDto);
    	}
    }

    public Map<String, Object> asMap() {
    	Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("org.jbpm.form.builder.services.model.Settings.userId", this.userId);
		dataMap.put("org.jbpm.form.builder.services.model.Settings.id", this.id == null ? null : String.valueOf(this.id));
		for (SettingsEntryDTO entry : entries) {
			dataMap.put(entry.getKey(), entry.getValue());
			dataMap.put(entry.getKey() + "@org.jbpm.form.builder.services.model.SettingsEntry.id", 
					entry.getId() == null ? null : String.valueOf(entry.getId()));
		}
		return dataMap;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SettingsEntryDTO> getEntries() {
        return entries;
    }
    
    public SettingsEntryDTO getEntry(String key) {
        for(SettingsEntryDTO entry : entries){
            if(entry.getKey().equals(key)){
                return entry;
            }
        }
        return null;
    }

    public void setEntries(List<SettingsEntryDTO> entries) {
        this.entries = entries;
    }

    public void addEntry(SettingsEntryDTO entry){
        this.entries.add(entry);
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Settings{" + "id=" + id + ", userId=" + userId + ", entries=" + entries +  '}';
    }

}
