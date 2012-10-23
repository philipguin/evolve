package frameworks;


public class StepLooper
{
	private final IStepper stepper;
	private boolean isRunning;
	
	public StepLooper(IStepper stepper)
	{
		this.stepper = stepper;
	}
	
	public void kill()
	{
		isRunning = false;
	}

	public void run()
	{
		this.isRunning = true;
		
		while (isRunning)
		{
			stepper.step();
		}
	}
}
