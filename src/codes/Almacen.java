package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Almacen implements Runnable{
	
	private List<Producto> stock = new ArrayList<Producto>();
	//Cerrojo de lectura y escritura
	private ReadWriteLock cerrojo = new ReentrantReadWriteLock();
	private Lock cerrojoLectura = cerrojo.readLock();
	private Lock cerrojoEscritura = cerrojo.writeLock();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public void run() {
		//Mientras el hilo del almacen no sea interrumpido ira colocando un producto nuevo elegido
		//aleatoriamente cada 2 segundos
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Random random = new Random();
				int producto = random.nextInt(3) + 1;
				colocarProducto(producto);
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				System.out.println("El almacen se ha interrumpido");
				return;
			} 
		}
	}


	private void colocarProducto(int producto) {
		//Se prepara el cerrojo de escritura y se bloquea la entrada de los demas hilos en el almacen
		cerrojoEscritura.lock();
		//Se añade el producto con el ID recibido por parametro
		try {
			stock.add(new Producto(producto));
			System.out.println(LocalDateTime.now().format(formatter) + " --- El almacen ha añadido un producto nº " + producto);
		} finally {
			//Se desbloquea el cerrojo
			cerrojoEscritura.unlock();
		}
		
	}


	public void consultarProducto(int productoID) {
		int cantidadProducto = 0;
		//Se prepara el cerrojo de lectura y se bloquea la entrada de los demas hilos al almacen
		cerrojoLectura.lock();
		
		try {
			//El empleado cuenta su producto del que es encargado de la lista de productos del almacen
			for (Producto producto : stock) {
				if (producto.getProductoId() == productoID) {
					cantidadProducto++;	
				}
			}
			
			System.out.println(LocalDateTime.now().format(formatter) + " --- El empleado " + productoID + " ha contado " + cantidadProducto + " del producto " + productoID);
		} finally {
			//Se desbloquea el cerrojo
			cerrojoLectura.unlock();
		}
		
	}
}
