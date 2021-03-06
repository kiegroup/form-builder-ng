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

import org.jbpm.form.builder.ng.model.client.FormBuilderException;
import org.jbpm.form.builder.ng.model.client.effect.FBFormEffect;
import org.jbpm.form.builder.ng.model.client.form.FBFormItem;
import org.jbpm.form.builder.ng.model.client.form.I18NFormItem;
import org.jbpm.form.builder.ng.model.shared.api.FormBuilderDTO;
import org.jbpm.form.builder.ng.model.client.form.I18NUtils;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.gwtent.reflection.client.Reflectable;
import org.jbpm.form.builder.ng.model.client.CommonGlobals;

/**
 * UI form item. Represents a generic button
 */
@Reflectable
public class ButtonFormItem extends FBFormItem implements I18NFormItem {

    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();
    private Button button = new Button(i18n.Button());
    private final I18NUtils utils = new I18NUtils();
    private String innerText = i18n.Button();
    private String name;
    private String id;
    private String cssStyleName;

    public ButtonFormItem() {
        this(new ArrayList<FBFormEffect>());
    }

    public ButtonFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        add(button);
        setHeight("27px");
        setWidth("100px");
        
        button.setSize(getWidth(), getHeight());
    }

    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        setHeight(extractString(asPropertiesMap.get("height")));
        setWidth(extractString(asPropertiesMap.get("width")));
        this.name = extractString(asPropertiesMap.get("name"));
        this.id = extractString(asPropertiesMap.get("id"));
        this.innerText = extractString(asPropertiesMap.get("innerText"));
        this.cssStyleName = extractString(asPropertiesMap.get("cssStyleName"));

        populate(this.button);
    }

    private void populate(Button button) {
        if (getHeight() != null) {
            button.setHeight(getHeight());
        }
        if (getWidth() != null) {
            button.setWidth(getWidth());
        }
        if (this.innerText != null) {
            button.setText(this.innerText);
        }
        if (this.cssStyleName != null) {
            button.setStyleName(this.cssStyleName);
        }
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("height", getHeight());
        map.put("width", getWidth());
        map.put("innerText", this.innerText);
        map.put("cssStyleName", this.cssStyleName);
        map.put("name", this.name);
        map.put("id", this.id);
        return map;
    }

    @Override
    public FormBuilderDTO getRepresentation() {
        FormBuilderDTO dto = super.getRepresentation();
        dto.setString("text", this.innerText);
        dto.setString("name", this.name);
        dto.setString("id", this.id);
        dto.setMapOfStrings("i18n", getI18nMap());
        dto.setString("format", getFormat() == null ? null : getFormat().toString());
        return dto;
    }

    @Override
    public void populate(FormBuilderDTO dto) throws FormBuilderException {
        if (!dto.getClassName().endsWith("ButtonRepresentation")) {
            throw new FormBuilderException(i18n.RepNotOfType(dto.getClassName(), "ButtonRepresentation"));
        }
        super.populate(dto);
        this.innerText = dto.getString("text");
        this.name = dto.getString("name");
        this.id = dto.getString("id");
        this.saveI18nMap(dto.getMapOfStrings("i18n"));
        if (dto.getString("format") != null && !"".equals(dto.getString("format"))) {
            this.setFormat(Format.valueOf(dto.getString("format")));
        }
        populate(this.button);
    }

    @Override
    public FBFormItem cloneItem() {
        ButtonFormItem clone = new ButtonFormItem(getFormEffects());
        clone.cssStyleName = this.cssStyleName;
        clone.setHeight(this.getHeight());
        clone.setWidth(this.getWidth());
        clone.id = this.id;
        clone.innerText = this.innerText;
        clone.saveI18nMap(getI18nMap());
        clone.name = this.name;
        clone.populate(clone.button);
        clone.setFormat(getFormat());
        return clone;
    }

    @Override
    public Widget cloneDisplay(final Map<String, Object> data) {
        Button bt = new Button();
        populate(bt);
        Object input = getInputValue(data);
        if (input != null) {
            bt.setText(input.toString());
        }
        if (getOutput() != null && getOutput().get("name") != null) {
            ButtonElement.as(bt.getElement()).setName(String.valueOf(getOutput().get("name")));
        }

        super.populateActions(bt.getElement());
        return bt;
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
                this.innerText = defaultI18n;
                populate(this.button);
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
;
}
