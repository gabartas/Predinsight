package com.demo.predinsight.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import org.openml.apiconnector.xml.Task;

import com.demo.predinsight.client.AttributesInformations;
import com.demo.predinsight.client.ExplService;
import com.demo.predinsight.client.Influence;
import com.demo.predinsight.client.Informations;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fr.irit.sig.workflow.splits.TaskSplitInfo;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import com.demo.predinsight.server.Session;
import com.demo.predinsight.server.explanation;

public class ExplServiceImpl extends RemoteServiceServlet implements ExplService {

	int[] flowsID = {7838,7835,7845,7790,7789,7781};
	int[] tasksID = {3832,24,9979,145872,146800,146038,145906,146073,145977,145976};
	int[] datasetID = {969,24,1466,1504,40966,187,1541,481,39,37};
	String[] datasetName = {"Iris","Mushroom","Cardiotocography","Steel plates fault","Mice protein","Wine","Volcanoes-d4","Biomed","Ecoli","Diabete"};
	String[] flowName = {"Adaboost naive bayes","Nearest neighbors","Decision tree","Random forest","Bagging naive bayes","Support vector machine"};
	String[] flowDesc = {"naive Bayes classifiers are simple \"probabilistic classifiers\" based on applying Bayes' theorem with strong (naive) independence assumptions between the features",
						"The method of the K nearest neighbors consist in comparing the object to his k closest known neighbors",
						"A decision tree is a decision support tool that uses a tree-like graph or model of decisions and their possible consequences",
						"An ensemble learning method for classification, regression and other tasks, that operate by constructing a multitude of decision trees at training time and outputting the class that is the mean prediction of the individual trees",
						"naive Bayes classifiers are simple \"probabilistic classifiers\" based on applying Bayes' theorem with strong (naive) independence assumptions between the features",
						"Support vector machines uses vectors to separate the data space in distinc categories"
	};
	File workDir = new File("F:\\Work\\whatev");
	
	
	public String[] getDatasetsNames(){
		return datasetName;
	}
	
	public int[] getDatasetsIds(){
		return datasetID;
	}

	public String[] getFlowsNames() {
		return flowName;
	}
	
	public String[] getFlowsDesc() {
		return flowDesc;
	}
	
	public Informations generateDesc(int[] flowsindex, int datasetindex){
	
		int[] flows = new int[flowsindex.length];
		int taskid = tasksID[datasetindex];
		int[] selectedInstances;
		double[] trueclasses = new double[10];
		explanation expl;
		
		Influence[][] descs = new Influence[flowsindex.length][10];
		
		for(int i=0; i<flowsindex.length; i++) {
			flows[i] = flowsID[flowsindex[i]];
		}
		Session sess;
		try {
			if(!SingleSession.getInstance().alreadyset()) {
				sess = new Session("test",flows,taskid,workDir);
				SingleSession.getInstance().setSession(sess);
			}
			else {
				SingleSession.getInstance().getSession().setFlowsID(flows);
				SingleSession.getInstance().getSession().setTaskID(taskid);
			}
			expl = explanation.generate_explanations_stat_quick(SingleSession.getInstance().getSession());
			selectedInstances = explanation.select_presentation_fool(expl.infs);
			for(int i = 0; i<selectedInstances.length;i++) {
				System.out.println(selectedInstances[i]);
			}
			
			OpenmlController omlctrl = SingleSession.getInstance().getSession().oml();
			File wfFile = omlctrl.getWorkflowFile(flowsID[flowsindex[0]]);
			Instances dataset = omlctrl.preprocessDataset(taskid, wfFile);
			int classind = dataset.classIndex();
			Instance currentinst;
			
			for(int i = 0; i<10; i++) {
				for(int j = 0; j<flowsindex.length; j++) {
					descs[j][i] = expl.infs[j][selectedInstances[i]];
				}
				currentinst = dataset.get(selectedInstances[i]);
				trueclasses[i] = currentinst.value(classind);
				System.out.println("class : " + currentinst.value(classind));
			}
			return new Informations(descs,trueclasses,selectedInstances);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Informations generateDesc_suppressatts(int[] flowsindex, int datasetindex, ArrayList<String> suppressedatts){
		
		int[] flows = new int[flowsindex.length];
		int taskid = tasksID[datasetindex];
		int[] selectedInstances;
		double[] trueclasses = new double[10];
		explanation expl;
		
		Influence[][] descs = new Influence[flowsindex.length][10];
		
		for(int i = 0; i<suppressedatts.size(); i++) {
  			  System.out.println("suppressed : "+suppressedatts.get(i));
  	  	}
		
		for(int i=0; i<flowsindex.length; i++) {
			flows[i] = flowsID[flowsindex[i]];
		}
		
		SingleSession.getInstance().getSession().setSuppressedAttributes(suppressedatts);
		try {
			expl = explanation.generate_explanations_stat_quick(SingleSession.getInstance().getSession(), true);
			selectedInstances = explanation.select_presentation_fool(expl.infs);
			for(int i = 0; i<selectedInstances.length;i++) {
				System.out.println(selectedInstances[i]);
			}
			
			OpenmlController omlctrl = SingleSession.getInstance().getSession().oml();
			File wfFile = omlctrl.getWorkflowFile(flowsID[flowsindex[0]]);
			Instances dataset = omlctrl.preprocessDataset(taskid, wfFile);
			int classind = dataset.classIndex();
			Instance currentinst;
			
			for(int i = 0; i<10; i++) {
				for(int j = 0; j<flowsindex.length; j++) {
					descs[j][i] = expl.infs[j][selectedInstances[i]];
				}
				currentinst = dataset.get(selectedInstances[i]);
				trueclasses[i] = currentinst.value(classind);
				System.out.println("class : " + currentinst.value(classind));
			}
			return new Informations(descs,trueclasses,selectedInstances);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean trainAndSave(String flowname) throws Exception {
		
		Classifier model;
		TaskSplitInfo cvInfos;
		File wfFile;
		
		OpenmlController oml = SingleSession.getInstance().getSession().oml();
		int taskId = SingleSession.getInstance().getSession().getTaskID();
		int flowId = 100000;		
		for(int i=0; i<flowName.length; i++) {
			if(flowname.equals(flowName[i])) {
				flowId = flowsID[i];
				break;
			}
		}
		int[] flowsid = {flowId};
		SingleSession.getInstance().getSession().setFlowsID(flowsid);

		System.out.println("getting workflow file");
		wfFile = oml.getWorkflowFile(flowId);
		System.out.println("path : " + wfFile.getAbsolutePath());
		System.out.println("preprocessing dataset");
		Instances dataset = oml.preprocessDataset(taskId, wfFile);
		
		int nbatt = dataset.numAttributes();
		
		ArrayList<String> suppressedAttributes = SingleSession.getInstance().getSession().getSuppressedAttributes();
		boolean suppressAttributes = suppressedAttributes.size() != 0;
		System.out.println("not suppressing attributes");
		if(suppressAttributes) {
			System.out.println("suppressing attributes");
			for(int i=0;i<suppressedAttributes.size(); i++) {
				dataset = explanation.delete_attribute(suppressedAttributes.get(i), dataset);
			}
		}
		
		System.out.println("preparing splits");
		cvInfos = oml.prepareSplits(taskId, dataset);
		wfFile = oml.getWorkflowFile(flowId);
		System.out.println("building model");
		model = oml.buildModelCustom(wfFile, cvInfos);
		System.out.println("done");
		
		SingleSession.getInstance().getSession().setModel((AbstractClassifier) model);
		SingleSession.getInstance().getSession().setDataset(dataset);
		return true;	
	}
	
	public AttributesInformations getinfs() {
		AttributesInformations infos;
		Instances dataset = SingleSession.getInstance().getSession().getDataset();
		int nbatt = dataset.numAttributes()-1;
		
		String[] attNames = new String[nbatt];
		String[] attTypes = new String[nbatt];
		double[] attmean = new double[nbatt];
		double[] attmin = new double[nbatt];
		double[] attmax = new double[nbatt];
		ArrayList<String[]> attvalues = new ArrayList<String[]>();
		String[] emptyvalues = {};
		int k=0;
		for(int i=0; i<dataset.numAttributes(); i++) {
			if(i!=dataset.classIndex()) {
				attNames[k] = dataset.attribute(i).name();
				attmean[k] = dataset.meanOrMode(i);
				if(dataset.attribute(i).isNumeric()) {
					attTypes[k] = "numeric";
					attmin[k] = dataset.kthSmallestValue(i, 1);
					attmax[k] = dataset.kthSmallestValue(i, dataset.numInstances());
					attvalues.add(emptyvalues);
				}
				else {
					attTypes[k] = "nominal";
					String[] attvaluestemp = new String[dataset.attribute(i).numValues()];
					for(int j=0; j<dataset.attribute(i).numValues(); j++) {
						attvaluestemp[j] = dataset.attribute(i).value(j);
					}
					attvalues.add(attvaluestemp);
				}
				k++;
			}
		}
		
		infos = new AttributesInformations(attNames,attTypes,attmean,attmin,attmax,attvalues);
		
		return infos;
	}
	
	public Influence predictOne(String[] attributes, double[] values, String param) {
		Influence inf = new Influence();
		try {
			inf = explanation.explain_single(SingleSession.getInstance().getSession(), attributes, values, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inf;
	}

}
