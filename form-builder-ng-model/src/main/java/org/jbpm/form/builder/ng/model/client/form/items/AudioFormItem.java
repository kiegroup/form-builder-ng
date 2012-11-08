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
import org.jbpm.form.builder.ng.model.client.form.HasSourceReference;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;
import org.jbpm.form.builder.ng.model.shared.api.FormBuilderDTO;

import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtent.reflection.client.Reflectable;

@Reflectable
public class AudioFormItem extends FBFormItem implements HasSourceReference {

    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();
    
    private final Audio audio = Audio.createIfSupported();
    private final Label notSupported = new Label(i18n.AudioNotSupported());
    
    private String cssClassName;
    private String id;
    private String dataType;
    private String audioUrl;
    
    public AudioFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public AudioFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        if (audio == null) {
            add(notSupported);
        } else {
            audio.setControls(true);
            add(audio);
        }
        setWidth("300px");
        setHeight("50px");
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cssClassName", this.cssClassName);
        map.put("dataType", this.dataType);
        map.put("height", this.getHeight());
        map.put("width", this.getWidth());
        map.put("audioUrl", this.audioUrl);
        map.put("id", this.id);
        return map;
    }

    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        this.cssClassName = extractString(asPropertiesMap.get("cssClassName"));
        this.setHeight(extractString(asPropertiesMap.get("height")));
        this.setWidth(extractString(asPropertiesMap.get("width")));
        this.audioUrl = extractString(asPropertiesMap.get("audioUrl"));
        this.id = extractString(asPropertiesMap.get("id"));
        this.dataType = extractString(asPropertiesMap.get("dataType"));
        populate(this.audio);
    }
    
    private void populate(Audio audio) {
        if (audio != null) {
            if (this.cssClassName != null) {
                audio.setStyleName(this.cssClassName);
            }
            if (this.getHeight() != null) {
                audio.setHeight(this.getHeight());
            }
            if (this.getWidth() != null) {
                audio.setWidth(this.getWidth());
            }
            if (this.audioUrl != null && !"".equals(this.audioUrl)) {
                audio.setSrc(this.audioUrl);
            }
            if (this.dataType != null) {
                audio.getElement().setPropertyObject("type", this.dataType);
            }
            audio.setControls(true);
        }
    }

    @Override
    public FormBuilderDTO getRepresentation() {
        FormBuilderDTO dto = super.getRepresentation();
        dto.setString("audioUrl", this.audioUrl);
        dto.setString("cssClassName", this.cssClassName);
        dto.setString("dataType", this.dataType);
        dto.setString("id", this.id);
        return dto;
    }
    
    @Override
    public void populate(FormBuilderDTO dto) throws FormBuilderException {
        if (!dto.getClassName().endsWith("AudioRepresentation")) {
            throw new FormBuilderException(i18n.RepNotOfType(dto.getClassName(), "AudioRepresentation"));
        }
        super.populate(dto);
        this.audioUrl = dto.getString("audioUrl");
        this.cssClassName = dto.getString("cssClassName");
        this.id = dto.getString("id");
        this.dataType = dto.getString("dataType");

        populate(this.audio);
    }

    @Override
    public FBFormItem cloneItem() {
        AudioFormItem clone = super.cloneItem(new AudioFormItem());
        clone.setHeight(this.getHeight());
        clone.setWidth(this.getWidth());
        clone.audioUrl = this.audioUrl;
        clone.cssClassName = this.cssClassName;
        clone.dataType = this.dataType;
        clone.id = this.id;
        clone.populate(clone.audio);
        return clone;
    }

    @Override
    public Widget cloneDisplay(Map<String, Object> formData) {
        Audio au = Audio.createIfSupported();
        if (au == null) {
            return new Label(notSupported.getText());
        }
        populate(au);
        Object input = getInputValue(formData);
        if (au != null && input != null) {
            String url = input.toString();
            au.setSrc(url);
            if (url.endsWith(".mp3")) {
                au.getElement().setPropertyString("type", "application/mp3");
            } else if (url.endsWith(".ogg")) {
                au.getElement().setPropertyString("type", "application/ogg");
            } else if (url.endsWith(".mid")) {
                au.getElement().setPropertyString("type", "application/midi");
            }
        }
        super.populateActions(au.getElement());
        return au;
    }

    @Override
    public void setSourceReference(String sourceReference) {
        this.audioUrl = sourceReference;
        if (audio != null) {
            this.audio.setSrc(sourceReference);
        }
    }

    @Override
    public String getSourceReference() {
        return this.audioUrl;
    }

    @Override
    public List<String> getAllowedTypes() {
        ArrayList<String> retval = new ArrayList<String>();
        retval.add("ogg");
        retval.add("mp3");
        retval.add("mid");
        return retval;
    }
}
