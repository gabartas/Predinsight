package com.demo.predinsight.client;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FlowSelection extends Content {

	private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
	private HorizontalPanel flowsDescPanel = new HorizontalPanel();
	private TitlePanel mainTitle = new TitlePanel();
	
	private VerticalPanel westPanel = new VerticalPanel();
		private Label selectionlbl = new Label("Selection");
		private HorizontalPanel instancePanel = new HorizontalPanel();
			private VerticalPanel instanceselectPanel = new VerticalPanel();
				private Label instanceslbl = new Label("Instance");
				private ListBox instancesList = new ListBox();
				
		private HorizontalPanel attribPanel = new HorizontalPanel();
			private VerticalPanel attribselectPanel = new VerticalPanel();
				private Label attriblbl = new Label("Attributes");
				private ListBox attributesList = new ListBox();
			private Button suppressButton = new Button("delete selected attributes");
	
	
	
	private HorizontalPanel southPanel = new HorizontalPanel();
		private HorizontalPanel buttonsPanel = new HorizontalPanel();
			private Button cancelButton = new Button("cancel changes");
			private Button returnButton = new Button("return");
			private Button nextButton = new Button("next");
	
	private VerticalPanel instrPanel = new VerticalPanel();
	private Label instructions = new Label("Select your desired flow and adapt your dataset");
	
	private String[] test2 = new String[0];
	
	private int[] selectedFlowsInd;
	private int selectedDatasetInd;
	
	private ExplServiceAsync explsvc = GWT.create(ExplService.class);
	
	private Influence[][] infs;
	private double[] trueclasses;
	private int[] instIds;
	String[] flowName = {"Adaboost naive bayes","Nearest neighbors","Decision tree","Random forest","Bagging naive bayes","Support vector machine"};
	String selectedFlow;
	InstanceDesc instance = new InstanceDesc("loading", "loading", test2, test2);
	
	
	public FlowSelection(LinkedList<Integer> selectedFlowsIndex, int selectedDatasetIndex) throws Exception {
		
		selectedFlowsInd = new int[selectedFlowsIndex.size()];
		for(int i = 0; i<selectedFlowsIndex.size();i++) {
			selectedFlowsInd[i] = selectedFlowsIndex.get(i);
		}
		
		selectedDatasetInd = selectedDatasetIndex;		
		
		selectionlbl.setStyleName("parttitle");
		instanceslbl.setStyleName("subparttitle");
		attriblbl.setStyleName("subparttitle");
		
		//bottom buttons
		southPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(returnButton);
		buttonsPanel.add(nextButton);
		southPanel.add(buttonsPanel);
		
		//instance and attribute selection
		westPanel.add(selectionlbl);
		westPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		instanceselectPanel.add(instanceslbl);
		instanceselectPanel.add(instancesList);
		instanceselectPanel.setStyleName("listpanel");
		instancePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		instancePanel.add(instanceselectPanel);
		instancePanel.add(instance);
		instance.setStyleName("instdesc");
		westPanel.add(instancePanel);
		
		attribselectPanel.add(attriblbl);
		attribselectPanel.add(attributesList);
		attribselectPanel.setStyleName("listpanel");
		attribPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		attribPanel.add(attribselectPanel);
		attribPanel.add(suppressButton);
		suppressButton.addStyleName("defaultbutton");
		westPanel.add(attribPanel);
		
		//instructions
		instrPanel.add(instructions);
		instrPanel.setStyleName("instrpnl");
		instructions.setStyleName("instr");
		
		//Global layout
		mainPanel.addNorth(mainTitle, 50);
		mainTitle.setSize("100%", "100%");
	
		mainPanel.addNorth(instrPanel, 50);
		instrPanel.setSize("100%", "75%");
		instructions.setSize("100%", "100%");
		
		mainPanel.addWest(westPanel,350);
		westPanel.setSize("100%", "100%");
		westPanel.setStyleName("westpanel");
		
		mainPanel.addSouth(southPanel, 50);
		southPanel.setSize("100%", "100%");
		southPanel.setStyleName("bottompanel");
		
		mainPanel.add(flowsDescPanel);
		flowsDescPanel.setStyleName("mainpanel");
		
		
		AsyncCallback<Informations> callbackinffirst = new AsyncCallback<Informations>() {
		    public void onFailure(Throwable caught) {
		    	Logger rootlogger = Logger.getLogger("");
		    	flowsDescPanel.add(new Label("Failure : " + caught.getLocalizedMessage()));
		    	rootlogger.log(Level.SEVERE, "error",caught);
		    }
		    public void onSuccess(Informations result) {
		    	flowsDescPanel.add(new Label("done"));
		    	infs = result.desc;
		    	trueclasses = result.trueclasses;
		    	instIds = result.selectedIds;
		    	refreshflowsdesc(0);
		    	Influence infref = infs[0][0];
		    	String[] names = infref.getAttNames();
		    	attributesList.clear();
		    	for(int i = 0; i<names.length ;i++) {
					attributesList.addItem(names[i]);
				}
		    	attributesList.setMultipleSelect(true);
				attributesList.setVisibleItemCount(10);
				
				instancesList.clear();
				for(int i = 0; i<10 ;i++) {
					instancesList.addItem("instance " + instIds[i]);
				}
		    }
		};
		
		AsyncCallback<Informations> callbackinf = new AsyncCallback<Informations>() {
		    public void onFailure(Throwable caught) {
		    	Logger rootlogger = Logger.getLogger("");
		    	flowsDescPanel.add(new Label("Failure : " + caught.getLocalizedMessage()));
		    	rootlogger.log(Level.SEVERE, "error",caught);
		    }
		    public void onSuccess(Informations result) {
		    	flowsDescPanel.add(new Label("done"));
		    	infs = result.desc;
		    	trueclasses = result.trueclasses;
		    	instIds = result.selectedIds;
		    	refreshflowsdesc(0);
		    	
				instancesList.clear();
				for(int i = 0; i<10 ;i++) {
					instancesList.addItem("instance " + instIds[i]);
				}
		    }
		};
		
		AsyncCallback<Boolean> callbacksave = new AsyncCallback<Boolean>() {
		    public void onFailure(Throwable caught) {
		    	Logger rootlogger = Logger.getLogger("");
		    	flowsDescPanel.add(new Label("Failure on callbacksave : " + caught.getLocalizedMessage()));
		    	rootlogger.log(Level.SEVERE, "error",caught);
		    }
		    public void onSuccess(Boolean result) {
		    	ContentContainer.getInstance().setContent(new Exploitation());
		    }
		};
//		AsyncCallback<String[]> callbacknames = new AsyncCallback<String[]>() {
//		    public void onFailure(Throwable caught) {
//		    	Logger rootlogger = Logger.getLogger("");
//		    	flowsDescPanel.add(new Label("Failure : " + caught.getLocalizedMessage()));
//		    	rootlogger.log(Level.SEVERE, "error",caught);
//		    }
//		    public void onSuccess(String[] result) {
//		    	flowsDescPanel.add(new Label("done"));
//		    	attnames = result;
//		    }
//		};
		
		explsvc.generateDesc(selectedFlowsInd, selectedDatasetInd, callbackinffirst);
		
		flowsDescPanel.add(new Label("Loading"));
		
		/*for(int i = 0; i<test.length;i++) {
			InfDesc newdesc = new InfDesc(test[i],test,test2);
			newdesc.setStyleName("infdesc");
			flowsDescPanel.add(newdesc);
		}*/
		
		
		instancesList.addChangeHandler(new ChangeHandler() {
			 public void onChange(ChangeEvent event) {
				 int index = instancesList.getSelectedIndex();
				 refreshflowsdesc(index);
			 }
		 });
		
		returnButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		          ContentContainer.getInstance().setContent(new Selection());
		      }
		});
		
		nextButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  try {
					explsvc.trainAndSave(selectedFlow, callbacksave);
				} catch (Exception e) {
					Logger rootlogger = Logger.getLogger("");
			    	flowsDescPanel.add(new Label("Failure : " + e.getLocalizedMessage()));
			    	rootlogger.log(Level.SEVERE, "error",e);
				}		          
		      }
		});
		
		suppressButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  flowsDescPanel.clear();
		    	  flowsDescPanel.add(new Label("Loading"));
		    	  ArrayList<String> suppr = new ArrayList<String>();
		    	  for(int i = 0; i<attributesList.getItemCount(); i++) {
		    		  if(attributesList.isItemSelected(i)) {
		    			  suppr.add(attributesList.getItemText(i));
		    			  System.out.println("suppressed : "+i);
		    		  }
		    	  }
		    	  
		    	  explsvc.generateDesc_suppressatts(selectedFlowsInd, selectedDatasetInd, suppr, callbackinf);
		      }
		});  
		
		cancelButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  flowsDescPanel.clear();
		    	  flowsDescPanel.add(new Label("Loading"));
		    	  explsvc.generateDesc(selectedFlowsInd, selectedDatasetInd, callbackinf);
		      }
		});
				
		initWidget(mainPanel);
	}


	private void refreshflowsdesc(int instanceid) {
		flowsDescPanel.clear();
		String name;
		double[] values;
		String[] attnames;
		Influence currentinf;
		InfDesc desc;
		
		//refresh flows panel
		for(int i = 0; i<selectedFlowsInd.length; i++) {
			currentinf = infs[i][instanceid];
			name = flowName[selectedFlowsInd[i]];
			values = currentinf.getInf();
			attnames = currentinf.getAttNames();
			desc = new InfDesc(name, ""+currentinf.getPred(), attnames, values);
			flowsDescPanel.add(desc);
			desc.setStyleName("focus");
			desc.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		    	  Widget sender = (Widget) event.getSource();
		    	  int nbwidgets = flowsDescPanel.getWidgetCount();
		    	  for(int i = 0; i<nbwidgets; i++) {
		    		  InfDesc currentdesc = (InfDesc) flowsDescPanel.getWidget(i);
		    		  if (currentdesc == sender) {
		    			  selectedFlow = currentdesc.getName();
		    			  currentdesc.setStyleName("focusselected");
		    		  }
		    		  else {
		    			  currentdesc.setStyleName("focus");
		    		  }
		    	  }
		      }
			});
		}
		InfDesc currentdesc = (InfDesc) flowsDescPanel.getWidget(0);
		selectedFlow = currentdesc.getName();
		currentdesc.setStyleName("focusselected");
		
		
		//refresh instance
		Influence infref = infs[0][instanceid];
		String[] instvalues = new String[infref.getInstance().length];
		for(int i=0; i<infref.getInstance().length; i++) {
			instvalues[i] = ""+infref.getInstance()[i];
		}
		instance.refresh("instance " + instIds[instanceid], "" + trueclasses[instanceid], infref.getAttNames(), instvalues);
	}
	
	private DialogBox createDialogBox() {
	    // Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
	    dialogBox.ensureDebugId("cwDialogBox");
	    dialogBox.setText("Loading");

	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setSpacing(4);
	    dialogBox.setWidget(dialogContents);


	    dialogContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

	    // Return the dialog box
	    return dialogBox;
	  }
}
