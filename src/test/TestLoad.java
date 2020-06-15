package test;
import model.KaplanTurbine;
import model.KraftwerkException;
import model.Kraftwerksanlage;
import model.Windrad;


public class TestLoad
{
	public static void main(String[] args)
	{
		try
		{
			Kraftwerksanlage ka = new Kraftwerksanlage();
			KaplanTurbine k1 = new KaplanTurbine("Hopfing",50f,100);
			Windrad w1 = new Windrad("Gramatneusiedel",1f);
			
			ka.addGenerator(w1);
			ka.addGenerator(k1);
//			ka.loadGeneratoren(null);
//			ka.loadGeneratoren("x:\\scratch\\generatoren.ser");
//			ka.loadGeneratoren("c:\\sscratch\\generatoren.ser");
			ka.loadGeneratoren("c:\\scratch\\generatoren_ein.ser");
			ka.setRunning(true);
			ka.setWindstaerke(50.);
			System.out.println("........ done ...............");
			System.out.println(ka);
		}
		catch (KraftwerkException e)
		{
			System.out.println(e.getMessage());
		}
	}

}
