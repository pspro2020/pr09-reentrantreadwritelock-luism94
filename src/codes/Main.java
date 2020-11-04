package codes;

import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		//Se crea el objeto del hilo del almacen
		Almacen almacen = new Almacen();
		//Se crean los hilos de los empleados cada uno con un objeto empleado diferente
		Thread[] empleados = new Thread[3];
		for (int i = 0; i < empleados.length; i++) {
			empleados[i] = new Thread(new Empleado(almacen, i + 1));
		}
		//Hilo de ejecucion del almacen
		Thread almacenThread = new Thread(almacen);
		//Se inician los empleados y el almacen
		almacenThread.start();
		
		for (int i = 0; i < 3; i++) {
			empleados[i].start();
		}
				
		try {
			//Despues de un minuto se interrumpen la ejecucion de los hilos y se termina el programa
			TimeUnit.MINUTES.sleep(1);
			for (int i = 0; i < 3; i++) {
				empleados[i].interrupt();
			}
			almacenThread.interrupt();
		} catch (InterruptedException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}
}
