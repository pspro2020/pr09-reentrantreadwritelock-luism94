package codes;

import java.util.concurrent.TimeUnit;

public class Empleado implements Runnable {
	
	private Almacen almacen;
	private int productoID;
	
	public Empleado(Almacen almacen, int productoID) {
		this.almacen = almacen;
		this.productoID = productoID;
	}


	@Override
	public void run() {
		//Mientras no se interrumpa los hilos de los empleados, cada uno se encarga de
		//contar su producto asignado de la lista de productos del supermercado
		while (!Thread.currentThread().isInterrupted()) {
			try {
				almacen.consultarProducto(productoID);
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("El empleado " + productoID + "ha sido interrumpido");
				return;
			} 
		}
	}

}
