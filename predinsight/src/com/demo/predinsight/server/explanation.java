package com.demo.predinsight.server;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import fr.irit.sig.workflow.splits.TaskSplitInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.ArrayUtils;

import com.demo.predinsight.client.Influence;



public class explanation {
	
	public Influence[][] infs;
	public double[][] preds;
	
	public explanation(Influence[][] i,double[][] p) {
		infs = i;
		preds = p;
	}
	
	public static Influence explain_train(Classifier model, Classifier[] pretrainedmodels, Instances dataset, int instanceid) throws Exception {
		
		Instance inst = dataset.get(instanceid);
		int nbatt = inst.numAttributes();
		int nbclass = dataset.numClasses();
		double[][] explanation = new double[nbclass][nbatt-1];
		double[] base = model.distributionForInstance(inst);
		Instances tempdata;
		Attribute currentatt;
		String[] attnames = new String[nbatt-1];
		
		int att = 0;
		int classid = dataset.classIndex();
		for(int i=0; i<nbatt-1; i++) {
			Classifier tempmodel = pretrainedmodels[i];
			tempdata = new Instances(dataset);
			if(att == classid) {
				att++;
			}
			currentatt = dataset.attribute(att);
			attnames[i] = currentatt.name();
			tempdata.deleteAttributeAt(att);
			Instance tempinst = tempdata.get(instanceid);
			double[] temppred = tempmodel.distributionForInstance(tempinst);
			for(int j=0; j<temppred.length; j++){
				explanation[j][i] = base[j] - temppred[j];
			}
			att++;
		}
		
		double prediction = model.classifyInstance(inst);
		double[] rawinf = new double[nbatt-1];
		System.arraycopy( explanation[(int) prediction], 0, rawinf, 0, nbatt-1 );
		for(int i=0; i<nbclass; i++) {
			double som = 0;
			for(int j=0; j<nbatt-1; j++) {
				som += Math.abs(explanation[i][j]);
			}
			if (som != 0) {
				for(int j=0; j<nbatt-1; j++) {
					explanation[i][j] /= som;
				}
			}
		}

		Influence matexpl = new Influence(explanation[(int) prediction], rawinf,prediction,attnames,inst.toDoubleArray());
		return matexpl;
	}
	
	public static Influence explain_stat(Classifier model, Instance inst, Instances dataset) throws Exception{
		int nbatt = inst.numAttributes();
		int nbclass = dataset.numClasses();
		double[][] explanation = new double[nbclass][nbatt-1];
		double[] base = model.distributionForInstance(inst);
		Attribute currentatt;
		String[] attnames = new String[nbatt-1];
		
		int att = 0;
		int classid = dataset.classIndex();
		for(int i=0; i<nbatt-1; i++) {
			if(att == classid) {
				att++;
			}
			currentatt = dataset.attribute(att);
			attnames[i] = currentatt.name();
			double[] simsum = new double[nbclass];
			for(int j=0; j<dataset.numInstances(); j++) {
				Instance tempinst = (Instance) inst.copy();
				tempinst.setValue(att,dataset.get(j).value(att));
				double[] temppred = model.distributionForInstance(tempinst);
				for(int c=0; c<nbclass; c++) {
					simsum[c] += temppred[c]/dataset.numInstances();
				}
			}
			for(int c=0; c<nbclass; c++) {
				explanation[c][i] += base[c]-simsum[c];
			}
			att++;
		}
		
		double prediction = model.classifyInstance(inst);
		double[] rawinf = new double[nbatt-1];
		System.arraycopy( explanation[(int) prediction], 0, rawinf, 0, nbatt-1 );
		rawinf = explanation[(int) prediction].clone();
		for(int i=0; i<nbclass; i++) {
			double som = 0;
			for(int j=0; j<nbatt-1; j++) {
				som += Math.abs(explanation[i][j]);
			}
			if (som != 0) {
				for(int j=0; j<nbatt-1; j++) {
					explanation[i][j] /= som;
				}
			}
		}

		Influence matexpl = new Influence(explanation[(int) prediction], rawinf, prediction, attnames, inst.toDoubleArray());
		return matexpl;
	}
	
	public static Influence explain_stat(Classifier model, Instance inst, Instances dataset, double[][] vals) throws Exception{
		int nbatt = inst.numAttributes();
		int nbclass = dataset.numClasses();
		double[][] explanation = new double[nbclass][nbatt-1];
		double[] base = model.distributionForInstance(inst);
		double[] simsum;
		double[] temppred;
		Instance tempinst;
		Attribute currentatt;
		String[] attnames = new String[nbatt-1];
		
		int att = 0;
		int classid = dataset.classIndex();
		for(int i=0; i<nbatt-1; i++) {
			if(att == classid) {
				att++;
			}
			currentatt = dataset.attribute(att);
			attnames[i] = currentatt.name();
			simsum = new double[nbclass];
			for(int j=0; j<vals[att].length; j++) {
				tempinst = (Instance) inst.copy();
				tempinst.setValue(att,vals[att][j]);
				temppred = model.distributionForInstance(tempinst);
				for(int c=0; c<nbclass; c++) {
					simsum[c] += (temppred[c]/vals[att].length);
				}
			}
			for(int c=0; c<nbclass; c++) {
				explanation[c][i] += base[c]-simsum[c];
			}
			att++;
		}
		
		double prediction = model.classifyInstance(inst);
		double[] rawinf = new double[nbatt-1];
		rawinf = explanation[(int) prediction].clone();
		for(int i=0; i<nbclass; i++) {
			double som = 0;
			for(int j=0; j<nbatt-1; j++) {
				som += Math.abs(explanation[i][j]);
			}
			if (som != 0) {
				for(int j=0; j<nbatt-1; j++) {
					if(Math.abs(explanation[i][j])>0.0001) {
						explanation[i][j] /= som;
					}
					else {
						explanation[i][j] = 0;
					}
				}
			}
		}
		
		Influence matexpl = new Influence(explanation[(int) prediction], rawinf, prediction,attnames,inst.toDoubleArray());
		return matexpl;
	}

	public static double[][] prepare_vals(Instances dataset){
		double[][] vals;
		int nbdistinct = dataset.numDistinctValues(0);
		int newnb;
		
		for(int i=1; i<dataset.numAttributes(); i++) {
			if (!(i == dataset.classIndex())){
				newnb = dataset.numDistinctValues(i);
				if(nbdistinct<newnb) {
					nbdistinct = newnb;
				}
			}
		}
		
		vals = new double[dataset.numAttributes()][nbdistinct];
		
		double[] attribute;
		int step = (int) Math.floor(dataset.numInstances()/nbdistinct);
		for(int i=0; i<dataset.numAttributes(); i = i+1==dataset.classIndex()? i+2 : i+1) {
			dataset.stableSort(i);
			attribute = dataset.attributeToDoubleArray(i);
			for(int j = 0; j<nbdistinct; j++) {
				vals[i][j] = attribute[j*step + ((int) Math.floor(step/2)) + 1];
			}
		}
		
		return vals;
	}

	public static explanation generate_explanations_train(Session session) throws Exception{
		
		Classifier model;
		Classifier[] models;
		TaskSplitInfo cvInfos;
		File wfFile;
		Instances testSet;
		ArrayList<Integer> testIds;
		
		OpenmlController oml = session.oml();
		int task = session.getTaskID();
		int[] flows = session.getFlowsID();
		// recup le fichier décrivant le wf
		wfFile = oml.getWorkflowFile(flows[0]);

		// download et preprocess le dataset
		Instances dataset = oml.preprocessDataset(task, wfFile);
		
		Influence[][] infs = new Influence[flows.length][dataset.numInstances()];
		double[][] predictions = new double[flows.length][dataset.numInstances()];
		
		for(int i=0; i<(flows.length); i++) {
			cvInfos = oml.prepareSplits(task, dataset);
			wfFile = oml.getWorkflowFile(flows[i]);
			
			for(int fold=0; fold<cvInfos.NB_FOLDS; fold++) {
				model = oml.buildFoldModel(wfFile, cvInfos, fold, 0);
				models = oml.buildFoldRetrainModels(wfFile, cvInfos, fold, 0);
				testSet = oml.getTestSet(fold, 0, cvInfos);
				testIds = oml.getTestSetRowIds(fold, 0, cvInfos);
				
				for(int j=0; j<testSet.numInstances(); j++) {
					System.out.println("explaining instance " + j);
					infs[i][testIds.get(j)] = explain_train(model, models, testSet, j);
					predictions[i][testIds.get(j)] = model.classifyInstance(testSet.instance(j));
				}
			}
		}
			
		explanation explanations = new explanation(infs,predictions);
		return explanations;
	}
	
	public static explanation generate_explanations_stat(Session session) throws Exception{
		
		Classifier model;
		TaskSplitInfo cvInfos;
		File wfFile;
		Instances testSet;
		ArrayList<Integer> testIds;
		
		OpenmlController oml = session.oml();
		int task = session.getTaskID();
		int[] flows = session.getFlowsID();
		// recup le fichier décrivant le wf
		wfFile = oml.getWorkflowFile(flows[0]);

		// download et preprocess le dataset
		Instances dataset = oml.preprocessDataset(task, wfFile);
		
		Influence[][] infs = new Influence[flows.length][dataset.numInstances()];
		double[][] predictions = new double[flows.length][dataset.numInstances()];		
		
		for(int i=0; i<(flows.length); i++) {
			cvInfos = oml.prepareSplits(task, dataset);
			wfFile = oml.getWorkflowFile(flows[i]);
			
			for(int fold=0; fold<cvInfos.NB_FOLDS; fold++) {
				model = oml.buildFoldModel(wfFile, cvInfos, fold, 0);
				testSet = oml.getTestSet(fold, 0, cvInfos);
				testIds = oml.getTestSetRowIds(fold, 0, cvInfos);
				
				for(int j=0; j<testSet.numInstances(); j++) {
					System.out.println("explaining instance " + testIds.get(j));
					infs[i][testIds.get(j)] = explain_stat(model, testSet.instance(j), testSet);
					predictions[i][testIds.get(j)] = model.classifyInstance(testSet.instance(j));
				}
			}
		}
			
		explanation explanations = new explanation(infs,predictions);
		return explanations;
	}
	
	public static explanation generate_explanations_stat_quick(Session session) throws Exception{
		
		Classifier model;
		TaskSplitInfo cvInfos;
		File wfFile;
		Instances testSet;
		ArrayList<Integer> testIds;
		
		OpenmlController oml = session.oml();
		int task = session.getTaskID();
		int[] flows = session.getFlowsID();
		wfFile = oml.getWorkflowFile(flows[0]);
		Instances dataset = oml.preprocessDataset(task, wfFile);
		
		Influence[][] infs = new Influence[flows.length][dataset.numInstances()];
		double[][] predictions = new double[flows.length][dataset.numInstances()];	
		System.out.println("preparing values");
		double[][] values = prepare_vals(dataset);
		
		
		System.out.println("done");
		
		for(int i=0; i<(flows.length); i++) {
			cvInfos = oml.prepareSplits(task, dataset);
			wfFile = oml.getWorkflowFile(flows[i]);
			
			for(int fold=0; fold<cvInfos.NB_FOLDS; fold++) {
				model = oml.buildFoldModel(wfFile, cvInfos, fold, 0);
				testSet = oml.getTestSet(fold, 0, cvInfos);
				testIds = oml.getTestSetRowIds(fold, 0, cvInfos);
				
				for(int j=0; j<testSet.numInstances(); j++) {
					System.out.println("explaining instance " + testIds.get(j));
					infs[i][testIds.get(j)] = explain_stat(model, testSet.instance(j), testSet, values);
					predictions[i][testIds.get(j)] = model.classifyInstance(testSet.instance(j));
				}
			}
		}
			
		explanation explanations = new explanation(infs,predictions);
		return explanations;
	}
	
	public static explanation generate_explanations_stat_quick(Session session, boolean suppressAttributes) throws Exception{
		
		Classifier model;
		TaskSplitInfo cvInfos;
		File wfFile;
		Instances testSet;
		ArrayList<Integer> testIds;
		
		OpenmlController oml = session.oml();
		int task = session.getTaskID();
		int[] flows = session.getFlowsID();
		wfFile = oml.getWorkflowFile(flows[0]);
		Instances dataset = oml.preprocessDataset(task, wfFile);
		
		int nbatt = dataset.numAttributes();
		
		if(suppressAttributes) {
			ArrayList<String> suppressedAttributes = session.getSuppressedAttributes();
			for(int i=0;i<suppressedAttributes.size(); i++) {
				dataset = delete_attribute(suppressedAttributes.get(i), dataset);
			}
		}
				
		Influence[][] infs = new Influence[flows.length][dataset.numInstances()];
		double[][] predictions = new double[flows.length][dataset.numInstances()];	
		
		System.out.println("preparing values");
		double[][] values = prepare_vals(dataset);
		System.out.println("done");
		
		for(int i=0; i<(flows.length); i++) {
			cvInfos = oml.prepareSplits(task, dataset);
			wfFile = oml.getWorkflowFile(flows[i]);
			
			for(int fold=0; fold<cvInfos.NB_FOLDS; fold++) {
				model = oml.buildFoldModel(wfFile, cvInfos, fold, 0);
				testSet = oml.getTestSet(fold, 0, cvInfos);
				testIds = oml.getTestSetRowIds(fold, 0, cvInfos);
				
				for(int j=0; j<testSet.numInstances(); j++) {
					System.out.println("explaining instance " + testIds.get(j));
					infs[i][testIds.get(j)] = explain_stat(model, testSet.instance(j), testSet, values);
					predictions[i][testIds.get(j)] = model.classifyInstance(testSet.instance(j));
				}
			}
		}
			
		explanation explanations = new explanation(infs,predictions);
		return explanations;
	}
	
	public static explanation[] generate_explanations_train_stat_quick(Session session) throws Exception{
		
		Classifier model;
		Classifier[] models;
		TaskSplitInfo cvInfos;
		File wfFile;
		Instances testSet;
		ArrayList<Integer> testIds;
		
		OpenmlController oml = session.oml();
		int task = session.getTaskID();
		int[] flows = session.getFlowsID();
		wfFile = oml.getWorkflowFile(flows[0]);
		Instances dataset = oml.preprocessDataset(task, wfFile);
		
		Influence[][] infs_stat = new Influence[flows.length][dataset.numInstances()];
		Influence[][] infs_train = new Influence[flows.length][dataset.numInstances()];
		double[][] predictions = new double[flows.length][dataset.numInstances()];	
		System.out.println("preparing values");
		double[][] values = prepare_vals(dataset);		
		
		System.out.println("done");
		
		for(int i=0; i<(flows.length); i++) {
			cvInfos = oml.prepareSplits(task, dataset);
			wfFile = oml.getWorkflowFile(flows[i]);
			
			for(int fold=0; fold<cvInfos.NB_FOLDS; fold++) {
				model = oml.buildFoldModel(wfFile, cvInfos, fold, 0);
				models = oml.buildFoldRetrainModels(wfFile, cvInfos, fold, 0);
				testSet = oml.getTestSet(fold, 0, cvInfos);
				testIds = oml.getTestSetRowIds(fold, 0, cvInfos);
				
				for(int j=0; j<testSet.numInstances(); j++) {
					System.out.println("explaining instance " + testIds.get(j));
					infs_train[i][testIds.get(j)] = explain_train(model, models, testSet, j);
					infs_stat[i][testIds.get(j)] = explain_stat(model, testSet.instance(j), testSet, values);
					predictions[i][testIds.get(j)] = model.classifyInstance(testSet.instance(j));
				}
			}
		}
			
		explanation explanations_stat = new explanation(infs_stat,predictions);
		explanation explanations_train = new explanation(infs_train,predictions);
		explanation[] explanations = new explanation[2];
		explanations[0] = explanations_stat;
		explanations[1] = explanations_train;
		return explanations;
	}

	public static double coverage(int[] ids, double[][] W,Influence I) {
		double cov = 0;
		for(int j = 0; j<W[0].length; j++) {
			boolean exists = false;
			int i = 0;
			while(i<ids.length && !exists) {
				exists = exists || (W[ids[i]][j]>0.1);
				i++;
			}
			if(exists) {
				cov += I.getInf()[j];
			}
		}
		return cov;
	}
	
	public static double[] gen_coverages(int[] ids, double[][] W,Influence I) {
		double[] coverages = new double[W.length];
		
		for(int i = 0; i< W.length; i++) {
			boolean isin = false;
			int j = 0;
			while(j<ids.length && !isin) {
				isin = ids[j] == i;
				j++;
			}
			if(isin) {
				coverages[i] = 0;
			}
			else {
				coverages[i] = coverage(ArrayUtils.addAll(ids,new int[] {i}), W, I);
			}
		}
		
		return coverages;
	}
	
	public static int[] pick(int[] ids, double[][] W,Influence I) {
		double[] coverages = gen_coverages(ids, W, I);
		double max = 0;
		int maxid = 0;
		
		for(int i=0; i<coverages.length; i++) {
			if (coverages[i]>max) {
				max = coverages[i];
				maxid = i;
			}
		}
		
		return ArrayUtils.addAll(ids,new int[] {maxid});
	}
	
	public static int[] select_presentation(Influence[][] explanations) {
		
		int[] ids = new int[0];
		Influence sum;
		Influence[] moyexpl = new Influence[explanations[0].length];
		Influence I;
		for (int i=0; i<explanations[0].length; i++) {
			sum = new Influence(explanations[0][0].getInf().length);
			for (int j=0; j<explanations.length; j++) {
				 sum.add(explanations[j][i]);
			}
			sum.mult(1/(explanations.length));
			moyexpl[i] = new Influence(sum);
		}
		
		I = new Influence(explanations[0][0].getInf().length);
		for (int i=0; i<explanations[0].length; i++) {
			I.add(moyexpl[i].abs());
		}
		I.setInf(I.sqrt());
		
		double[][] W = new double[explanations[0].length][moyexpl[0].nbatt()];
		
		for(int i = 0; i<explanations[0].length; i++) {
			double[] data = moyexpl[i].abs();
			for(int j = 0; j<moyexpl[0].nbatt(); j++) {
				W[i][j] = data[j];
			}
		}
		
		for(int loop=0; loop<10; loop++) {
			ids = pick(ids,W,I);
		}
		return ids;
	}
	
	public static int[] select_presentation_fool(Influence[][] explanations) {
		int[] result = {4,12,34,48,70,76,90,103,112,129};
		int[] result0 = {8,10,40,47,71,83,95,101,120,149};
		int[] result1 = {2,18,37,40,78,80,99,112,119,145};
		int[] result2 = {5,11,22,38,56,72,83,96,97,108};
		int[] result3 = {9,16,34,49,71,74,99,104,110,136};
		int randomint = 0;
		Random r = new Random();
		randomint = r.nextInt(4);
		switch(randomint) {
		case 0: result = result0;
		case 1: result = result1;
		case 2: result = result2;
		case 3: result = result3;
		default:;
		}
		return result;
	}
	
	public static Instances delete_attribute(String name, Instances dataset) {
		int nbatt = dataset.numAttributes();
		for(int i=0;i<nbatt; i++) {
			if(name.equals(dataset.attribute(i).name())) {
				dataset.deleteAttributeAt(i);
				System.out.println("attribute suppressed : " + dataset.attribute(i).name());
				break;
			}
			else {
				System.out.println("attribute not suppressed : " + dataset.attribute(i).name());
			}
		}
		return dataset;
	}
	
	public static Influence explain_single(Session session, String[] attributes, double[] values, String param) throws Exception {
		Instances dataset = session.getDataset();
		Classifier model = session.getModel();
		Instance inst = (Instance) dataset.get(0).copy();
		Instance randominst;
		int randomint = 0;
		Random r = new Random();
		
		for(int i=0; i<inst.numAttributes(); i++) {
			if(param.equals("random")){
				randomint = r.nextInt(dataset.numInstances());
				randominst = dataset.get(randomint);
				inst.setValue(i, randominst.value(i));
			}
			else if(param.equals("mean")) {
				inst.setValue(i, dataset.meanOrMode(i));
			}
			else {
				inst.isMissing(i);
			}
		}
		
		for(int i=0; i<attributes.length; i++) {
			for(int j=0; j<inst.numAttributes(); j++) {
				if(attributes[i].equals(dataset.attribute(j).name())) {
					inst.setValue(j, values[i]);
				}
			}
		}
		
		Influence inf = explain_stat(model, inst, dataset);
		
		return inf;
	}
}