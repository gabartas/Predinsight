package com.demo.predinsight.client;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sun.xml.internal.ws.api.pipe.NextAction;

public class Selection extends Content {
	private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
	private HorizontalPanel validatePanel = new HorizontalPanel();
	private VerticalPanel datadescPanel = new VerticalPanel();
	private VerticalPanel flowDescPanel = new VerticalPanel();
	private VerticalPanel selectPanel = new VerticalPanel();
	private HorizontalPanel dataselectPanel = new HorizontalPanel();
	private VerticalPanel flowselectPanel = new VerticalPanel();
	private VerticalPanel describePanel = new VerticalPanel();
	private TitlePanel mainTitle = new TitlePanel();
	
	private Frame datadescFrame = new Frame("https://www.openml.org/d/969");

	private ListBox datasetsList = new ListBox();
	private ListBox flowsList = new ListBox();
	
	private Button validateButton = new Button("next");
	
	private Label flowDesc = new Label("No flow to describe yet");
	
	private Label datasetlbl = new Label("Dataset");
	private Label flowlbl = new Label("Flow");
	
	private String[] datasetNames;
	private int[] datasetIds;
	private String[] flowsNames;
	private String[] flowsDesc;
	
	private LinkedList<Integer> selectedFlowsIndex = new LinkedList<Integer>();
	private int selectedDatasetIndex;
	
	private VerticalPanel instrPanel = new VerticalPanel();
	private Label instructions = new Label("Select the dataset and workflows you want to work with");
	
	private ExplServiceAsync explsvc = GWT.create(ExplService.class);
	
	public Selection() {
		
		datasetlbl.setStyleName("parttitle");
		flowlbl.setStyleName("parttitle");
		
		//assemble validate panel
		validatePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		validatePanel.add(validateButton);
		validatePanel.setStyleName("bottompanel");
		
		//assemble describe panel
		describePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		dataselectPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		dataselectPanel.add(datasetlbl);
		dataselectPanel.add(datasetsList);
		dataselectPanel.setSize("300px", "50px");
		describePanel.add(dataselectPanel);
		
		datadescPanel.add(datadescFrame);
		describePanel.add(datadescPanel);
		datadescFrame.setSize("100%","700px");
		datadescPanel.setSize("100%","100%");
		
		describePanel.add(flowDescPanel);
		describePanel.setStyleName("mainpanel");
		
		//assemble select panel
		selectPanel.add(flowlbl);
		selectPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		selectPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
	
		flowselectPanel.add(flowsList);
		flowselectPanel.setStyleName("listpanel");
		selectPanel.add(flowselectPanel);
		flowDescPanel.add(flowDesc);
		selectPanel.add(flowDescPanel);
		flowDescPanel.setStyleName("flowdescpnl");
		selectPanel.setStyleName("westpanel");
		
		//assemble instruction panel
		instrPanel.add(instructions);
		instrPanel.setStyleName("instrpnl");
		instructions.setStyleName("instr");
		
		//assemble main panel
		mainPanel.addNorth(mainTitle, 50);
		mainTitle.setSize("100%", "100%");
		mainPanel.addNorth(instrPanel,50);
		instrPanel.setSize("100%", "75%");
		instructions.setSize("100%", "100%");
		
		mainPanel.addWest(selectPanel,200);
		mainPanel.addSouth(validatePanel,50);
		validatePanel.setSize("100%", "100%");
		mainPanel.add(describePanel);
		describePanel.setSize("100%", "100%");
		flowDesc.setWidth("100%");
		
		
		
		validateButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  for (int i = 0; i < flowsList.getItemCount(); i++) {
		    	      if (flowsList.isItemSelected(i)) {
		    	    	  selectedFlowsIndex.add(i);
		    	      }
		    	  }
		    	  selectedDatasetIndex = datasetsList.getSelectedIndex();
		          try {
		        	validateButton.setText("loading, please wait");
					ContentContainer.getInstance().setContent(new FlowSelection(selectedFlowsIndex,selectedDatasetIndex));
				} catch (Exception e) {
					validateButton.setText("an error occured");
					mainPanel.clear();
					Logger rootlogger = Logger.getLogger("");
					mainPanel.add(new Label(e.getLocalizedMessage()));
					rootlogger.log(Level.SEVERE, "error", e);
				}
		      }
		});
		
		
		
		//Listbox gestion
		
		AsyncCallback<String[]> callbackflownames = new AsyncCallback<String[]>() {
		    public void onFailure(Throwable caught) {
		    	
		    }
		    public void onSuccess(String[] result) {
		    	flowsNames = result;
		    	for(int i = 0; i<flowsNames.length ;i++) {
					flowsList.addItem(flowsNames[i]);
				}
		    }
		 };
		 
		 AsyncCallback<String[]> callbackdataNames = new AsyncCallback<String[]>() {
			    public void onFailure(Throwable caught) {
			    	
			    }
			    public void onSuccess(String[] result) {
			    	datasetNames = result;
			    	for(int i = 0; i<datasetNames.length ;i++) {
						datasetsList.addItem(datasetNames[i]);
					}
			    }
			 };
		 
		 AsyncCallback<int[]> callbackdataIds = new AsyncCallback<int[]>() {
			    public void onFailure(Throwable caught) {
			    	
			    }
			    public void onSuccess(int[] result) {
			    	datasetIds = result;
			    }
		 };
		 
		 AsyncCallback<String[]> callbackflowdesc = new AsyncCallback<String[]>() {
			    public void onFailure(Throwable caught) {
			    	
			    }
			    public void onSuccess(String[] result) {
			    	flowsDesc = result;
			    }
			 };
		 
		 explsvc.getDatasetsIds(callbackdataIds);
		 explsvc.getDatasetsNames(callbackdataNames);
		 explsvc.getFlowsNames(callbackflownames);
		 explsvc.getFlowsDesc(callbackflowdesc);
		 
		 datasetsList.addChangeHandler(new ChangeHandler() {
			 public void onChange(ChangeEvent event) {
				 int index = datasetsList.getSelectedIndex();
				 datadescFrame.setUrl("https://www.openml.org/d/"+datasetIds[index]);
			 }
		 });
		 
		 flowsList.addChangeHandler(new ChangeHandler() {
			 public void onChange(ChangeEvent event) {
				 int index = flowsList.getSelectedIndex();
				 flowDesc.setText(flowsDesc[index]);;
			 }
		 });
		
		flowsList.setVisibleItemCount(10);
		flowsList.setMultipleSelect(true);
		
		//mainPanel.setSize("1600px","800px");
		
		mainPanel.addStyleName("mainpanel");
		
		initWidget(mainPanel);
		
		
		
	}
	
}
