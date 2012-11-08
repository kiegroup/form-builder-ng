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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.form.builder.ng.model.client.FormBuilderException;
import org.jbpm.form.builder.ng.model.client.effect.FBFormEffect;
import org.jbpm.form.builder.ng.model.client.form.FBFormItem;
import org.jbpm.form.builder.ng.model.client.messages.I18NConstants;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;
import com.gwtent.reflection.client.Reflectable;
import org.jbpm.form.builder.ng.model.client.CommonGlobals;
import org.jbpm.form.builder.ng.model.shared.api.FormBuilderDTO;

/**
 * UI form item. Represents a line graph
 */
@Reflectable
public class LineGraphFormItem extends FBFormItem {

    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();

    private LineChart chart = new LineChart(DataTable.create(), Options.create());
    
    private List<List<String>> dataTableRep = new ArrayList<List<String>>();
    private List<Map.Entry<String, String>> dataStructRep = new ArrayList<Map.Entry<String, String>>();
    
    private String graphTitle;
    private String graphXTitle;
    private String graphYTitle;
    
    public LineGraphFormItem() {
        this(new ArrayList<FBFormEffect>());
    }
    
    public LineGraphFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        setSize("200px", "150px");
        chart.setSize("200px", "150px");
        add(chart);
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("width", getWidth());
        map.put("height", getHeight());
        map.put("graphTitle", this.graphTitle);
        map.put("graphXTitle", this.graphXTitle);
        map.put("graphYTitle", this.graphYTitle);
        return map;
    }

    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        setWidth(extractString(asPropertiesMap.get("width")));
        setHeight(extractString(asPropertiesMap.get("height")));
        this.graphTitle = extractString(map.get("graphTitle"));
        this.graphXTitle = extractString(map.get("graphXTitle"));
        this.graphYTitle = extractString(map.get("graphYTitle"));
        populate(this.chart);
    }
    
    private int getIntValue(Object obj) {
        return Integer.valueOf(obj.toString());
    }
    
    private void populate(LineChart chart) {
        Options options = Options.create();
        options.setWidth(getOffsetWidth());
        options.setHeight(getOffsetHeight());
        options.setTitle(this.graphTitle);
        options.setTitleX(this.graphXTitle);
        options.setTitleY(this.graphYTitle);
        DataTable dataTable = DataTable.create();
        if (dataStructRep != null) {
            for (Map.Entry<String, String> col : dataStructRep) {
                dataTable.addColumn(ColumnType.valueOf(col.getValue()), col.getKey());
            }
        }
        if (dataTableRep != null) {
            for (List<String> row : dataTableRep) {
                dataTable.setValue(getIntValue(row.get(0)), getIntValue(row.get(1)), row.get(2));
            }
        }
        chart.draw(dataTable, options);  
    }

    @Override
    public FormBuilderDTO getRepresentation() {
        FormBuilderDTO dto = super.getRepresentation();
        dto.setString("graphTitle", graphTitle);
        dto.setString("graphXTitle", graphXTitle);
        dto.setString("graphYTitle", graphYTitle);
        List<Object> structMap = new ArrayList<Object>();
        for (Map.Entry<String, String> entry : dataStructRep) {
        	Map<String, Object> itemMap = new HashMap<String, Object>();
        	itemMap.put("key", entry.getKey());
        	itemMap.put("value", entry.getValue());
        	structMap.add(itemMap);
        }
        dto.setList("dataStructure", structMap);
        List<Object> tableMap = new ArrayList<Object>();
        for (List<String> yPoint : dataTableRep) {
        	Map<String, Object> itemMap = new HashMap<String, Object>();
        	if (yPoint != null) {
        		int index = 0;
        		for (String point : yPoint) {
        			itemMap.put(String.valueOf(index), point);
        			index++;
        		}
        	}
        	tableMap.add(itemMap);
        }
        dto.setList("dataTable", tableMap);
        return dto;
    }
    
    @Override
    public void populate(FormBuilderDTO dto) throws FormBuilderException {
        if (!dto.getClassName().endsWith("LineGraphRepresentation")) {
            throw new FormBuilderException(i18n.RepNotOfType(dto.getClassName(), "LineGraphRepresentation"));
        }
        super.populate(dto);
        List<FormBuilderDTO> dataStructList = dto.getListOfDtos("dataStructure");
        this.dataStructRep.clear();
        for (FormBuilderDTO structDto : dataStructList) {
        	LineGraphEntry entry = new LineGraphEntry();
        	entry.setKey(structDto.getString("key"));
        	entry.setValue(structDto.getString("value"));
        	dataStructRep.add(entry);
        }
        this.dataTableRep.clear();
        List<FormBuilderDTO> dataTableList = dto.getListOfDtos("dataTable");
        for (FormBuilderDTO pointDto : dataTableList) {
        	List<String> keys = new ArrayList<String>(pointDto.getParameters().keySet());
        	Collections.sort(keys);
        	List<String> item = new ArrayList<String>();
        	for (String key : keys) {
        		item.add(pointDto.getString(key));
        	}
        	this.dataTableRep.add(item);
        }
        this.graphTitle = dto.getString("graphTitle");
        this.graphXTitle = dto.getString("graphXTitle");
        this.graphYTitle = dto.getString("graphYTitle");
        populate(this.chart);
    }

    @Override
    public FBFormItem cloneItem() {
        LineGraphFormItem item = super.cloneItem(new LineGraphFormItem(getFormEffects()));
        item.chart = (LineChart) cloneDisplay(null);
        item.dataStructRep = new ArrayList<Map.Entry<String, String>>(this.dataStructRep);
        item.dataTableRep = new ArrayList<List<String>>(this.dataTableRep);
        item.graphTitle = this.graphTitle;
        item.graphXTitle = this.graphXTitle;
        item.graphYTitle = this.graphYTitle;
        return item;
    }

    @Override
    public Widget cloneDisplay(Map<String, Object> data) {
        LineChart chart = new LineChart();
        populate(chart);
        if (getInput() != null && getInput().get("name") != null) {
            DataTable dataTable = DataTable.create();
            Object myData = data.get(getInput().get("name"));
            populateInput(dataTable, myData);
            chart.draw(dataTable);
        }
        super.populateActions(chart.getElement());
        return chart;
    }

    private void populateInput(DataTable dataTable, Object myData) {
        if (myData.getClass().isArray()) {
            Object[] myDataArray = (Object[]) myData;
            int index = 0;
            for (Object item : myDataArray) {
                setRowDataFromInput(dataTable, index, item);
                index++;
            }
        } else if (myData instanceof Collection) {
            Collection<?> myDataCol = (Collection<?>) myData;
            int index = 0;
            for (Object item : myDataCol) {
                setRowDataFromInput(dataTable, index, item);
                index++;
            }
        } else if (myData instanceof Map) {
            Map<?, ?> myDataMap = (Map<?, ?>) myData;
            int index = 0;
            for (Object item : myDataMap.values()) {
                setRowDataFromInput(dataTable, index, item);
                index++;
            }
        }
        
    }

    private void setRowDataFromInput(DataTable dataTable, int index, Object item) {
        if (item.getClass().isArray()) {
            Object[] subObjArray = (Object[]) item;
            int columnIndex = 0;
            for (Object subObj : subObjArray) {
                String value = subObj.toString();
                dataTable.setCell(index, columnIndex, value, value, null);
                columnIndex++;
            }
        } else if (item instanceof Collection) {
            Collection<?> subObjCol = (Collection<?>) item;
            int columnIndex = 0;
            for (Object subObj : subObjCol) {
                String value = subObj.toString();
                dataTable.setCell(index, columnIndex, value, value, null);
                columnIndex++;
            }
        } else if (item instanceof Map) {
            Map<?, ?> subObjMap = (Map<?, ?>) item;
            int columnIndex = 0;
            for (Object subObj : subObjMap.values()) {
                String value = subObj.toString();
                dataTable.setCell(index, columnIndex, value, value, null);
                columnIndex++;
            }

        } else {
            String value = item.toString();
            dataTable.setCell(index, 0, value, value, null);
        }
    }

    private class LineGraphEntry implements Map.Entry<String, String> {

    	private String key;
    	
    	private String value;

    	@Override
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public String setValue(String value) {
			String aux = this.value;
			this.value = value;
			return aux;
		}
    }
}
