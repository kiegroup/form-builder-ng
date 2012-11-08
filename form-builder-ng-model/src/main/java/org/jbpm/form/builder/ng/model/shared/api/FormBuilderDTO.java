package org.jbpm.form.builder.ng.model.shared.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.gwtent.reflection.client.Reflectable;

@Portable
@Reflectable
public class FormBuilderDTO {

	private Map<String, Object> parameters = new HashMap<String, Object>();

	public FormBuilderDTO() {
	}

	public FormBuilderDTO(Map<String, Object> parameters) {
		this();
		this.parameters = parameters;
	}
	
	public String getString(String key) {
		Object obj = parameters.get(key);
		return (obj == null) ? null : String.valueOf(obj);
	}
	
	public void setString(String key, String value) {
		this.parameters.put(key, value);
	}
	
	public List<Object> getList(String key) {
		Object obj = parameters.get(key);
		if (obj == null) {
			return null;
		}
		if (!(obj instanceof Map)) {
			throw new IllegalArgumentException("parameter['"+key+"'] should be a map");
		}
		Map<String, Object> subMap = (Map<String, Object>) obj;
		List<Object> retval = new ArrayList<Object>(subMap.size());
		List<String> keys = new ArrayList<String>(subMap.keySet());
		Collections.sort(keys, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
			}
		});
		for (String subKey : keys) {
			retval.add(subMap.get(subKey));
		}
		return retval;
	}

	public void setList(String key, List<Object> value) {
		Map<String, Object> subMap = new HashMap<String, Object>();
		int index = 0;
		for (Object item : value) {
			subMap.put(String.valueOf(index), item);
			index++;
		}
	}

	public List<FormBuilderDTO> getListOfDtos(String key) {
		List<Object> objs = getList(key);
		if (objs == null) {
			return null;
		}
		List<FormBuilderDTO> retval = new ArrayList<FormBuilderDTO>(objs.size());
		for (Object obj : objs) {
			if (obj == null) {
				retval.add(null);
			} else if (obj instanceof Map) {
				Map<String, Object> item = (Map<String, Object>) obj;
				retval.add(new FormBuilderDTO(item));
			} else {
				throw new IllegalArgumentException("parameter['"+key+"'] subitems should be a map but it is of type " + obj.getClass().getName());
			}
		}
		return retval;
	}

	public Map<String, Object> getMap(String key) {
		Object obj = parameters.get(key);
		if (obj == null) {
			return null;
		}
		if (!(obj instanceof Map)) {
			throw new IllegalArgumentException("parameter['"+key+"'] should be a map");
		}
		return (Map<String, Object>) obj;
	}

	public void setMap(String key, Map<String, Object> value) {
		parameters.put(key, value);
	}

	public FormBuilderDTO getSubDto(String key) {
		return new FormBuilderDTO(getMap(key));
	}
	
	public String getClassName() {
		return getString("@className");
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setInteger(String key, Integer value) {
		this.parameters.put(key, value == null ? null : String.valueOf(value));
	}
	
	public Integer getInteger(String key) {
		String val = getString(key);
		return val == null ? null : Integer.valueOf(val);
	}

	public void setBoolean(String key, Boolean value) {
		this.parameters.put(key, value == null ? null : String.valueOf(value));
	}
	
	public Boolean getBoolean(String key) {
		String val = getString(key);
		return val == null ? null : Boolean.valueOf(val);
	}

	public void setLong(String key, Long value) {
		this.parameters.put(key, value == null ? null : String.valueOf(value));
	}
	
	public Long getLong(String key) {
		String val = getString(key);
		return val == null ? null : Long.valueOf(getString(key));
	}

	public void setDouble(String key, Double value) {
		this.parameters.put(key, value == null ? null : String.valueOf(value));
	}
	
	public Double getDouble(String key) {
		String val = getString(key);
		return val == null ? null : Double.valueOf(getString(key));
	}
	
	public void setMapOfStrings(String key, Map<String, String> value) {
		Map<String, Object> transValue = new HashMap<String, Object>();
		for (Map.Entry<String, String> entry : value.entrySet()) {
			transValue.put(entry.getKey(), entry.getValue());
		}
		this.parameters.put(key, transValue);
	}

	public Map<String, String> getMapOfStrings(String key) {
		Map<String, Object> value = getMap(key);
		Map<String, String> transValue = new HashMap<String, String>();
		for (Map.Entry<String, Object> entry : value.entrySet()) {
			transValue.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		return transValue;
	}

}
