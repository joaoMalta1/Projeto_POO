package controller;

public interface Observado<T> {
	 void add(Observador<T> o);
	 void remove(Observador<T> o);
	 void notifyObservers();
	 T get(int index); // Mantido para compatibilidade com o código existente
}