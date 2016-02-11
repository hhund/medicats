import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.OpsService;

public class Ops2015Test
{
	public static void main(String[] args)
	{
		OpsService opsService = OpsService.getService();
		OpsNodeFactory f = opsService.getNodeFactory("ops20");
		OpsNodeWalker w = f.createNodeWalker();

		w.printTree(System.out);
	}
}
