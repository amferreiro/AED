package aed.loops;

public class Utils {
	
	/*PRE:a y elem != de null 
	 * max: variable que almacena el numero maximo de ocurrencias consecutivas
	 *seguidos: cuenta el numero de elementos seguidos que hay en el array
	 */	
  public static int maxNumRepeated(Integer[] a, Integer elem)  {
	  int max = 0;
	  int seguidos = 0;
	  for (int i = 0;i<a.length;i++) {
		  if(a[i].equals(elem)) 
			  seguidos++;
		  else
			  seguidos=0;
		  if(seguidos>max)
		  		max=seguidos;
	  }
	  return max;  
  }
}
