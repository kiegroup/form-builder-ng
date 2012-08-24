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
package org.jbpm.form.builder.ng.client.view;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.jbpm.form.builder.ng.client.command.DisposeDropController;
import org.jbpm.form.builder.ng.client.view.palette.PalettePresenter;
import org.jbpm.form.builder.ng.client.view.palette.PaletteView;
import org.jbpm.form.builder.ng.model.client.CommonGlobals;
import org.jbpm.form.builder.ng.model.client.menu.FBMenuItem;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;
import org.jbpm.form.builder.ng.model.common.reflect.ReflectionHelper;
import org.jbpm.form.builder.ng.model.shared.api.RepresentationFactory;
import org.jbpm.form.builder.ng.model.shared.menu.MenuItemDescription;
import org.jbpm.form.builder.ng.shared.FormServiceEntryPoint;
import org.jbpm.form.builder.ng.shared.events.MenuItemAddedEvent;
import org.jbpm.form.builder.services.api.MenuServiceException;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

@Dependent
@WorkbenchScreen(identifier = "Form Builder")
public class FormBuilderPresenter {

    @Inject
    private FormBuilderView view;
    @Inject
    private Caller<FormServiceEntryPoint> formServices;

    public interface FormBuilderView
            extends
            IsWidget {

        ScrollPanel getMenuView();

        ScrollPanel getLayoutView();
        
        AbsolutePanel getPanel();
    }

    @PostConstruct
    public void init() {
        CommonGlobals.getInstance().registerI18n((I18NConstants) GWT.create(I18NConstants.class));
        PickupDragController dragController = new PickupDragController(view.getPanel(), true);
        dragController.registerDropController(new DisposeDropController(view.getPanel()));
        CommonGlobals.getInstance().registerDragController(dragController);
        try {
            formServices.call(new RemoteCallback<Map<String, String>>() {
                @Override
                public void callback(Map<String, String> properties) {
                    for (String key : properties.keySet()) {
                        RepresentationFactory.registerItemClassName(key, properties.get(key));
                    }

                }
            }).getFormBuilderProperties();
            
            System.out.println("XXXXX  Calling List Menu Items" + this.hashCode());
            
            formServices.call(new RemoteCallback<Void>() {
                @Override
                public void callback(Void nothing) {
                    System.out.println("XXXXX  RETURN List Menu Items");
                }
            }).listMenuItems();


           



            //
            //        bus.addHandler(RepresentationFactoryPopulatedEvent.TYPE, new RepresentationFactoryPopulatedHandler() {
            //            @Override
            //            public void onEvent(RepresentationFactoryPopulatedEvent event) {
            //                try {
            //                    service.getMenuItems();
            //                    service.getMenuOptions();
            //                } catch (FormBuilderException e) {
            //                    //implementation never throws this
            //                }
            //                List<GwtEvent<?>> events = setDataPanel(rootPanel);
            //
            //                //events are fired deferred since they might need that ui components are already attached
            //                fireEvents(events);
            //            }
            //        });
            //        populateRepresentationFactory(service);
            //        populateRepresentationFactory(service);

        } catch (MenuServiceException ex) {
            Logger.getLogger(FormBuilderPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    @WorkbenchPartTitle
    public String getTitle() {
        return "Form Builder";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return view;
    }
}
