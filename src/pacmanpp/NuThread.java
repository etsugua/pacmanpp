/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanpp;

/**
 *
 * @author Auguste
 */
public class NuThread extends Thread
{
	private Runnable r;
	
	public Runnable getRunnable()
	{
		return r;
	}
	
	public NuThread(Runnable runnable)
	{
		super(runnable);
		r = runnable;
	}
	
	@Override
	public void start()
	{
		super.start();
	}
}
