package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> idMap;
	private List<Business> percorsoBest;
	private List<Business> elencoVertici;
	private int numLocaliBest = 99999;
	private Business localeBest = null;
	
	public Model() {
		dao = new YelpDao();		
		idMap = new HashMap<>();
		dao.getAllBusiness(idMap);
	}
	
	public List<String> getCitta() {
		return dao.listaCitta();
	}
	
	public List<Integer> getAnno() {
		return dao.listaAnni();
	}
	
	public List<Business> getBusinesses() {
		return elencoVertici;
	}
	
	public void creaGrafo(String citta, int anno) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(citta, anno));
		elencoVertici = dao.getVertici(citta, anno);
		for(ArcoGrafo arco : dao.calcolaArchi(citta, anno)) {
			if(grafo.containsVertex(idMap.get(arco.getBusinessId1())) && grafo.containsVertex(idMap.get(arco.getBusinessId2()))) {
				if(arco.getMedia1() > arco.getMedia2() && arco.getMedia1() != 0 && arco.getMedia2() != 0) {
					Double diffDouble = arco.getMedia1() - arco.getMedia2();
					arco.setPeso(diffDouble);
					Graphs.addEdgeWithVertices(grafo, idMap.get(arco.getBusinessId2()), idMap.get(arco.getBusinessId1()), diffDouble);
				}
				
				if(arco.getMedia1() < arco.getMedia2() && arco.getMedia1() != 0 && arco.getMedia2() != 0) {
					Double diffDouble = arco.getMedia2() - arco.getMedia1();
					arco.setPeso(diffDouble);
					Graphs.addEdgeWithVertices(grafo, idMap.get(arco.getBusinessId1()), idMap.get(arco.getBusinessId2()), diffDouble);
				}
			}
		}
	}
	
	public String calcolaMigliore() {
		int migliore = -9999;		
		
		for(Business business : grafo.vertexSet()) {
			Set<DefaultWeightedEdge> archiING = grafo.incomingEdgesOf(business);
			Set<DefaultWeightedEdge> archiOUT = grafo.outgoingEdgesOf(business);
			int sommaING = 0;
			int sommaOUT = 0;
			for(DefaultWeightedEdge arco : archiING) {
				sommaING += grafo.getEdgeWeight(arco);
			}
			for(DefaultWeightedEdge arco : archiOUT) {
				sommaOUT += grafo.getEdgeWeight(arco);
			}
			
			int diff = sommaING - sommaOUT;
			if(diff > migliore) {
				migliore = diff;
				localeBest = business;
			}
		}
		
		return localeBest.getBusinessName();
	}
	
	public Integer numArchi() {
		return grafo.edgeSet().size();
	}
	
	public Integer numVertici() {
		return grafo.vertexSet().size();
	}
	
	public List<Business> calcolaPercorso(Business localeInput, double soglia) {
		this.percorsoBest = null ;
		
		List<Business> parziale = new ArrayList<Business>() ;
		parziale.add(localeInput) ;
		
		cerca(parziale, 1, localeBest, soglia) ;
		
		return this.percorsoBest ;
	}

	private void cerca(List<Business> parziale, int livello, Business arrivo, double soglia) {
		// Il livello rappresenta i locali visitati
		
		// Prendo l'ultimo vertice inserito nel grafo
		Business ultimo = parziale.get(parziale.size()-1);
		
		if(ultimo.equals(arrivo)) {
			if(livello < numLocaliBest) {
				numLocaliBest = livello;
				percorsoBest = new ArrayList<>(parziale);
			}
		}
		else {			
			Set<DefaultWeightedEdge> archi = grafo.outgoingEdgesOf(ultimo); // Mi prendo tutti gli archi uscenti dall'ultimo vertice presente nella lista
			for(DefaultWeightedEdge arco : archi) {
				if(grafo.getEdgeWeight(arco) >= soglia) {
					Business prossimo = Graphs.getOppositeVertex(grafo, arco, ultimo);
					if(!parziale.contains(prossimo)) {
						parziale.add(prossimo);
						cerca(parziale, livello+1, arrivo, soglia);
						parziale.remove(parziale.size()-1);
					}
				}
			}
		}
		
	}

}
