package org.jbpm.form.builder.ng.model.client;

public class SettingsEntryDTO {

    private Long id;
    private String key;
    private String value;
    
    public SettingsEntryDTO(String key, String value) {
    	this.key = key;
        this.value = value;
    }
    
    public SettingsEntryDTO(String key, Long id, String value) {
        this.key = key;
        this.id = id;
        this.value = value;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SettingEntry{" + "id=" + id + ", key=" + key + ", value=" + value + "}";
    }
}
