package frameworks;

public interface IGenerationCompleteNotifier<T>
{
	public void addGenerationCompleteListener(IGenerationCompleteListener<T> listener);
}
