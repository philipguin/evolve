package processing;

import java.util.ArrayList;
import java.util.List;

import competitions.ICompetition;
import competitions.ICompetition.IMatch;

import fitness.IFitnessed;
import frameworks.IGenerationCompleteListener;
import frameworks.IGenerationCompleteNotifier;

import reproducers.IReproducer;
import spawners.ICreator;
import spawners.IMater;


public class CompetitiveGeneticAlgorithmProvider<T extends IFitnessed> implements IResourceProvider<IMatch<T>>, IGenerationCompleteNotifier<T>
{
	private final int initialPopulationSize;
	private final ICreator<T> initialCreator;
	private final IMater<T> mater;
	private final IReproducer<T> reproducer;
	private final ICompetition<T> competition;
	
	private List<T> population;
	private int roundCount;
	
	private final List<IGenerationCompleteListener<T>> generationCompleteListeners = new ArrayList<IGenerationCompleteListener<T>>();
	
	public int getRoundCount() { return roundCount; }
	
	public CompetitiveGeneticAlgorithmProvider(
			int initialPopulationSize,
			ICreator<T> initialCreator,
			IMater<T> mater,
			IReproducer<T> reproducer,
			ICompetition<T> competition)
	{
		this.initialPopulationSize = initialPopulationSize;
		this.initialCreator = initialCreator;
		this.mater = mater;
		this.reproducer = reproducer;
		this.competition = competition;
		
		population = new ArrayList<T>(initialPopulationSize);
		reset();
	}

	@Override
	public void addGenerationCompleteListener(IGenerationCompleteListener<T> listener)
	{
		generationCompleteListeners.add(listener);
	}
	
	public void reset()
	{	
		population.clear();
		
		for (int i = 0; i < initialPopulationSize; ++i)
			population.add(initialCreator.create());
		
		roundCount = population.size() * (population.size() - 1) / 2;
		
		competition.begin(population);
	}
	
	@Override
	public boolean hasResourceReady()
	{
		return competition.hasResourceReady();
	}
	
	@Override
	public IMatch<T> getNextResource()
	{
		return competition.getNextResource();
	}
	
	@Override
	public void returnResource(IMatch<T> match)
	{
		competition.returnResource(match);
		
		if (competition.isFinished())
		{
			@SuppressWarnings("unused")
			T champion = competition.concludeCompetition();

			for (IGenerationCompleteListener<T> listener : generationCompleteListeners)
				listener.onGenerationComplete(population);

			population = reproducer.makeNextGeneration(population, mater);
			
			roundCount = population.size() * (population.size() - 1) / 2;
			
			competition.begin(population);
		}
	}
}
