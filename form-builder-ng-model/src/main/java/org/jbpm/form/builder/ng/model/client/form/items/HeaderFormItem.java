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
import org.jbpm.form.builder.ng.model.client.form.FBInplaceEditor;
import org.jbpm.form.builder.ng.model.client.form.I18NFormItem;
import org.jbpm.form.builder.ng.model.shared.api.FormBuilderDTO;
import org.jbpm.form.builder.ng.model.client.form.I18NUtils;
import org.jbpm.form.builder.ng.model.client.form.editors.HeaderInplaceEditor;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtent.reflection.client.Reflectable;
import org.jbpm.form.builder.ng.model.client.CommonGlobals;

/**
 * UI form item. Represents a header or title
 */
@Reflectable
public class HeaderFormItem extends FBFormItem implements I18NFormItem {

    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();
    private final HTML header = new HTML("<h1>" + i18n.MenuItemHeader() + "</h1>");
    private final I18NUtils utils = new I18NUtils();
    
    private String id;
    private String name;
    private String cssClassName;

    public HeaderFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public HeaderFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        add(getHeader());
        setWidth("99%");
        setHeight("30px");
        getHeader().setSize(getWidth(), getHeight());
    }
    
    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> formItemPropertiesMap = new HashMap<String, Object>();
        formItemPropertiesMap.put("id", id);
        formItemPropertiesMap.put("name", name);
        formItemPropertiesMap.put("width", getWidth());
        formItemPropertiesMap.put("height", getHeight());
        formItemPropertiesMap.put("cssClassName", cssClassName);
        return formItemPropertiesMap;
    }

    @Override
    public FBInplaceEditor createInplaceEditor() {
        return new HeaderInplaceEditor(this);
    }

    @Override
    public void saveValues(Map<String, Object> propertiesMap) {
        this.id = extractString(propertiesMap.get("id"));
        this.name = extractString(propertiesMap.get("name"));
        setWidth(extractString(propertiesMap.get("width")));
        setHeight(extractString(propertiesMap.get("height")));
        this.cssClassName = extractString(propertiesMap.get("cssClassName"));
        populate(getHeader());
    }

    private void populate(HTML html) {
        if (getWidth() != null) {
            html.setWidth(getWidth());
        }
        if (this.getHeight() != null) {
            html.setHeight(getHeight());
        }
        if (this.cssClassName != null) {
            html.setStyleName(this.cssClassName);
        }
    }
    
    public HTML getHeader() {
        return this.header;
    }
    
    public void setContent(String html) {
        getHeader().setHTML(html);
    }
    
    @Override
    public void addEffect(FBFormEffect effect) {
        super.addEffect(effect);
        effect.setWidget(this.header);
    }
    
    @Override
    public FormBuilderDTO getRepresentation() {
        FormBuilderDTO dto = super.getRepresentation();
        dto.setString("value", this.header.getText());
        dto.setString("styleClass", this.cssClassName);
        dto.setString("cssId", this.id);
        dto.setString("cssName", this.name);
        dto.setMapOfStrings("i18n", getI18nMap());
        dto.setString("format", getFormat() == null ? null : getFormat().toString());
        return dto;
    }
    
    @Override
    public void populate(FormBuilderDTO dto) throws FormBuilderException {
        if (!dto.getClassName().endsWith("HeaderRepresentation")) {
            throw new FormBuilderException(i18n.RepNotOfType(dto.getClassName(), "HeaderRepresentation"));
        }
        super.populate(dto);
        this.cssClassName = dto.getString("cssName");
        this.id = dto.getString("cssId");
        saveI18nMap(dto.getMapOfStrings("i18n"));
        if (dto.getString("value").startsWith("<h1>")) {
            setContent(dto.getString("value"));
        } else {
            setContent("<h1>" + dto.getString("value") + "</h1>");
        }
        if (dto.getString("format") != null && !"".equals(dto.getString("format"))) {
            setFormat(Format.valueOf(dto.getString("format")));
        }
    }
    
    @Override
    public FBFormItem cloneItem() {
        HeaderFormItem clone = super.cloneItem(new HeaderFormItem(getFormEffects()));
        clone.cssClassName = this.cssClassName;
        clone.id = this.id;
        clone.name = this.name;
        clone.setContent(this.header.getHTML());
        clone.saveI18nMap(getI18nMap());
        clone.setFormat(getFormat());
        clone.populate(this.header);
        return clone;
    }
    
    @Override
    public Widget cloneDisplay(Map<String, Object> data) {
        HTML html = new HTML(this.header.getHTML());
        populate(html);
        String value = (String) getInputValue(data);
        if (value != null) {
            html.setHTML("<h1>" + value + "</h1>");
        } else {
            String locale = (String) data.get(CommonGlobals.BASE_LOCALE);
            html.setHTML(this.header.getHTML());
            if (locale != null) {
                String i18nText = getI18n(locale);
                if (i18nText != null && !"".equals(i18nText)) {
                    html.setHTML("<h1>" + i18nText + "</h1>");
                }
            }
        }
        super.populateActions(html.getElement());
        return html;
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
                this.header.setHTML("<h1>" + defaultI18n + "</h1>");
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
