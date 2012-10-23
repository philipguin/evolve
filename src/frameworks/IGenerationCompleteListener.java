package frameworks;

import java.util.List;

public interface IGenerationCompleteListener<T>
{
	public void onGenerationComplete(List<T> population);
}
