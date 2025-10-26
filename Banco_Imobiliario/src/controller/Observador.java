package controller;

public interface Observador<T> {
	void notify(Observado<T> o);
}