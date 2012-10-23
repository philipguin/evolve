package processing;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import frameworks.IStepper;



public class SimpleStepProcessor<T> implements IStepper
{
	private final IResourceProvider<IProcess<T>> processProvider;
	private final IResourceProvider<T> resourceProvider;
	private final List<IProcess<T>> processes = new LinkedList<IProcess<T>>();
	
	public SimpleStepProcessor(
			IResourceProvider<IProcess<T>> processProvider,
			IResourceProvider<T> resourceProvider)
	{
		this.processProvider = processProvider;
		this.resourceProvider = resourceProvider;

		while (processProvider.hasResourceReady() && resourceProvider.hasResourceReady())
		{
			IProcess<T> process = processProvider.getNextResource();
			process.startProcessing(resourceProvider.getNextResource());
			processes.add(process);
		}
	}
	
	public void step()
	{
		ListIterator<IProcess<T>> iterator = processes.listIterator();
		
		while (iterator.hasNext())
		{
			IProcess<T> process = iterator.next();
			
			if (process.process())
			{
				resourceProvider.returnResource(process.finish());
				processProvider.returnResource(process);

				if (processProvider.hasResourceReady() && resourceProvider.hasResourceReady())
				{
					process = processProvider.getNextResource();
					process.startProcessing(resourceProvider.getNextResource());
				}
				else
				{
					iterator.remove();
				}
			}
		}

		while (processProvider.hasResourceReady() && resourceProvider.hasResourceReady())
		{
			IProcess<T> newProcess = processProvider.getNextResource();
			newProcess.startProcessing(resourceProvider.getNextResource());
			processes.add(newProcess);
		}
	}
	
	public void finish()
	{
	}
}
