/*
 * Copyright 2011 JBoss Inc 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.form.builder.ng.model.client.validation;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.form.builder.ng.model.client.FormBuilderException;
import org.jbpm.form.builder.ng.model.common.reflect.ReflectionHelper;
import org.jbpm.form.builder.ng.model.shared.api.RepresentationFactory;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base UI component for a validation class
 */
public abstract class FBValidationItem {

    private final Map<String, HasValue<String>> propertiesMap = new HashMap<String, HasValue<String>>();
    
    public FBValidationItem() {
    }
    
    public Map<String, HasValue<String>> getPropertiesMap() {
        return propertiesMap;
    }
    
    public void populatePropertiesMap(Map<String, HasValue<String>> map) {
        propertiesMap.putAll(map);
    }
    
    public Map<String, Object> getDataMap() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (Map.Entry<String, HasValue<String>> entry : propertiesMap.entrySet()) {
            dataMap.put(entry.getKey(), entry.getValue().getValue());
        }
        return dataMap;
    }
    
    public abstract String getName();

    public abstract Widget createDisplay();

    public abstract FBValidationItem cloneItem();
    
    public abstract void setDataMap(Map<String, Object> dataMap) throws FormBuilderException;

    public static FBValidationItem createValidation(Map<String, Object> validationMap) throws FormBuilderException {
        try {
            String repClassName = (String) validationMap.get("@className");
            String className = RepresentationFactory.getItemClassName(repClassName);
            Object obj = ReflectionHelper.newInstance(className);
            FBValidationItem item = (FBValidationItem) obj;
            item.setDataMap(validationMap);
            return item;
        } catch (Exception e) {
            throw new FormBuilderException(e);
        }
    }

	public boolean canValidateOnClient() {
		return false; //TODO override to validate something on the client-side
	}

	public boolean isValid(Object object) {
		return true; //By default all client-side validations (unless specified) are valid
	}
	
}
