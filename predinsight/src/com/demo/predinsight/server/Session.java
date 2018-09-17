package com.demo.predinsight.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.io.File;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

public class Session implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private int[] flowsID;
	private int taskID;
	private int datasetID;
	private int selectedflow;
	private AbstractClassifier model;
	private AbstractClassifier[] pretrained;
	private OpenmlController omlctrl;
	private ArrayList<String> suppressedAttributes;
	private Instances dataset;
	
	public Session(String newname,int[] newflow, int taskid, File maindir) throws Exception {
		name = newname;
		setFlowsID(newflow);
		omlctrl = new OpenmlController(maindir);
		taskID = taskid;
		datasetID = omlctrl.getDatasetIdFromTask(taskid);
		suppressedAttributes = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDatasetID() {
		return datasetID;
	}

	public void setDatasetID(int datasetID) {
		this.datasetID = datasetID;
	}

	public AbstractClassifier getModel() {
		return model;
	}

	public void setModel(AbstractClassifier model) {
		this.model = model;
	}

	public int[] getFlowsID() {
		return flowsID;
	}
	
	public void setFlowsID(int[] flowsID) {
		this.flowsID = flowsID;
	}

	public int getSelectedflow() {
		return selectedflow;
	}

	public void setSelectedflow(int selectedflow) {
		this.selectedflow = selectedflow;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public AbstractClassifier[] getPretrained() {
		return pretrained;
	}
	
	public void setPretrained(AbstractClassifier[] pretrained) {
		this.pretrained = pretrained;
	}

	public OpenmlController oml() {
		return omlctrl;
	}

	public ArrayList<String> getSuppressedAttributes() {
		return suppressedAttributes;
	}

	public void setSuppressedAttributes(ArrayList<String> suppressedAttributes) {
		this.suppressedAttributes = suppressedAttributes;
	}

	public Instances getDataset() {
		return dataset;
	}

	public void setDataset(Instances dataset) {
		this.dataset = dataset;
	}
	
}
