package com.mitch.framework.containers;

public class MathHelper {
	
	private MathHelper() {}
	
	public static double differenceBetweenAngles(double angleA, double angleB)
	{
		angleA = Math.toRadians(angleA);
		angleB = Math.toRadians(angleB);
		
		Vector2d a = new Vector2d(Math.cos(angleA), Math.sin(angleA));
		Vector2d b = new Vector2d(Math.cos(angleB), Math.sin(angleB));
		
		double dot = a.x * b.x + a.y *b.y;
		
		return Math.toDegrees(Math.acos(dot)) * (angleA - angleB < 0 ? -1 : 1);
	}
	
	public static double degreeDifference(double a, double b)
    {
    	
    	double angle = (a - b) % 360;
    	
    	if (angle >= 180) {
    		angle = 360 - angle;
    	}
    	
    	if (angle <= -180) {
    		angle = -360 - angle;
    	}
    	
    	return angle;
    }
	
	public static double convertToAngle(double x, double y)
	{
		return Math.toDegrees(Math.atan2(y, x));
	}
	
	public static double convertToAngle(Vector2d vector)
	{
		return Math.toDegrees(Math.atan2(vector.y, vector.x));
	}
	
	/**
	 * Converts a 4x4 matrix into a size 2 vector
	 * @param matrix the matrix to calculate
	 * @return 
	 */
	public static Vector2d matrixToVector(float[] matrix)
	{
		
		
		return null;
		
	}
	
}
