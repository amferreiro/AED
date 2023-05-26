package aed.hashtable;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Arrays;

import es.upm.aedlib.Entry;
import es.upm.aedlib.EntryImpl;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.InvalidKeyException;


/**
 * A hash table implementing using open addressing to handle key collisions.
 */
public class HashTable<K,V> implements Map<K,V> {
  Entry<K,V>[] buckets;
  int size;


  public HashTable(int initialSize) {
    this.buckets = createArray(initialSize);
    this.size = 0;

  }

public int size (){
  return size;
}

public boolean isEmpty (){
  return size==0;
}

public V put (K key, V value){
  int pos = search(key);
  V anterior = null;
  //Si no hay espacio hacemos el rehash
  if(pos==-1) {
    rehash();
  }
  //buscamos la key 
  pos = search(key);

  if(buckets[pos]!=null)
    //guardamos el elemento para devolverlo
    anterior = buckets[pos].getValue();
  else
    size++;
  //creamos el entry y lo anadimos
  buckets[pos] = new EntryImpl<K,V>(key, value);
  return anterior;
}

public V get (K key){
  //buscamos la key
  int pos = search(key);
  if (key == null)
    throw new InvalidKeyException();
  if(pos == -1 || buckets[pos]==null)
    return null;
  else
    //devolvemos el elemento
    return buckets[pos].getValue();
}


public boolean containsKey (Object key){
  if (key == null)
    throw new IllegalArgumentException();
 //buscamos la key
  int pos = search(key); 
  if(pos == -1 || buckets[pos]==null)
    return false;
  else
    //true si encontramos la key
    return buckets[pos].getKey().equals(key);
}

public V remove (K key) throws InvalidKeyException{
  int pos = search(key);
  V anterior = null;
  int start = pos;
  if (key == null)
    throw new IllegalArgumentException();
  if(pos==-1)
    return null;
  if(buckets[pos]!=null){
    anterior = buckets[pos].getValue();
    buckets[pos] = null;
    size--;
  }
  int i = (start+1)%buckets.length;
  int preferido;
  boolean cerca = false;
  while(i!=start && buckets[i]!=null){
    preferido = index(buckets[i].getKey());
    if(i>=preferido)
      cerca = preferido<=pos && pos<i;
    else
      cerca = pos>=preferido || pos<i;
    if(cerca){
      buckets[pos]=buckets[i];
      buckets[i]=null;
      pos=i;
    }
    i=(i+1)%buckets.length;
  }
  return anterior;
}

public Iterable <K> keys (){
  //creamos lista la recorremos y añadimos las keys a la nueva lista
  PositionList<K> lista = new NodePositionList<K>();	 
  for(int i=0;i<buckets.length;i++){
    if(buckets[i]!=null)
      lista.addLast(buckets[i].getKey());
  } 
  return lista;
}


public Iterable <Entry <K,V>> entries (){
  //creamos lista la recorremos y añadimos las entry a la nueva lista
  PositionList<Entry<K,V>> lista = new NodePositionList<Entry<K,V>>();	 
  for(int i=0;i<buckets.length;i++){
    if(buckets[i]!=null)
      lista.addLast(buckets[i]);
  } 
  return lista;
}

public Iterator <Entry<K,V>> iterator(){
  return entries().iterator();
}


  /**
   * Add here the method necessary to implement the Map api, and
   * any auxilliary methods you deem convient.
   */

  // Examples of auxilliary methods: IT IS NOT REQUIRED TO IMPLEMENT THEM
  
  @SuppressWarnings("unchecked") 
  private Entry<K,V>[] createArray(int size) {
   Entry<K,V>[] buckets = (Entry<K,V>[]) new Entry[size];
   return buckets;
  }

  // Returns the bucket index of an object
  private int index(Object obj) {
    return obj.hashCode()%buckets.length;
  }

  // Returns the index where an entry with the key is located,
  // or if no such entry exists, the "next" bucket with no entry,
  // or if all buckets stores an entry, -1 is returned.
  private int search(Object obj) {
    boolean encontrado = false;
    int indice = index(obj);
    int anterior;
    //limites de recorrido circular
    if(indice==0)
      anterior=buckets.length-1;
    else
      anterior=indice-1;
    while (!encontrado && (indice!=anterior)){
        //si no hay entrys, salimos del bucle
        if(buckets[indice]==null)
          encontrado = true;
        //si encontramos la entry, salimos del bucle
        else if(buckets[indice].getKey().equals(obj))
          encontrado = true;
        else
          //recorrido circular
          indice = (indice+1)%buckets.length;
    }
    if(!encontrado)
      return -1;
    else
      return indice;
  }

  // Doubles the size of the bucket array, and inserts all entries present
  // in the old bucket array into the new bucket array, in their correct
  // places. Remember that the index of an entry will likely change in
  // the new array, as the size of the array changes.
  private void rehash() {
    HashTable<K,V> newtable = new HashTable<K,V>(buckets.length*2);
    //recorremos la tabla anteorior
    for(int i=0;i<buckets.length;i++){
      if(buckets[i]!=null)
        //insertamos en la nueva
        newtable.put(buckets[i].getKey(), buckets[i].getValue());
    }
    buckets = newtable.buckets; 
  }
  
}

