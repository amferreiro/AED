package aed.bancofiel;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.indexedlist.ArrayIndexedList;


/**
 * Implements the code for the bank application.
 * Implements the client and the "gestor" interfaces.
 */
public class BancoFiel implements ClienteBanco, GestorBanco {

  // NOTAD. No se deberia cambiar esta declaracion.
  public IndexedList<Cuenta> cuentas;

  // NOTAD. No se deberia cambiar esta constructor.
  public BancoFiel() {
    this.cuentas = new ArrayIndexedList<Cuenta>();
  }

  // ----------------------------------------------------------------------
  // Anadir metodos aqui ...

@Override
/*
 * getCuentasOrdenadas: ordena de la lista de acuerdo al criterio de comparacion que da el argumento. Como no hemos conseguido que indexedList sea interpretada como Collections la compiamos en un arrayList.
 */
public IndexedList<Cuenta> getCuentasOrdenadas(Comparator<Cuenta> cmp) {
	IndexedList<Cuenta> resultado = new ArrayIndexedList<Cuenta>();
	ArrayList<Cuenta> aux = new ArrayList<Cuenta>();
	for (int i=0;i< cuentas.size();i++){
		aux.add(cuentas.get(i));
	}	
	Collections.sort(aux, cmp);
	for(int i=0;i<aux.size();i++) {
		resultado.add(i, aux.get(i));
	}
	return resultado;
}

@Override
/*
 * crearCuenta: crea una cuenta nueva y la inserta ordenadamente en la lista.
 */
public String crearCuenta(String dni, int saldoIncial) {
	Cuenta c = new Cuenta(dni, saldoIncial);
	insertarOrdenado(c);
	return c.getId();
}

@Override
/*
 * borrarCuenta: elimina una cuenta de la lista indexada si su saldo es 0.
 */
public void borrarCuenta(String id) throws CuentaNoExisteExc, CuentaNoVaciaExc {
	int indice = buscarCuenta(id);
	if (indice < 0)
		throw new CuentaNoExisteExc();
	if(cuentas.get(indice).getSaldo()!=0)
		throw new CuentaNoVaciaExc();
	cuentas.removeElementAt(buscarCuenta(id));
}

@Override
/*
 * ingresarDinero: ingresa una cantidad de dinero en una cuenta.
 */
public int ingresarDinero(String id, int cantidad) throws CuentaNoExisteExc {
	int indice = buscarCuenta(id);
	if(indice < 0)
		throw new CuentaNoExisteExc();
	return cuentas.get(indice).ingresar(cantidad);
}

@Override
/*
 * retirarDinero: retira una cantidad de dinero de una cuenta si hay suficiente saldo.
 */
public int retirarDinero(String id, int cantidad) throws CuentaNoExisteExc, InsuficienteSaldoExc {
	int indice = buscarCuenta(id);
	if(indice < 0)
		throw new CuentaNoExisteExc();
	if(cuentas.get(indice).getSaldo() < cantidad)
		throw new InsuficienteSaldoExc();
	return cuentas.get(indice).retirar(cantidad);
}

@Override
/*
 * consultarSaldo: devuelve el saldo de una cuenta.
 */
public int consultarSaldo(String id) throws CuentaNoExisteExc {
	int indice = buscarCuenta(id);
	if(indice < 0)
		throw new CuentaNoExisteExc();
	return cuentas.get(indice).getSaldo();
}

@Override
/*
 * hacerTransferencia: realiza una transferencia de dinero de una cuenta a otra. Se retira de la cuenta de origen la cantidad y se suma en la cuenta destinataria. 
 */
public void hacerTransferencia(String idFrom, String idTo, int cantidad)
		throws CuentaNoExisteExc, InsuficienteSaldoExc {
	int indiceFrom = buscarCuenta(idFrom);
	int indiceTo = buscarCuenta(idTo);
	if((indiceFrom < 0) || (indiceTo < 0))
		throw new CuentaNoExisteExc();
	if(cuentas.get(indiceFrom).getSaldo() < cantidad)
		throw new InsuficienteSaldoExc();
	cuentas.get(indiceFrom).retirar(cantidad);
	cuentas.get(indiceTo).ingresar(cantidad);
}

@Override
/*
 * getIdCuentas: devuelve una lista indexada de todos aquellos ids que tengan el mismo dni en la cuenta, para ello miramos la lista orginal y metemos los coincidentes en la nueva lista.
 */
public IndexedList<String> getIdCuentas(String dni) {
	IndexedList<String> lista = new ArrayIndexedList<String>();
	for(int i=0;i<cuentas.size();i++){
		if(cuentas.get(i).getDNI().equals(dni))
			lista.add(lista.size(), cuentas.get(i).getId());
	}
	return lista;
}
/*
 * getSaldoCuentas: busca aquellos elemntos en la lista indexada cuyo dni coincide y devuelve el saldo total de todas las cuentas
 */
@Override
public int getSaldoCuentas(String dni) {
		int saldototal = 0;
		for(int i=0;i<cuentas.size();i++) {
			if(cuentas.get(i).getDNI().equals(dni))
				saldototal=saldototal + cuentas.get(i).getSaldo();
		}
		return saldototal;
}
/*Metodo buscarCuenta: el metodo buscarCuenta pasa los datos de la lista indexada a un arrayList para poder hacer la busqueda binaria, ya que no nos dejaba hacer una busqueda binaria en la indexedList.
 * ya que no hemos encontrado la forma de interpretar una indexedList como una colection. 
 */
private int buscarCuenta(String id) { 
	int indice;
	ArrayList<String> aux = new ArrayList<String>();
	for (int i=0;i< cuentas.size();i++){
		aux.add(cuentas.get(i).getId());
	}	
	indice = Collections.binarySearch(aux, id, null );
	return indice;
}
/*
 * binarySearch devuelve la posicion si esta, pero si no esta devuelve la posicion en la que deberia insertarse (negativo) por lo que el indice sera uno menos. Es en esa posicion donde hay que insertarlo.
 */
private void insertarOrdenado(Cuenta c) {
	int indice =buscarCuenta(c.getId());
	if(indice < 0)
		indice = -indice-1;

	cuentas.add(indice, c);
	}

// ----------------------------------------------------------------------
// NOTAD. No se deberia cambiar este metodo.
public String toString() {
  return "banco";
}
  
}



