package net.ninecube.saturn;

public interface NameResolver<T> {
	
	public boolean validate(Context context, String name);
	public T resolve(Context context, String name);
	
	
}
