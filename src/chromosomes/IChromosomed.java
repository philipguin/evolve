package chromosomes;
import java.util.List;


public interface IChromosomed<C, T extends IChromosomed<C, T>>
{
	public List<? extends C> getChromosomes();
	public ChromosomedType<C, ? extends T> getChromosomedType();
}
