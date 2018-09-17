package com.demo.predinsight.server;

import java.io.File;

import org.openml.apiconnector.algorithms.TaskInformation;
import org.openml.apiconnector.io.OpenmlConnector;
import org.openml.apiconnector.xml.DataSetDescription;
import org.openml.apiconnector.xml.Flow;
import org.openml.apiconnector.xml.Task;

import fr.irit.sig.workflow.main.BasicController;

public class OpenmlController extends BasicController {
	
	protected OpenmlConnector openML;
	private static String apikey = "4aed97aca566f7251113bde7f65bbc51";
	
	public OpenmlController(File mainDir) throws Exception {
		super(mainDir, apikey);
		openML = new OpenmlConnector("https://www.openml.org/", api_key);
	}
	
	public String getDataDesc(int datasetID) {
		return("https://www.openml.org/d/"+Integer.toString(datasetID));
	}
	
	public String getFlowDesc(int flowID) throws Exception {
		Flow flow = openML.flowGet(flowID);	
		return flow.getDescription();
	}
	
	public String getFlowName(int flowID) throws Exception {
		Flow flow = openML.flowGet(flowID);
		return flow.getName();
	}
	
	public Task taskGet(int taskID) throws Exception {
		Task task;
		task = openML.taskGet(taskID);
		return task;
	}
	
	public int getDatasetIdFromTask(int taskID) throws Exception {
		Task t = openML.taskGet(taskID);
		int datasetId = TaskInformation.getSourceData(t).getData_set_id();
		return datasetId;
	}
	
}
