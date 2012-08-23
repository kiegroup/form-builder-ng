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

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.jbpm.form.builder.ng.client.view.layout.LayoutViewImpl;
import org.jbpm.form.builder.ng.client.view.menu.AnimatedMenuViewImpl;
import org.uberfire.client.mvp.PlaceManager;

/**
 * Main view. Uses UIBinder to define the correct position of components
 */
@Dependent
public class FormBuilderViewImpl extends AbsolutePanel implements FormBuilderPresenter.FormBuilderView{

    @Inject
    private UiBinder<Widget, FormBuilderViewImpl> uiBinder;


    @Inject
    private PlaceManager placeManager;
    @Inject
    private FormBuilderPresenter presenter;
    
    public @UiField(provided=true) ScrollPanel menuView;
    public @UiField(provided=true) ScrollPanel layoutView;
   

    
    
    @PostConstruct
    protected final void init() {
            menuView = new AnimatedMenuViewImpl();
            layoutView = new LayoutViewImpl();
            //initWidget(uiBinder.createAndBindUi(this));
            menuView.setAlwaysShowScrollBars(true);
            menuView.setSize("235px", "100%");
            layoutView.setSize("700px", "700px");
            layoutView.setAlwaysShowScrollBars(true);
            add(uiBinder.createAndBindUi(this));
            //adopt(this);
//            
//            int fullHeight = Window.getClientHeight();
//            String height = "" + (fullHeight - 80) + "px";
//            String smallerHeight = "" + (fullHeight - 105) + "px";
            //treeView.setHeight("100%");
           // menuView.setHeight("100%");
//            editionView.setHeight("100%");
            //ioAssociationView.setHeight("100%");
           // layoutView.setHeight(smallerHeight);
            
            
    }

    public ScrollPanel getMenuView() {
        return menuView;
    }

    public ScrollPanel getLayoutView() {
        return layoutView;
    }

    public AbsolutePanel getPanel(){
        return this;
    }
    
    

    
    
}
