package processing;


public interface IProcess<R>
{
	public void startProcessing(R resource);
	public boolean process();
	public R finish();
}
