package aed.recursion;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.indexedlist.*;
import es.upm.aedlib.positionlist.*;


public class Utils {

  public static int multiply(int a, int b) {  
    if(a>0)
      return multiplyRec(a, b, 0);
    else
      return -multiplyRec(a, b, 0);
  }

  public static int multiplyRec(int a, int b, int suma){
    if(a==0)
      return suma;
    else if(a%2 != 0)
      return multiplyRec(a/2, b*2, suma+b);
    else 
      return multiplyRec(a/2, b*2, suma);
  }

  public static <E extends Comparable<E>> int findBottom(IndexedList<E> l) {
    return findBottomRec(l, 0, l.size()-1);
  }

  public static <E extends Comparable<E>> int findBottomRec(IndexedList<E> l, int start, int end) {
    int medio;
    int size = end-start + 1; 
    if(size==1)
      return end;
    else if(size==2){
      if(l.get(start).compareTo(l.get(end)) <= 0)
        return start;
      else
        return end;
    }
    else{ 
      medio = (start + end) / 2;
      if(l.get(medio).compareTo(l.get(medio+1)) <= 0 && l.get(medio).compareTo(l.get(medio-1)) <= 0)
        return medio;
      else
        if(l.get(medio).compareTo(l.get(medio+1)) >= 0)
          return findBottomRec(l, medio, end);
        else
          return findBottomRec(l, start, medio);
    }
  }
	
  public static <E extends Comparable<E>> NodePositionList<Pair<E,Integer>>
    joinMultiSets(NodePositionList<Pair<E,Integer>> l1,
		  NodePositionList<Pair<E,Integer>> l2) {
    return joinMultiSetsRec(l1, l2, l1.first(), l2.first());
  }

  public static <E> NodePositionList<E> copiar (NodePositionList<E> l, Position<E> cr){
    NodePositionList<E> res;
    if(cr==null)
      return new NodePositionList<E>();
    else{
      res = copiar(l, l.next(cr));
      res.addFirst(cr.element());
      return res;
    }
  }

  public static <E extends Comparable<E>> NodePositionList<Pair<E,Integer>>
    joinMultiSetsRec(NodePositionList<Pair<E,Integer>> l1,
		  NodePositionList<Pair<E,Integer>> l2, Position<Pair<E,Integer>>  cr1, Position<Pair<E,Integer>>  cr2) {
    if(cr1==null)
        return copiar(l2,cr2);
    else if(cr2==null)
        return copiar(l1,cr1);
    else{
        NodePositionList<Pair<E,Integer>> res;
        if(cr1.element().getLeft().compareTo(cr2.element().getLeft())<0){
          res = joinMultiSetsRec(l1, l2, l1.next(cr1), cr2);
          res.addFirst(cr1.element());
          return res;
        }
        else if(cr1.element().getLeft().compareTo(cr2.element().getLeft())>0){
          res = joinMultiSetsRec(l1, l2, cr1, l2.next(cr2));
          res.addFirst(cr2.element());
          return res;
        }
        else {
          res = joinMultiSetsRec(l1, l2, l1.next(cr1), l2.next(cr2));
          res.addFirst(new Pair<E,Integer>(cr1.element().getLeft(),cr1.element().getRight()+cr2.element().getRight()));
          return res;
        }

    }   
  }
}
