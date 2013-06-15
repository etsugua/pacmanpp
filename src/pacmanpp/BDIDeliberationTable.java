/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanpp;

public class BDIDeliberationTable
{
	private int [][][] table = new int [6][6][];

	private static BDIDeliberationTable instance = null;
	
	public static BDIDeliberationTable getInstance()
	{
		if (instance == null)
			instance = new BDIDeliberationTable();
		return instance;
	}
	
	protected BDIDeliberationTable()
	{
		int [] a00 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_PACMAN,Constants.BDI_SHOOT_PACMAN};
		table[Constants.BDI_SEE_PACMAN][Constants.BDI_SEE_PACMAN] = a00;
			
		System.out.println(""+table[Constants.BDI_SEE_PACMAN][Constants.BDI_SEE_PACMAN][Constants.BDI_ENERGY_HIGH]);
		
		
		int [] a01_10 = {-1, -1, -1};
		table[Constants.BDI_SEE_PACMAN][Constants.BDI_PACMAN_CLOSE] =
		table[Constants.BDI_PACMAN_CLOSE][Constants.BDI_SEE_PACMAN] = a01_10;
			
		int [] a02_20 = {Constants.BDI_SPLIT, Constants.BDI_SHOOT_PACMAN, Constants.BDI_SHOOT_PACMAN};
		table[Constants.BDI_SEE_PACMAN][Constants.BDI_SEE_BLUE] =
		table[Constants.BDI_SEE_BLUE][Constants.BDI_SEE_PACMAN] = a02_20;
		
		int [] a03_30 = {Constants.BDI_SPLIT, Constants.BDI_SHOOT_PACMAN, Constants.BDI_SHOOT_PACMAN};
		table[Constants.BDI_SEE_PACMAN][Constants.BDI_SEE_GHOST] =
		table[Constants.BDI_SEE_GHOST][Constants.BDI_SEE_PACMAN] = a03_30;
		
		int [] a04_40 = {Constants.BDI_SPLIT, Constants.BDI_SHOOT_PACMAN, Constants.BDI_SHOOT_PACMAN};
		table[Constants.BDI_SEE_PACMAN][Constants.BDI_BLUE_CLOSE] =
		table[Constants.BDI_BLUE_CLOSE][Constants.BDI_SEE_PACMAN] = a04_40;
		
		int [] a05_50 = {Constants.BDI_SPLIT, Constants.BDI_SHOOT_PACMAN, Constants.BDI_SHOOT_CRYSTAL};
		table[Constants.BDI_SEE_PACMAN][Constants.BDI_SEE_CRYSTAL] =
		table[Constants.BDI_SEE_CRYSTAL][Constants.BDI_SEE_PACMAN] = a05_50;
		
		int [] a11 = {Constants.BDI_SPLIT,Constants.BDI_SPLIT,Constants.BDI_MOVE_PACMAN};
		table[Constants.BDI_PACMAN_CLOSE][Constants.BDI_PACMAN_CLOSE] = a11;
		
		int [] a12_21 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_BLUE,Constants.BDI_JOIN};
		table[Constants.BDI_PACMAN_CLOSE][Constants.BDI_SEE_BLUE] =
		table[Constants.BDI_SEE_BLUE][Constants.BDI_PACMAN_CLOSE] = a12_21;
		
		int [] a13_31 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_GHOST,Constants.BDI_SHOOT_GHOST};
		table[Constants.BDI_PACMAN_CLOSE][Constants.BDI_SEE_GHOST] =
		table[Constants.BDI_SEE_GHOST][Constants.BDI_PACMAN_CLOSE] = a13_31;
		
		int [] a14_41 = {Constants.BDI_SPLIT,Constants.BDI_MOVE_PACMAN,Constants.BDI_MOVE_BLUE};
		table[Constants.BDI_PACMAN_CLOSE][Constants.BDI_BLUE_CLOSE] =
		table[Constants.BDI_BLUE_CLOSE][Constants.BDI_PACMAN_CLOSE] = a14_41;
		
		int [] a15_51 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_CRYSTAL,Constants.BDI_SHOOT_CRYSTAL};
		table[Constants.BDI_PACMAN_CLOSE][Constants.BDI_SEE_CRYSTAL] =
		table[Constants.BDI_SEE_CRYSTAL][Constants.BDI_PACMAN_CLOSE] = a15_51;
		
		int [] a22 = {Constants.BDI_JOIN,Constants.BDI_JOIN,Constants.BDI_JOIN};
		table[Constants.BDI_SEE_BLUE][Constants.BDI_SEE_BLUE] = a22;
		
		int [] a23_32 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_GHOST,Constants.BDI_JOIN};
		table[Constants.BDI_SEE_BLUE][Constants.BDI_SEE_GHOST] =
		table[Constants.BDI_SEE_GHOST][Constants.BDI_SEE_BLUE] = a23_32;
		
		int [] a24_42 = {-1, -1, -1};
		table[Constants.BDI_SEE_BLUE][Constants.BDI_BLUE_CLOSE] =
		table[Constants.BDI_BLUE_CLOSE][Constants.BDI_SEE_BLUE] = a24_42;
		
		int [] a25_52 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_CRYSTAL,Constants.BDI_SHOOT_CRYSTAL};
		table[Constants.BDI_SEE_BLUE][Constants.BDI_SEE_CRYSTAL] =
		table[Constants.BDI_SEE_CRYSTAL][Constants.BDI_SEE_BLUE] = a25_52;
		
		int [] a33 = {Constants.BDI_SHOOT_GHOST,Constants.BDI_SHOOT_GHOST,Constants.BDI_MOVE};
		table[Constants.BDI_SEE_GHOST][Constants.BDI_SEE_GHOST] = a33;
		
		int [] a34_43 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_GHOST,Constants.BDI_MOVE_BLUE};
		table[Constants.BDI_SEE_GHOST][Constants.BDI_BLUE_CLOSE] =
		table[Constants.BDI_BLUE_CLOSE][Constants.BDI_SEE_GHOST] = a34_43;
		
		int [] a35_53 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_CRYSTAL,Constants.BDI_SHOOT_CRYSTAL};
		table[Constants.BDI_SEE_GHOST][Constants.BDI_SEE_CRYSTAL] =
		table[Constants.BDI_SEE_CRYSTAL][Constants.BDI_SEE_GHOST] = a35_53;
		
		int [] a44 = {Constants.BDI_SPLIT,Constants.BDI_MOVE_BLUE,Constants.BDI_MOVE_BLUE};
		table[Constants.BDI_BLUE_CLOSE][Constants.BDI_BLUE_CLOSE] = a44;
		
		int [] a45_54 = {Constants.BDI_SPLIT,Constants.BDI_SHOOT_CRYSTAL,Constants.BDI_SHOOT_CRYSTAL};
		table[Constants.BDI_BLUE_CLOSE][Constants.BDI_SEE_CRYSTAL] =
		table[Constants.BDI_SEE_CRYSTAL][Constants.BDI_BLUE_CLOSE] = a45_54;
		
		int [] a55 = {Constants.BDI_SHOOT_CRYSTAL,Constants.BDI_SHOOT_CRYSTAL,Constants.BDI_SHOOT_CRYSTAL};
		table[Constants.BDI_SEE_CRYSTAL][Constants.BDI_SEE_CRYSTAL] = a55;
	}
	
	public int deliberate(int key1, int key2, int energyLevel)
	{
		return table[key1][key2][energyLevel];
	}
}
