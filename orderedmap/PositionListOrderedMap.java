package aed.orderedmap;

import java.security.Key;
import java.util.Comparator;
import java.util.Iterator;

import javax.lang.model.util.ElementScanner6;
import javax.swing.plaf.TreeUI;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

import es.upm.aedlib.Entry;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.positionlist.NodePositionList;

public class PositionListOrderedMap<K,V> implements OrderedMap<K,V> {
    private Comparator<K> cmp;
    private PositionList<Entry<K,V>> elements;
  
    /* Acabar de codificar el constructor */
    public PositionListOrderedMap(Comparator<K> cmp) {
        this.elements = new NodePositionList<Entry<K,V>>();
	    this.cmp = cmp;
    }

    /* Ejemplo de un posible método auxiliar: */
  
    /* If key is in the map, return the position of the corresponding
     * entry.  Otherwise, return the position of the entry which
     * should follow that of key.  If that entry is not in the map,
     * return null.  Examples: assume key = 2, and l is the list of
     * keys in the map.  For l = [], return null; for l = [1], return
     * null; for l = [2], return a ref. to '2'; for l = [3], return a
     * reference to [3]; for l = [0,1], return null; for l = [2,3],
     * return a reference to '2'; for l = [1,3], return a reference to
     * '3'. */

    private Position<Entry<K,V>> findKeyPlace(K key) {
        boolean encontrado = false;
        boolean imposible = false;
        Position<Entry<K,V>> cursor = elements.first();
        if(key == null)
            throw new IllegalArgumentException();
        while(cursor != null && !encontrado && !imposible){
            if(cmp.compare(cursor.element().getKey(), key)==0)
                // Clave encontrada
                encontrado = true;
            else if (cmp.compare(cursor.element().getKey(), key)>0)
                // Encontrada clave superior. El elemento no está
                imposible = true;
            else 
                // Seguimos avanzando
                cursor = elements.next(cursor);
        }
        return cursor;
    }

    /* Podéis añadir más métodos auxiliares */
  
    public boolean containsKey(K key) {
        Position<Entry<K,V>> cursor;
        //boolean result = false;
        if(key == null)
            throw new IllegalArgumentException();
        cursor = findKeyPlace(key);
        // La clave está si se ha encontrado, esto es no se ha acabado la lista sin encontrarlo (null) o su posición es aproximada
        return (cursor != null) && (cursor.element().getKey().equals(key));
    }
  
    public V get(K key) {
        Position<Entry<K,V>> cursor;
        if(key == null)
            throw new IllegalArgumentException();
        cursor = findKeyPlace(key);
        if(cursor == null || !cursor.element().getKey().equals(key))
            //Devolvemos null si la clave no está en la lista
            return null;
        else 
            return cursor.element().getValue();
    }
  
    public V put(K key, V value) {
        Position<Entry<K,V>> cursor;
        V oldvalue = null;
        if(key == null)
            throw new IllegalArgumentException();
        cursor = findKeyPlace(key);
        if(cursor == null)
            //No existe la clave,valor entonces creamos una y la introducimos en la lista
            elements.addLast(new EntryImpl<K,V>(key, value));
        else if(cursor.element().getKey().equals(key)){
            //Existe la clave y guardamos la anterior en una variable que devolveremos, y luego metemos en la lista un nuevo Clave, Valor
            oldvalue = cursor.element().getValue();
            elements.set(cursor, new EntryImpl<K,V>(key, value));
        }
        else 
            //Existe pero no es igual a la Key, a si que la introducimos antes del cursor
            elements.addBefore(cursor, new EntryImpl<K,V>(key, value));
	   return oldvalue;
    }
  
    public V remove(K key) {
        Position<Entry<K,V>> cursor;
        V oldvalue = null;
        if(key == null)
            throw new IllegalArgumentException();
        cursor = findKeyPlace(key);
        if(cursor != null && cursor.element().getKey().equals(key)){
            //Guardamos lo que vamos a eliminar en una variable que devolveremos, y eliminamos. 
            oldvalue = cursor.element().getValue();
            elements.remove(cursor);
        }
	return oldvalue;
    }
  
    public int size() {
	    return elements.size();
    }
  
    public boolean isEmpty() {
        return elements.isEmpty();
    }
  
    public Entry<K,V> floorEntry(K key) {
        Position<Entry<K,V>> cursor = elements.first();
        Entry<K,V> lastentry = null;
        if(key == null)
            throw new IllegalArgumentException();
        if(elements.isEmpty())
            //lista vacia 
            return null;
        if(cmp.compare(cursor.element().getKey(), key) > 0){ 
            //no existe la entry
             return null;
        }
        while(cursor != null){
            //recorremos la lista
            if(cmp.compare(cursor.element().getKey(), key) <= 0){
                //Comparamos la key con los diferentes valores de la lista 
                lastentry = cursor.element();
            }
            cursor = elements.next(cursor);
        }
        return lastentry;
    }
  
    public Entry<K,V> ceilingEntry(K key) {
        Position<Entry<K,V>> cursor = elements.last();
        Entry<K,V> lastentry = null;
        if(key == null)
            throw new IllegalArgumentException();
        if(elements.isEmpty())
            //lista vacia 
            return null;
        if(cmp.compare(cursor.element().getKey(), key) < 0){ 
             //no existe la entry
             return null;
        }
        while(cursor != null){
            //recorremos la lista pero esta vez desde el final
            if(cmp.compare(cursor.element().getKey(), key)>=0){
                //Comparamos la key con los diferentes valores de la lista 
                lastentry = cursor.element();
            }
            cursor = elements.prev(cursor);
        }    
        return lastentry;       
    }
  
    public Iterable<K> keys() {
        PositionList<K> listakeys = new NodePositionList<K>();
        Position<Entry<K,V>> cursor = elements.first();
        K key;
        while(cursor != null){
            //Creamos lista y la vamos recorreindo mientras almacenamos las keys 
            key = cursor.element().getKey();
            listakeys.addLast(key);
            cursor = elements.next(cursor);
        }
        return listakeys;
    } 
  
    public String toString() {
	  return elements.toString();
    } 
}
