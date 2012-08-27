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
package org.jbpm.form.builder.ng.client.view.palette;


import com.allen_sauer.gwt.dnd.client.PickupDragController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import org.jbpm.form.builder.ng.model.client.CommonGlobals;
import org.jbpm.form.builder.ng.model.client.menu.FBMenuItem;
import org.jbpm.form.builder.ng.model.common.reflect.ReflectionHelper;
import org.jbpm.form.builder.ng.model.shared.menu.MenuItemDescription;
import org.jbpm.form.builder.ng.shared.events.PaletteItemAddedEvent;

/**
 * Menu presenter. Handles the adding and
 * removing of items from the view when
 * notified, either from the current user
 * or from the server.
 */
public class PalettePresenter {

    
    private PaletteView view;
    private PickupDragController dragController;
    
    public PalettePresenter(PaletteView menuView) {
        
        this.view = menuView;
        this.dragController = CommonGlobals.getInstance().getDragController();
        //this.view.startDropController(this.dragController);

        
    }
    
    
}
