package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {

	private SerieADAO dao;
	private Graph<Integer, DefaultWeightedEdge> graph;
	private List<SeasonForTeam> seasonsForTeam;
	
	private List<SeasonForTeam> bestPath;
 	
	public Model() {
		this.dao = new SerieADAO();
	}
	
	public List<SeasonForTeam> selectedTeam(String team) {
		Map<Integer, Integer> wins = this.dao.getWinsForTeam(team);
		Map<Integer, Integer> draws = this.dao.getDrawsForTeam(team);
		
		this.seasonsForTeam = new ArrayList<>();
		
		for(Integer season : wins.keySet()) {
			SeasonForTeam sft = new SeasonForTeam(team, season, null);
			sft.calculatePoints(wins.get(season), draws.get(season));
			this.seasonsForTeam.add(sft);
		}
		Collections.sort(this.seasonsForTeam);
		return this.seasonsForTeam;
	}
	
	public void createGraph() {
		this.graph = new SimpleDirectedWeightedGraph(DefaultWeightedEdge.class);
		for(SeasonForTeam sft : this.seasonsForTeam) {
			this.graph.addVertex(sft.getSeason());
		}
		for(SeasonForTeam sft1 : this.seasonsForTeam) {
			for(SeasonForTeam sft2 : this.seasonsForTeam) {
				if(! sft1.equals(sft2)) {
					Integer weight = sft1.getPoints() - sft2.getPoints();
					if(weight > 0) {
						Graphs.addEdge(this.graph, sft1.getSeason(), sft2.getSeason(), weight);
					}
				}
			}
		}
	}
	
	public Map<Integer, Integer> getAnnataDoro() {
		Map<Integer, Integer> annateDoro = new HashMap<>();
		Integer annataDoro = null;
		Integer bestWeight = 0;
		for(SeasonForTeam sft : this.seasonsForTeam) {
			Integer weight = 0;
			for(Integer season : Graphs.predecessorListOf(this.graph, sft.getSeason())) {
				weight -= (int) this.graph.getEdgeWeight(this.graph.getEdge(season, sft.getSeason()));
			}
			for(Integer season : Graphs.successorListOf(this.graph, sft.getSeason())) {
				weight += (int) this.graph.getEdgeWeight(this.graph.getEdge(sft.getSeason(), season));
			}
			if(weight > bestWeight) {
				bestWeight = weight;
				annataDoro = sft.getSeason();
			}
		}
		annateDoro.put(annataDoro, bestWeight);
		return annateDoro;
	}
	
	public List<SeasonForTeam> camminoVirtuoso() {
		this.bestPath = new ArrayList<>();
		List<SeasonForTeam> parziale = new ArrayList<>();
		parziale.add(this.seasonsForTeam.get(0));
		recursion(parziale, 0);
		return this.bestPath;
	}
	
	private void recursion(List<SeasonForTeam> parziale, Integer indexLastSeason) {
		if(parziale.size() > this.bestPath.size()) {
			this.bestPath = new ArrayList<>(parziale);
		}
		
		if(indexLastSeason == this.seasonsForTeam.size()-1) 
			return;
		
		if(this.seasonsForTeam.get(indexLastSeason+1).getPoints() > 
				this.seasonsForTeam.get(indexLastSeason).getPoints()) {
			parziale.add(this.seasonsForTeam.get(indexLastSeason+1));
			recursion(parziale, indexLastSeason+1);
		}
		else {
			parziale = new ArrayList<>();
			parziale.add(this.seasonsForTeam.get(indexLastSeason+1));
			recursion(parziale, indexLastSeason+1);
		}
		
	}

	public String getTeamOfCalculatedList() {
		if(this.seasonsForTeam != null)
			return this.seasonsForTeam.get(0).getTeam();
		return null;
	}
	
	public List<String> getAllTeams() {
		return this.dao.getAllTeams();
	}
}
