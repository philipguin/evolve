package spawners;
import java.util.List;

public interface IMater<T>
{
	public T mate(List<? extends T> parents);
}
