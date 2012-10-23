package processing;

import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

import frameworks.IStepper;



public class ThreadedStepProcessor<T> implements IStepper
{
	private final IResourceProvider<IProcess<T>> processProvider;
	private final IResourceProvider<T> resourceProvider;
	private final Executor executor;
	
	private ProcessRunnable root;
	private final Semaphore stepBarrier = new Semaphore(0);
	private int processCount;
	
	public ThreadedStepProcessor(
			IResourceProvider<IProcess<T>> processCreator,
			IResourceProvider<T> resourceProvider,
			Executor executor)
	{
		this.processProvider = processCreator;
		this.resourceProvider = resourceProvider;
		this.executor = executor;
		
		processCount = 0;
		
		while (processCreator.hasResourceReady() && resourceProvider.hasResourceReady())
		{
			ProcessRunnable oldRoot = root;
			root = new ProcessRunnable(null, oldRoot);
			
			if (oldRoot != null)
				oldRoot.left = root;
			
			executor.execute(root);
			++processCount;
		}
	}
	
	public void step()
	{
		if (processCount > 0)
		{
			synchronized (processLock)
			{
				for (ProcessRunnable runnable = root; runnable != null; runnable = runnable.right)
				{
					runnable.stepTrigger.release();
				}
			}
			
			stepBarrier.acquireUninterruptibly(processCount);
			
			while (processProvider.hasResourceReady() && resourceProvider.hasResourceReady())
			{
				ProcessRunnable oldRoot = root;
				root = new ProcessRunnable(null, oldRoot);
				
				if (oldRoot != null)
					oldRoot.left = root;
				
				executor.execute(root);
				++processCount;
			}
		}
	}
	
	//TODO: if root == null!
	public void finish()
	{
		synchronized (processLock)
		{
			for (ProcessRunnable runnable = root; runnable != null; runnable = runnable.right)
				runnable.kill();
		}
	}
	
	private final Object processLock = new Object();
	
	private final class ProcessRunnable implements Runnable
	{
		public final Semaphore stepTrigger = new Semaphore(0);
		
		private ProcessRunnable left, right;
		
		private IProcess<T> process;
		private boolean kill = false;
		
		public ProcessRunnable(ProcessRunnable left, ProcessRunnable right)
		{
			this.left = left;
			this.right = right;
			
			process = processProvider.getNextResource();
			process.startProcessing(resourceProvider.getNextResource());
		}
		
		public final void kill()
		{
			kill = true;
			stepTrigger.release();
		}
		
		private final void removeSelfFromSideNodes()
		{
			if (left == null)
				root = right;
			else
				left.right = right;
			
			if (right != null)
				right.left = left;
			
			--processCount;
		}
		
		@Override
		public final void run()
		{
			while (true)
			{
				stepTrigger.acquireUninterruptibly();
				
				if (kill)
				{
					T resource = process.finish();

					synchronized (processLock)
					{
						resourceProvider.returnResource(resource);
						processProvider.returnResource(process);
						
						removeSelfFromSideNodes();
					}
					break;
				}

				if (process.process())
				{
					T resource = process.finish();
					
					synchronized (processLock)
					{
						resourceProvider.returnResource(resource);
						processProvider.returnResource(process);

						if (processProvider.hasResourceReady() && resourceProvider.hasResourceReady())
						{
							process = processProvider.getNextResource();
							process.startProcessing(resourceProvider.getNextResource());
						}
						else
						{
							removeSelfFromSideNodes();
							break;
						}
					}
				}
				
				stepBarrier.release();
			}
			
			stepBarrier.release();
		}
	}
}
