package processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import frameworks.IStepper;

public class CompositeStepper implements IStepper
{
	private final List<IStepper> steppers;
	
	public CompositeStepper(IStepper... steppers)
	{
		this.steppers = Arrays.asList(steppers);
	}
	
	public CompositeStepper(List<IStepper> steppers)
	{
		this.steppers = new ArrayList<IStepper>(steppers.size());
		Collections.copy(this.steppers, steppers);
	}
	
	public void step()
	{
		for (IStepper stepper : steppers)
			stepper.step();
	}
}
