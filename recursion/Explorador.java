package aed.recursion;

import java.util.Iterator;

import es.upm.aedlib.Pair;
import es.upm.aedlib.positionlist.*;


public class Explorador {
  
  public static Pair<Object,PositionList<Lugar>> explora(Lugar inicialLugar) {
	PositionList<Lugar> caminovacio = new NodePositionList<Lugar>();
    return exploraRec(inicialLugar,caminovacio);
  }
  
  /*
   * Analizamos los 3 casos posibles, para recorrer el laberinto. 
   *	Casilla con tesoro
   *	Casilla con tiza
   *	Casilla en blanco de manera que llamamos recursivamente a todas las casillas adyacentes que las da el metedo iterator.
   *
   */
  public static Pair<Object,PositionList<Lugar>> exploraRec(Lugar laberinto, PositionList<Lugar> camino){
	  Iterable<Lugar> siguiente;
	  boolean haycamino = false;
	  Pair<Object, PositionList<Lugar>> resultado = new Pair<>(null, null);	
	  camino.addLast(laberinto);
	  if(laberinto.tieneTesoro()) {
		  resultado.setLeft(laberinto.getTesoro());
	  	  resultado.setRight(camino);
	  }
	  else if(laberinto.sueloMarcadoConTiza()) {
		  resultado.setLeft(null);
		  resultado.setRight(camino);  
	  }
	  else {
		  siguiente = laberinto.caminos();
		  Iterator<Lugar> it = siguiente.iterator();
          laberinto.marcaSueloConTiza();
		  while((!haycamino) && it.hasNext()) { 
			  resultado = exploraRec(it.next(),camino);
			  if(resultado.getLeft()!=null) 
			  	  haycamino=true; 
			  else {
				  camino.remove(camino.last());
			  }		  
		  }
	  }
	  return resultado;
  }
}
