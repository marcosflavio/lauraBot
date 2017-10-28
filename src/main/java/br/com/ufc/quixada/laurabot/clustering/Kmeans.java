package br.com.ufc.quixada.laurabot.clustering;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Kmeans {
	private int numClusters;
	private List<Question> questions;
	private List<Cluster> clusters;

	public Kmeans(int k) {
		this.numClusters = k;
		this.questions = new ArrayList<>();
		this.clusters = new ArrayList<>();
	}

	public void init() {
		System.err.println("Iniciei o kmeans");
		this.initClusters();
		this.setRandomCentroids();
	}

	private void initClusters() {
		for (int i = 0; i < this.numClusters; i++) {
			Cluster cluster = new Cluster(i);
			this.clusters.add(cluster);
		}
	}
	
	private void setRandomCentroids() {
		List<Integer> possibleCentroids = new ArrayList<>();
		for (int i = 0; i < this.questions.size(); i++) {
			possibleCentroids.add(i);
		}
		Collections.shuffle(possibleCentroids);
		for (int j = 0; j < this.numClusters; j++) {
			Question centroid = questions.get(possibleCentroids.get(j));
			centroid.setClusterId(j);
			clusters.get(j).setCentroid(centroid);
		}
	}

	public void plotClusters() {
		for (int i = 0; i < this.numClusters; i++) {
			Cluster cl = clusters.get(i);
			System.out.println("Centroid: " + cl.getCentroid() + "Size: " + cl.getQuestions().size());
			plotQuestions(cl.getQuestions(), cl.getId());
		}
	}
	
	public void plotQuestions(List<Question> questions, Integer id) {
		FileWriter arquivo;
			try {  
	            arquivo = new FileWriter(new File("/home/marcos/clusters/" + new Date().toString() + "cluster " + id.toString() +".txt"));  
	            for(Question q : questions){
	            	arquivo.write(q.getTitle());
	            	arquivo.write("\n");
	            }  
	            arquivo.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }	
	}
	
	public void clearClusters() {
		for (Cluster cluster : this.clusters) {
			cluster.clearCluster();
		}
	}

	public List<Question> getCentroids() {
		List<Question> centroids = new ArrayList<Question>(this.numClusters);
		for (Cluster cluster : this.clusters) {
			Question aux = cluster.getCentroid();
			Question question = new Question(aux.getTitle(), aux.getId());
			question.setClusterId(aux.getClusterId());
			centroids.add(question);
		}
		return centroids;
	}

	public void assignCluster() {
		Double max = Double.MAX_VALUE;
		Double min = max;
		LevenshteinDistance levenshtein = new LevenshteinDistance();
		this.clearClusters();
		List<Question> centroids = this.getCentroids();
		int clusterId = -1;
		for (int i = 0; i < this.questions.size(); i++) {
			for (int j = 0; j < centroids.size(); j++) {
				Double distance = levenshtein.calculateDistance(questions.get(i), centroids.get(j));
				if (distance < min) {
					min = distance;
					clusterId = centroids.get(j).getClusterId();
				}
			}
			this.questions.get(i).setClusterId(clusterId);
			getClusterById(clusterId).addQuestion(this.questions.get(i));
			clusterId = -1;
			min = max;
		}
	}
	
	public List<Question> calculateCentroids() {
		LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
		double mean;
		double max = Double.POSITIVE_INFINITY;
		double dist = max;
		List<Question> centroids = new ArrayList<>();
		for (int i = 0; i < clusters.size(); i++) {
			List<Question> questions = clusters.get(i).getQuestions();
			questions.add(clusters.get(i).getCentroid());
			for (int j = 0; j < questions.size(); j++) {
				double sumDistance = 0;
				for (int k = 0; k < questions.size(); k++) {
					if (!questions.get(j).equals(questions.get(k))) {
						sumDistance += levenshteinDistance.calculateDistance(questions.get(j), questions.get(k));
					}
				}
				mean = sumDistance / (questions.size() - 1);

				if (mean < dist) {
					dist = mean;
					clusters.get(i).setCentroid(questions.get(j));
				}
			}
			clusters.get(i).getCentroid().setClusterId(clusters.get(i).getId());
			centroids.add(clusters.get(i).getCentroid());
			dist = max;
		}
		return centroids;
	}

	public Double calculateCentroidsPercentVariation(List<Question> oldCentroids, List<Question> newCentroids) {
		double distance = 0.0;
		LevenshteinDistance levenshtein = new LevenshteinDistance();
		for (int i = 0; i < oldCentroids.size(); i++) {
			distance += levenshtein.calculateDistance(oldCentroids.get(i), newCentroids.get(i));
		}
		double mean = (distance / oldCentroids.size());
		return mean;
	}

	public int getNumClusters() {
		return numClusters;
	}

	public void setNumClusters(int numClusters) {
		this.numClusters = numClusters;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Cluster> getClusters() {
		return this.clusters;
	}

	public Cluster getClusterById(int id) {
		for (int i = 0; i < this.clusters.size(); i++) {
			if (this.clusters.get(i).getId() == id) {
				return this.clusters.get(i);
			}
		}
		return null;
	}

	public void calculate() {
		int i = 0;
		System.err.println("Comecei a calcular..");
		while (i < this.getNumClusters()) {
			this.assignCluster();
			// oldCentroids = getCentroids();
			System.err.println("Inicio da " + i + " iteração");
			this.plotClusters();
			// newCentroids = this.calculateCentroids();
			// this.calculateCentroidsPercentVariation(oldCentroids,
			// newCentroids);
			System.err.println("Fim da " + i + " iteração");
			calculateCentroids();
			i++;
		}
	}
}