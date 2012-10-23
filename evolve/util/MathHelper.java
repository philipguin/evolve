package util;

public abstract class MathHelper
{
	public static final int getBinomialCoefficient(int n, int k)
	{
	    if (k < 0 || k > n)
	        return 0;
	    
	    if (k > n - k) //take advantage of symmetry
	        k = n - k;
	    
	    int c = 1;
	    for (int i = 0; i < k; ++i)
	    {
	        c = c * (n - k + i + 1);
	        c /= i+1;
	    }
	    
	    return c;
	}
}
