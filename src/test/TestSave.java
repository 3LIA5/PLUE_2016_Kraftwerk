package test;
import model.KaplanTurbine;
import model.KraftwerkException;
import model.Kraftwerksanlage;
import model.Windrad;


public class TestSave
{
	public static void main(String[] args)
	{
		try
		{
			Kraftwerksanlage ka = new Kraftwerksanlage();
			System.out.println(ka);
			KaplanTurbine k1 = new KaplanTurbine("Gmunden",50,100);
			KaplanTurbine k2 = new KaplanTurbine("Au",10,50);
			KaplanTurbine k3 = new KaplanTurbine("Bad Goisern",50,100);
			Windrad w1 = new Windrad("Sandling",1f);
			Windrad w2 = new Windrad("Traunstein",5f);
			Windrad w3 = new Windrad("Loser",3f);
			
			ka.addGenerator(w3);
			ka.addGenerator(w2);
			ka.addGenerator(k2);
			ka.addGenerator(w1);
			ka.addGenerator(k3);
			ka.addGenerator(k1);
			ka.setRunning(false);
//			ka(null);
//			ka("x:\\scratch\\generatoren_test.ser");
//			ka("D:\\ssscratch\\generatoren_test.ser");
			String filename = "c:\\scratch\\generatoren_aus.ser";
			ka.saveGeneratoren(filename);
			System.out.println("save ("+filename+") done ...............");
			ka.setRunning(true);
			ka.setWindstaerke(100);
			filename = "c:\\scratch\\generatoren_ein.ser";
			ka.saveGeneratoren(filename);
			System.out.println("save ("+filename+") done ...............");
		}
		catch (KraftwerkException e)
		{
			System.out.println(e.getMessage());
		}
	}

}
