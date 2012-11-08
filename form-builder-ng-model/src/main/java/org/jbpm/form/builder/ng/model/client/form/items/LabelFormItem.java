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
import org.jbpm.form.builder.ng.model.client.form.FBInplaceEditor;
import org.jbpm.form.builder.ng.model.client.form.I18NFormItem;
import org.jbpm.form.builder.ng.model.client.form.I18NUtils;
import org.jbpm.form.builder.ng.model.client.form.editors.LabelInplaceEditor;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;
import org.jbpm.form.builder.ng.model.shared.api.FormBuilderDTO;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtent.reflection.client.Reflectable;

/**
 * UI form item. Represents a label
 */
@Reflectable
public class LabelFormItem extends FBFormItem implements I18NFormItem {

    private final Label label = new Label("Label");
    
    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();
    private final I18NUtils utils = new I18NUtils();
    private String id;
    private String cssClassName;

    public LabelFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public LabelFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        setWidth("200px");
        add(getLabel());
    }
    
    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> formItemPropertiesMap = new HashMap<String, Object>();
        formItemPropertiesMap.put("id", id);
        formItemPropertiesMap.put("width", getWidth());
        formItemPropertiesMap.put("height", getHeight());
        formItemPropertiesMap.put("cssClassName", cssClassName);
        return formItemPropertiesMap;
    }

    @Override
    public FBInplaceEditor createInplaceEditor() {
        return new LabelInplaceEditor(this);
    }

    @Override
    public void saveValues(Map<String, Object> propertiesMap) {
        this.id = extractString(propertiesMap.get("id"));
        this.setWidth(extractString(propertiesMap.get("width")));
        this.setHeight(extractString(propertiesMap.get("height")));
        this.cssClassName = extractString(propertiesMap.get("cssClassName"));
        populate(getLabel());
    }

    private void populate(Label label) {
        if (this.getWidth() != null) {
            label.setWidth(this.getWidth());
        }
        if (this.getHeight() != null) {
            label.setHeight(this.getHeight());
        }
        if (this.cssClassName != null) {
            label.setStyleName(this.cssClassName);
        }
    }
    
    public Label getLabel() {
        return this.label;
    }
    
    @Override
    public void addEffect(FBFormEffect effect) {
        super.addEffect(effect);
        effect.setWidget(this.label);
    }
    
    @Override
    public FormBuilderDTO getRepresentation() {
        FormBuilderDTO dto = super.getRepresentation();
        dto.setString("value", this.label.getText());
        dto.setString("cssName", this.cssClassName);
        dto.setString("id", this.id);
        dto.setMapOfStrings("i18n", getI18nMap());
        dto.setString("format", getFormat() == null ? null : getFormat().toString());
        return dto;
    }
    
    @Override
    public void populate(FormBuilderDTO dto) throws FormBuilderException {
        if (!dto.getClassName().endsWith("LabelRepresentation")) {
            throw new FormBuilderException(i18n.RepNotOfType(dto.getClassName(), "LabelRepresentation"));
        }
        super.populate(dto);
        this.label.setText(dto.getString("value"));
        this.cssClassName = dto.getString("cssName");
        this.id = dto.getString("id");
        saveI18nMap(dto.getMapOfStrings("i18n"));
        if (dto.getString("format") != null && !"".equals(dto.getString("format"))) {
            setFormat(Format.valueOf(dto.getString("format")));
        }
        populate(this.label);
    }
    
    @Override
    public FBFormItem cloneItem() {
        LabelFormItem clone = new LabelFormItem(getFormEffects());
        clone.cssClassName = this.cssClassName;
        clone.setHeight(this.getHeight());
        clone.id = this.id;
        clone.setWidth(this.getWidth());
        clone.getLabel().setText(this.label.getText());
        clone.populate(clone.label);
        clone.saveI18nMap(getI18nMap());
        clone.setFormat(getFormat());
        return clone;
    }
    
    @Override
    public Widget cloneDisplay(Map<String, Object> data) {
        Label lb = new Label();
        populate(lb);
        String value = (String) getInputValue(data);
        if (value != null) {
            lb.setText(value);
        } else {
            String locale = (String) data.get(CommonGlobals.BASE_LOCALE);
            lb.setText(this.label.getText());
            if (locale != null) {
                String i18nText = getI18n(locale);
                if (i18nText != null && !"".equals(i18nText)) {
                    lb.setText(i18nText);
                }
            }
        }
        super.populateActions(lb.getElement());
        return lb;
    }
    
    @Override
    public boolean containsLocale(String localeName) {
        return utils.containsLocale(localeName);
    }
    
    @Override
    public String getI18n(String key) {
        return utils.getI18n(key);
    }
    
    @Override
    public Map<String, String> getI18nMap() {
        return utils.getI18nMap();
    }
    
    @Override
    public void saveI18nMap(Map<String, String> i18nMap) {
        if (i18nMap != null) {
            String defaultI18n = i18nMap.get("default");
            if (defaultI18n != null && !"".equals(defaultI18n)) {
                this.label.setText(defaultI18n);
            }
            utils.saveI18nMap(i18nMap);
        }
    }
    
    @Override
    public Format getFormat() {
        return utils.getFormat();
    }
    
    @Override
    public void setFormat(Format format) {
        utils.setFormat(format);
    }
}
