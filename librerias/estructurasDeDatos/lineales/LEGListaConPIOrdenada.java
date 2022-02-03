package librerias.estructurasDeDatos.lineales;
import librerias.estructurasDeDatos.modelos.ListaConPI;
public class LEGListaConPIOrdenada<E extends Comparable> 
extends LEGListaConPI<E> implements ListaConPI<E> {
    public void insertar(E e) {
        this.inicio();
        while(!esFin() && this.recuperar().compareTo(e) < 0) {
            this.siguiente();
        }
        super.insertar(e);
    }
}
