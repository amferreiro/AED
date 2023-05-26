package aed.filter;

import java.util.Iterator;
import java.util.function.Predicate;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;


public class Utils {

  public static <E> Iterable<E> filter(Iterable<E> d, Predicate<E> pred) {
    if(eqNull(d, null)){
      throw new IllegalArgumentException();
    }  
    
    PositionList<E> listaresultado = new NodePositionList<E>();
		Iterator<E> it = d.iterator();
    E elem;
        
    while(it.hasNext()){
      elem=it.next();
      if(!eqNull(elem, null) && pred.test(elem))
        listaresultado.addLast(elem);
    }
    return listaresultado;
  }


  private static boolean eqNull(Object o1, Object o2) {
		return o1 == o2 || o1 != null && o1.equals(o2);
	}
}

