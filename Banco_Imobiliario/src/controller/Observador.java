package controller;

/**
 * Observer that receives events of type E.
 */
public interface Observador<E> {
	void notify(E event);
}