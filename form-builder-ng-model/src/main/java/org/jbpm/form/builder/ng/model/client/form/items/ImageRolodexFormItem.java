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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.form.builder.ng.model.client.CommonGlobals;
import org.jbpm.form.builder.ng.model.client.FormBuilderException;
import org.jbpm.form.builder.ng.model.client.effect.FBFormEffect;
import org.jbpm.form.builder.ng.model.client.form.FBFormItem;
import org.jbpm.form.builder.ng.model.client.form.OptionsFormItem;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;
import org.jbpm.form.builder.ng.model.client.resources.FormBuilderResources;
import org.jbpm.form.builder.ng.model.common.panels.ImageRolodexPanel;
import org.jbpm.form.builder.ng.model.shared.api.FormBuilderDTO;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.ClippedImagePrototype;
import com.gwtent.reflection.client.Reflectable;
import com.yesmail.gwt.rolodex.client.RolodexCard;

@Reflectable
public class ImageRolodexFormItem extends OptionsFormItem { 

    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();
    private final ImageRolodexPanel panel = new ImageRolodexPanel(FormBuilderResources.INSTANCE.defaultImage(), 400);
    
    private boolean animated = true;
    private int selectedIndex = 0;
    private List<String> urls = new ArrayList<String>();
    private String cssClassName = null;
    
    public ImageRolodexFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public ImageRolodexFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        panel.setSize("400px", "180px");
        setSize("400px", "180px");
        add(panel);
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("width", getWidth());
        map.put("height", getHeight());
        map.put("animated", this.animated);
        map.put("selectedIndex", this.selectedIndex + 1);
        map.put("cssClassName", this.cssClassName);
        return map;
    }

    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        setWidth(extractString(asPropertiesMap.get("width")));
        setHeight(extractString(asPropertiesMap.get("height")));
        this.animated = extractBoolean(asPropertiesMap.get("animated"));
        Integer ind = extractInt(asPropertiesMap.get("selectedIndex"));
        this.selectedIndex = ind == null  || ind <= 0 ? 0 : ind - 1;
        this.cssClassName = extractString(asPropertiesMap.get("cssClassName"));
        
        populate(this.panel);
    }
    
    private void populate(ImageRolodexPanel panel) {
        panel.setSize(getWidth(), getHeight());
        if (urls != null) {
            for (String url : urls) {
                RolodexCard card = createCard(url);
                panel.add(card);
            }
        }
        panel.setSelectedCard(panel.get(this.selectedIndex));
        panel.setAnimated(this.animated);
        panel.setStyleName(this.cssClassName);
    }
    
    private RolodexCard createCard(String url) {
    	SafeUri surl = UriUtils.fromString(url);
        ClippedImagePrototype expanded = new ClippedImagePrototype(surl, 0, 0, getOffsetWidth() / 2, getOffsetHeight());
        ClippedImagePrototype collapseLeft = new ClippedImagePrototype(surl, 0, 0, getOffsetWidth() / 4, getOffsetHeight() / 2);
        ClippedImagePrototype collapseRight = new ClippedImagePrototype(surl, 0, 0, getOffsetWidth() / 4, getOffsetHeight() / 2);
        int expandedWidth = getOffsetWidth() / 2;
        int collapsedWidth = getOffsetWidth() / 4;
        int heightOffset = getOffsetHeight() / 4;
        RolodexCard card = new RolodexCard(expanded, collapseLeft, collapseRight, expandedWidth, collapsedWidth, heightOffset);
        return card;
    }

    @Override
    public FormBuilderDTO getRepresentation() {
        FormBuilderDTO dto = super.getRepresentation();
        dto.setBoolean("animated", this.animated);
        List<Object> urlList = new ArrayList<Object>(this.urls);
        dto.setList("urls", urlList);
        dto.setInteger("selectedIndex", this.selectedIndex);
        return dto;
    }

    @Override
    public void populate(FormBuilderDTO dto) throws FormBuilderException {
        if (!dto.getClassName().endsWith("ImageRolodexRepresentation")) {
            throw new FormBuilderException(i18n.RepNotOfType(dto.getClassName(), "ImageRolodexRepresentation"));
        }
        super.populate(dto);
        this.selectedIndex = dto.getInteger("selectedIndex");
        this.animated = dto.getBoolean("animated");
        List<Object> list = dto.getList("urls");
        this.urls.clear();
        for (Object objUrl : list) {
        	this.urls.add(String.valueOf(objUrl));
        }
        populate(this.panel);
    }
    
    @Override
    public FBFormItem cloneItem() {
        ImageRolodexFormItem clone = super.cloneItem(new ImageRolodexFormItem());
        clone.animated = this.animated;
        clone.cssClassName = this.cssClassName;
        clone.selectedIndex = this.selectedIndex;
        clone.urls = new ArrayList<String>(this.urls);
        clone.populate(clone.panel);
        return clone;
    }

    @Override
    public Widget cloneDisplay(Map<String, Object> formData) {
        Object input = getInputValue(formData);
        ImageRolodexPanel panel = ((ImageRolodexFormItem) cloneItem()).panel;
        if (input != null) {
            if (input.getClass().isArray()) {
                Object[] arr = (Object[]) input;
                for (Object obj : arr) {
                    panel.add(createCard(obj.toString()));
                }
            } else if (input instanceof Collection) {
                Collection<?> col = (Collection<?>) input;
                for (Object obj : col) {
                    panel.add(createCard(obj.toString()));
                }
            }
        }
        return panel;
    }

    @Override
    public void setSize(String width, String height) {
        super.setSize(width, height);
        if (width != null && width.endsWith("px") && height != null && height.endsWith("px")) {
            //when size changes, you should resize all cards
            List<RolodexCard> cards = panel.getCards();
            panel.clear();
            for (RolodexCard card : cards) {
                panel.add(createCard(card.getExpandedImagePrototype().createImage().getUrl()));
            }
        }
    }
    
    @Override
    public void addItem(String index, String url) {
        urls.add(url);
        panel.add(createCard(url));
    }

    @Override
    public void deleteItem(String index) {
        urls.remove(index);
        if (index != null && !"".equals(index)) {
            Integer i = Integer.valueOf(index);
            RolodexCard card = panel.get(i - 1);
            panel.remove(card);
        }
    }

    @Override
    public Map<String, String> getItems() {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < panel.size(); i++) {
            RolodexCard card = panel.get(i);
            map.put(String.valueOf(i), card.getExpandedImagePrototype().createImage().getUrl());
        }
        return map;
    }
}
