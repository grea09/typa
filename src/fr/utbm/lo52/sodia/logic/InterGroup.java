package fr.utbm.lo52.sodia.logic;

public interface InterGroup<E extends InterGroup<?>>
{
	public void add(E element);

	public void remove(E element);
}
