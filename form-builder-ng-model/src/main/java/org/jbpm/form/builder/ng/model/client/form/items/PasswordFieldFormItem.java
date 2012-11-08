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
package org.jbpm.form.builder.ng.model.client.form.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.form.builder.ng.model.client.CommonGlobals;
import org.jbpm.form.builder.ng.model.client.FormBuilderException;
import org.jbpm.form.builder.ng.model.client.effect.FBFormEffect;
import org.jbpm.form.builder.ng.model.client.form.FBFormItem;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;
import org.jbpm.form.builder.ng.model.shared.api.FormBuilderDTO;

import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtent.reflection.client.Reflectable;

/**
 * UI form item. Represents a password field
 */
@Reflectable
public class PasswordFieldFormItem extends FBFormItem {

    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();

    private final PasswordTextBox textBox = new PasswordTextBox();
    
    private String defaultContent = null;
    private String name = null;
    private String id = null;
    private String title = null;
    private Integer maxlength = null;

    public PasswordFieldFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public PasswordFieldFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        add(textBox);
        setWidth("100px");
        setHeight("21px");
        textBox.setSize(getWidth(), getHeight());
    }
    
    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("inputDefaultContent", this.defaultContent);
        map.put("name", this.name);
        map.put("id", this.id);
        map.put("height", getHeight());
        map.put("width", getWidth());
        map.put("maxlength", this.maxlength);
        map.put("title", this.title);
        return map;
    }

    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        this.defaultContent = extractString(asPropertiesMap.get("inputDefaultContent"));
        this.name = extractString(asPropertiesMap.get("name"));
        this.id = extractString(asPropertiesMap.get("id"));
        this.setHeight(extractString(asPropertiesMap.get("height")));
        this.setWidth(extractString(asPropertiesMap.get("width")));
        this.title = extractString(asPropertiesMap.get("title"));
        this.maxlength = extractInt(asPropertiesMap.get("maxlength"));
        
        populate(this.textBox);
    }

    private void populate(PasswordTextBox textBox) {
        if (this.defaultContent != null) {
            textBox.setValue(this.defaultContent);
        }
        if (this.name != null) {
            textBox.setName(this.name);
        }
        if (getHeight() != null) {
            textBox.setHeight(getHeight());
        }
        if (getWidth() != null) {
            textBox.setWidth(getWidth());
        }
        if (this.title != null) {
            textBox.setTitle(this.title);
        }
        if (this.maxlength != null) {
            textBox.setMaxLength(this.maxlength);
        }
    }
    
    @Override
    public FormBuilderDTO getRepresentation() {
    	FormBuilderDTO dto = super.getRepresentation();
        dto.setString("defaultValue", this.defaultContent);
        dto.setString("name", this.name);
        dto.setString("id", this.id);
        dto.setInteger("maxLength", this.maxlength);
        return dto;
    }
    
    @Override
    public void populate(FormBuilderDTO dto) throws FormBuilderException {
        if (!dto.getClassName().endsWith("PasswordFieldRepresentation")) {
            throw new FormBuilderException(i18n.RepNotOfType(dto.getClassName(), "PasswordFieldRepresentation"));
        }
        super.populate(dto);
        this.defaultContent = dto.getString("defaultValue");
        this.name = dto.getString("name");
        this.id = dto.getString("id");
        this.maxlength = dto.getInteger("maxLength");
        populate(this.textBox);
    }
    
    @Override
    public FBFormItem cloneItem() {
        PasswordFieldFormItem clone = new PasswordFieldFormItem(getFormEffects());
        clone.defaultContent = this.defaultContent;
        clone.setHeight(this.getHeight());
        clone.id = this.id;
        clone.maxlength = this.maxlength;
        clone.name = this.name;
        clone.title = this.title;
        clone.setWidth(this.getWidth());
        clone.populate(clone.textBox);
        return clone;
    }
    
    @Override
    public Widget cloneDisplay(Map<String, Object> data) {
        PasswordTextBox pb = new PasswordTextBox();
        populate(pb);
        Object input = getInputValue(data);
        if (input != null) {
            pb.setValue(input.toString());
        }
        if (getOutput() != null && getOutput().get("name") != null) {
            pb.setName(String.valueOf(getOutput().get("name")));
        }
        super.populateActions(pb.getElement());
        return pb;
    }
}
