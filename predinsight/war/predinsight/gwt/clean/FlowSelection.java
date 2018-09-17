package com.demo.predinsight.client;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FlowSelection extends Content {

	private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
	private Label title = new Label("Select your desired flow and adapt your dataset");
	private HorizontalPanel flowsDescPanel = new HorizontalPanel();
	private VerticalPanel listsPanel = new VerticalPanel();
	private ListBox instancesList = new ListBox();
	private ListBox attributesList = new ListBox();
	private Button suppressButton = new Button("delete selected attributes");
	private VerticalPanel instancePanel = new VerticalPanel();
	private HorizontalPanel buttonsPanel = new HorizontalPanel();
	private Button cancelButton = new Button("cancel changes");
	private Button returnButton = new Button("return");
	private Button nextButton = new Button("next");
	
	private String[] test = {"truc","bidule","machin","chouette"};
	
	
	public FlowSelection(String[] flowsTitles) {
		
		ArrayList<String> testArray = new ArrayList<String>(Arrays.asList(test));
		InstanceDesc instance = new InstanceDesc("instance", "classe", testArray, testArray);
		
		buttonsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(returnButton);
		buttonsPanel.add(nextButton);
		
		listsPanel.add(instancesList);
		listsPanel.add(attributesList);
		
		instancePanel.add(instance);
		instancePanel.add(suppressButton);
		
		for(int i = 0; i<flowsTitles.length;i++) {
			flowsDescPanel.add(new InfDesc(flowsTitles[i]));
		}
		
		//Listbox gestion
		for(int i = 0; i<test.length ;i++) {
			instancesList.addItem(test[i]);
			attributesList.addItem(test[i]);
		}
		instancesList.setVisibleItemCount(10);
		attributesList.setMultipleSelect(true);
		attributesList.setVisibleItemCount(10);
		
		mainPanel.addWest(listsPanel,150);
		listsPanel.setSize("100%", "100%");
		mainPanel.addWest(instancePanel, 250);
		instancePanel.setSize("100%", "100%");
		mainPanel.addSouth(buttonsPanel, 50);
		buttonsPanel.setSize("100%", "100%");
		mainPanel.addNorth(title, 50);
		mainPanel.add(flowsDescPanel);
		
		returnButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		          ContentContainer.getInstance().setContent(new Selection());
		      }
		});
		
		nextButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		          ContentContainer.getInstance().setContent(new Exploitation());
		      }
		});
		
		mainPanel.setSize("1600px","800px");
		
		initWidget(mainPanel);
	}
}
