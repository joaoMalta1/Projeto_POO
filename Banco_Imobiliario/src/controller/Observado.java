package controller;

/**
 * Observed interface: implementations can add/remove observers and notify them with events.
 */
public interface Observado<E> {
	void add(Observador<E> o);
	void remove(Observador<E> o);
	void notifyObservers(E event);
	E get(int index); // compatibility helper (kept)
}