package aed.indexedlist;
import es.upm.aedlib.indexedlist.*;

public class Utils {
	/*PRE: l != null y elementos de la lista != null
	 * indice: indica la posicion del ultimo elemento insertado.
	 */
  public static <E> IndexedList<E> deleteRepeated(IndexedList<E> l) {
	IndexedList<E> listaresultado = new ArrayIndexedList<E>();
	int indice=0;
	for(int i=0;i<l.size();i++) {
		if(listaresultado.indexOf(l.get(i)) == -1) {
			listaresultado.add(indice,l.get(i));
			indice++;
		}
	}	
    return listaresultado;
  }
}
