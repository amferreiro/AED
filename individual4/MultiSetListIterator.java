package aed.individual4;

import java.util.Iterator;
import java.util.NoSuchElementException;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;

public class MultiSetListIterator<E> implements Iterator<E> {
  PositionList<Pair<E,Integer>> list;

  Position<Pair<E,Integer>> cursor;
  int counter;
  Position<Pair<E,Integer>> prevCursor;
  boolean hechonext;

  public MultiSetListIterator(PositionList<Pair<E,Integer>> list) {
    this.list = list;
    this.cursor = list.first();
    this.counter = 1;
    this.hechonext = false;
 }

  public boolean hasNext() {
    boolean result;
    if(cursor == null || list.isEmpty() || (cursor == list.last() && counter > cursor.element().getRight()))
      result = false;
    else  
      result = true;
    
    return result;
  } 


  public E next() {
    E result = null;
    if ((cursor != null) && hasNext()){
      hechonext = true;
      result = cursor.element().getLeft();
      prevCursor = cursor;
      if(cursor.element().getRight() <= counter){
        counter=1;
        cursor = list.next(cursor);
      }
      else
          counter++;     
    }
    else
      throw new NoSuchElementException();
    
    return result;
  }
  
  public void remove() {
    if(!hechonext){
      hechonext = false;
      throw new IllegalStateException();
    }
    else{
      hechonext = false;
      Position <Pair<E,Integer>> aux = list.next(prevCursor);
      if(prevCursor.element().getRight()==1){
        list.remove(prevCursor);
        counter = 0;
        cursor = aux;
      }
      else{
        int x = prevCursor.element().getRight()-1;
        prevCursor.element().setRight(x);
        counter--;
      }
    }
  }
}
