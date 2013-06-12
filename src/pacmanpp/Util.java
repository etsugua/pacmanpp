/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanpp;

/**
 *
 * @author Auguste
 */
public class Util 
{
	
	// convenient workaround
	protected static void simpleTrace(String s)
	{
		try
		{
			throw new Exception(s);
		}
		
		catch (Exception e)
		{ 
			System.err.println("" + Thread.currentThread().getStackTrace()[2].getClassName()+ " ("+Thread.currentThread().getStackTrace()[2].getLineNumber()+") - " + e.toString().split("java.lang.Exception: ")[1]);
		}
	}
	
	protected static int random(int limit)
	{
		double rand = Math.random()*100;
		return (int)(rand%limit);
	}
	
	// position conversion - from map view to world view
	protected static int convertPosition(int index)
	{
		int retValue = (index + 1) * Constants.GRID_UNIT;
		return retValue;
	}
}
