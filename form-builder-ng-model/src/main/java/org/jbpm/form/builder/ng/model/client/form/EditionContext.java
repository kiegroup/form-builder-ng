package org.jbpm.form.builder.ng.model.client.form;

import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class EditionContext {

	private Map<String, Object> map;
	private String id;
	
	public EditionContext() {
	}
	
	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
