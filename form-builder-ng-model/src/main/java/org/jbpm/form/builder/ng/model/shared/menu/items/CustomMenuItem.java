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
package org.jbpm.form.builder.ng.model.shared.menu.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.form.builder.ng.model.client.CommonGlobals;
import org.jbpm.form.builder.ng.model.client.FormBuilderException;
import org.jbpm.form.builder.ng.model.client.bus.FormItemSelectionEvent;
import org.jbpm.form.builder.ng.model.client.bus.UndoableEvent;
import org.jbpm.form.builder.ng.model.client.bus.UndoableHandler;
import org.jbpm.form.builder.ng.model.client.effect.FBFormEffect;
import org.jbpm.form.builder.ng.model.client.form.FBFormItem;
import org.jbpm.form.builder.ng.model.client.menu.FBMenuItem;
import org.jbpm.form.builder.ng.model.client.resources.FormBuilderResources;
import org.jbpm.form.builder.ng.model.common.panels.CommandPopupPanel;
import org.jbpm.form.builder.ng.model.shared.api.FormItemRepresentation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.gwtent.reflection.client.Reflectable;

/**
 * This class is used to store a POJO representation of
 * a complex item that can be extrapolated from other simpler
 * items, and allow the user to use them by having it as a custom
 * menu option
 */
@Reflectable
public class CustomMenuItem extends FBMenuItem {

    private String optionName;
    private FormItemRepresentation representation;
    private String groupName;
    private String iconUrlAsString;

    
    public CustomMenuItem() {
        //needs a default constructor for reconstruction from xml in GWT
        this(null, null, new ArrayList<FBFormEffect>(), null);
    }
    
    public CustomMenuItem(FormItemRepresentation representation, String optionName, List<FBFormEffect> formEffects, String groupName) {
        super(formEffects);
        this.representation = representation;
        this.optionName = optionName;
        this.groupName = groupName;
        sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU);
        repaint();
    }
    
    //right click handling for optional menu

    @Override
    public void onBrowserEvent(Event event) {
      event.stopPropagation();
      event.preventDefault();
      switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEUP:
          if (DOM.eventGetButton(event) == Event.BUTTON_LEFT) {
              ClickEvent evt = new ClickEvent() {
                  @Override
                 public Object getSource() {
                     return CustomMenuItem.this;
                 } 
              };
              evt.setNativeEvent(event);
              fireEvent(evt);
              super.onBrowserEvent(event);
          } else if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
              final CommandPopupPanel removePanel = new CommandPopupPanel(true);
              MenuItem removeItem = new MenuItem(CommonGlobals.getInstance().getI18n().RemoveMenuItem(), new Command() {
                  @Override
                  public void execute() {
                      Map<String, Object> dataSnapshot = new HashMap<String, Object>();
                      dataSnapshot.put("menuItem", CustomMenuItem.this);
                      dataSnapshot.put("groupName", groupName);
                      final EventBus bus = CommonGlobals.getInstance().getEventBus();
                      bus.fireEvent(new UndoableEvent(dataSnapshot, new UndoableHandler() {
                          @Override
                          public void onEvent(UndoableEvent event) { }
                          @Override
                          public void undoAction(UndoableEvent event) {
                              FBMenuItem item = (FBMenuItem) event.getData("menuItem");
                              String group = (String) event.getData("groupName");
                             // bus.fireEvent(new MenuItemAddedEvent(item, group));
                          }
                          @Override
                          public void doAction(UndoableEvent event) {
                              FBMenuItem item = (FBMenuItem) event.getData("menuItem");
                              String group = (String) event.getData("groupName");
                              //bus.fireEvent(new MenuItemRemoveEvent(item, group));
                          }
                      }));
                      removePanel.hide();
                  }
              });
              removePanel.addItem(removeItem);
              removePanel.setPopupPosition(event.getClientX(), event.getClientY());
              removePanel.show();
          }
          break;
        case Event.ONDBLCLICK:
          break;
        case Event.ONCONTEXTMENU:
          break;
        default:
          break; // Do nothing
      }//end switch
    }

    public String getOptionName() {
        return optionName;
    }
    
    public FormItemRepresentation getRepresentation() {
        return representation;
    }
    
    public void setRepresentation(FormItemRepresentation representation) {
        this.representation = representation;
    }
    
    public void setOptionName(String optionName) {
        this.optionName = optionName;
        repaint();
    }
    
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    protected String getIconUrlAsString() {
    	return iconUrlAsString;
    }
    
    public void setIconUrlAsString(String iconUrlAsString) {
		this.iconUrlAsString = iconUrlAsString;
	}

    @Override
    protected ImageResource getIconUrl() {
   		return FormBuilderResources.INSTANCE.questionIcon();
    }

    @Override
    public Label getDescription() {
        return new Label(optionName);
    }

    @Override
    public FBMenuItem cloneWidget() {
    	CustomMenuItem customItem = new CustomMenuItem(representation, optionName, new ArrayList<FBFormEffect>(getFormEffects()), groupName);
    	customItem.setIconUrlAsString(this.iconUrlAsString);
    	customItem.repaint();
        return clone(customItem);
    }

    @Override
    public void addEffect(FBFormEffect effect) {
        super.addEffect(effect);
    }
    
    @Override
    public FBFormItem buildWidget() {
        try {
            FBFormItem item = FBFormItem.createItem(representation);
            return build(item);
        } catch (FormBuilderException e) {
            return new ErrorMenuItem(e.getLocalizedMessage()).buildWidget();
        }
    }
    
    @Override
    public String getItemId() {
        return groupName + ":" + optionName;
    }
}