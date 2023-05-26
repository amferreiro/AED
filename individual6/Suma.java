package aed.individual6;

import es.upm.aedlib.graph.Edge;
import es.upm.aedlib.graph.Vertex;
import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.graph.DirectedGraph;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.map.HashTableMap;

public class Suma {
	private static <E> Map<Vertex<Integer>,E>iniciarMapa(PositionList<Vertex<Integer>> lista,E v) {
		Map<Vertex<Integer>,E> m = new HashTableMap<Vertex<Integer>,E>();
		Position<Vertex<Integer>> cursor = lista.first();
		for(int i=0; i<lista.size();i++) {
			m.put(cursor.element(),v);	
			cursor=lista.next(cursor);
		} 
		return m;
	}
	private static int buscar (PositionList<Vertex<Integer>> lista, Vertex<Integer> v) {
		int i=0;
		boolean encontrado=false;
		Position<Vertex<Integer>> cursor = lista.first();
		while(!encontrado && i<lista.size()) {
			if(v.equals(cursor.element())) 
				encontrado=true;
			else
				i++;
			cursor=lista.next(cursor);
		}
		return i;
	}
	
	private static <E> boolean [] alcanzables (DirectedGraph<Integer,E> g, Vertex<Integer> v, boolean [] visitados){
		PositionList<Edge<E>> listae = (PositionList<Edge<E>>) g.outgoingEdges(v);
		PositionList<Vertex<Integer>> listav = (PositionList<Vertex<Integer>>) g.vertices();		
		boolean [] alcanzados, alcanzadosaux;
		Position<Edge<E>> cursore = listae.first();
		Edge<E> arista;
		Vertex<Integer> vertice;
		int numVertices=g.numVertices();
		alcanzadosaux = new boolean [numVertices];
		alcanzados = new boolean [numVertices];
		for(int k=0;k<numVertices;k++) {
			alcanzados[k]=false;
		}
		for(int i=0;i<listae.size();i++) {
			arista=cursore.element();
			vertice = g.endVertex(arista);
			int posicion=buscar(listav,vertice);
			alcanzados[posicion]=true;
			if(!visitados[posicion]) {
				visitados[posicion]=true;
				alcanzadosaux = alcanzables(g,vertice,visitados);
				for(int j=0;j<numVertices;j++) {
					alcanzados[j]=alcanzados[j] || alcanzadosaux[j];
				}
			}	
			cursore=listae.next(cursore);
		}	
		return alcanzados;
	}
	
	public static <E> Map<Vertex<Integer>,Integer> sumVertices(DirectedGraph<Integer,E> g) {
		Map<Vertex<Integer>,Integer> mapa = null;
		boolean [] visitados,alcanzados;
		int suma;
		PositionList<Vertex<Integer>> listav = (PositionList<Vertex<Integer>>) g.vertices();
		PositionList<Vertex<Integer>> listavaux = (PositionList<Vertex<Integer>>) g.vertices();
		Position<Vertex<Integer>> cursorv = listav.first();
		Position<Vertex<Integer>> cursorvaux = listav.first();
		int numVertices=g.numVertices();
		Vertex<Integer> origen,destino;
		mapa = iniciarMapa(listav,0);
		visitados = new boolean [numVertices];
		alcanzados = new boolean [numVertices];		
		for(int i=0;i<numVertices;i++) {
			for(int o=0;o<numVertices;o++) {
				alcanzados[o]=false;	
			}
			for(int k=0;k<numVertices;k++) {
				visitados[k]=false;	
			}
			visitados[i] = true;
			origen=cursorv.element();
			alcanzados = alcanzables(g,origen,visitados);
			cursorvaux = listav.first();
			suma=origen.element();
			for(int j=0;j<numVertices;j++) {
				destino=cursorvaux.element();
				if(alcanzados[j] && !origen.equals(destino)) {
					suma=suma+destino.element();
				}
				cursorvaux=listavaux.next(cursorvaux);
			}
			mapa.put(origen, suma);
			cursorv=listav.next(cursorv);
		}
		return mapa;
	}
}
