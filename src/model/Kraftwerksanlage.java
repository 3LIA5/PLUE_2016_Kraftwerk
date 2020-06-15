package model;
import java.util.*;
import java.io.*;

@SuppressWarnings("serial")
public class Kraftwerksanlage implements Serializable
{
	private List<Generator> generatoren;
	private boolean running;
	private double windstaerke;

	public Kraftwerksanlage()
	{
		generatoren = new LinkedList<Generator>();
	}
	//----------------------------------- getter ------------------------------------
	public double getWindstaerke()
	{
		return windstaerke;
	}
	public List<Generator> getGeneratoren()
	{
		return generatoren;
	}
	//----------------------------------- setter ------------------------------------
	public void setWindstaerke(double windstaerke) throws KraftwerkException
	{
		if ( windstaerke >= 0 && windstaerke <= 350)
		{
			this.windstaerke = windstaerke;
			if ( windstaerke > 110 )
			{
				alleWindraederEinAus(false);
			}
		}
		else
			throw new KraftwerkException("Falscher Wert("+windstaerke+") für KraftwerkZentrale.setWindstaerke()");
	}
	public void setRunning(boolean running)
	{
		this.running = running;
		for (Generator g : generatoren)
		{
			g.setRunning(running);
		}
	}
	//----------------------------------- add & removes ------------------------------------
	public void addGenerator(Generator genNeu) throws KraftwerkException
	{
		if (genNeu != null)
			if ( !generatoren.contains(genNeu) )
				{
//					for (Generator genAkt : generatoren)
//					{
//						if ( genAkt.getStandort().equals(genNeu.getStandort()) &&  // ungenau !! -> hier wären eine eigene
//							 genAkt.getNennLeistung() == genNeu.getNennLeistung() )// Implementierung von equals() besser
//							throw new KraftwerkException("Fehler!! Diesen Generator ("+genNeu+") gibt es schon in der Collection");
//					}
					generatoren.add(genNeu);
					if (genNeu instanceof Windrad)
						((Windrad)genNeu).setZentrale(this);
				}
				else
					throw new KraftwerkException("Fehler!! Diese Generator-Referenz "+genNeu+" ist schon in der Collection");
		else
			throw new KraftwerkException("Null-Referenz für KraftwerkZentrale.addGenerator");
	}
	public void removeGenerator(int pos) throws KraftwerkException
	{
		if (pos >= 0 && pos < generatoren.size())
			generatoren.remove(pos);
		else 
			throw new KraftwerkException("Falsche Position("+pos+") für KraftwerkZentrale.removeGenerator(pos)");
	}
	public double removeGeneratoren(double nenn) throws KraftwerkException
	{
		if (nenn >= 0.1f)
		{
			double sum = 0f;
			Generator temp;
			Iterator<Generator> it = generatoren.iterator();
			while (it.hasNext())
			{
				temp = it.next();
				if (temp.getNennLeistung() < nenn)
				{
					sum += temp.getNennLeistung();
					it.remove();
				}
			}
			return sum;
		}
		else
			throw new KraftwerkException("Falscher Nennleistung ("+nenn+") für KraftwerkZentrale.removeGenerator(nenn)");
	}
	//----------------------------------- sonstige ------------------------------------
	public void alleWindraederEinAus(boolean running)
	{
		for (Generator g : generatoren)
		{
			if (g instanceof Windrad)
				g.setRunning(running);
		}
	}
	public double berechneGesamtleistung()
	{
		double sum = 0f;
		for (Generator g : generatoren)
			sum += g.berechneLeistung();
		return sum;
	}
	public int anzGeneratorenRunning()
	{
		int anz = 0;
		for (Generator g : generatoren)
			if (g.isRunning() )
					anz ++;
		return anz;
	}
	public void sortLeistung()
	{
		Collections.sort(generatoren, new Generator.LeistungComparator());
	}
	public void sortStandort()
	{
		Collections.sort(generatoren, new Generator.StandortComparator());
	}
	public String toString()
	{
		int nr = 1;
		StringBuilder sb = new StringBuilder(1000);
		sb.append("\nAnlage ").append(running?"läuft":"steht").append(" (derzeit ").append(generatoren.size())
		  .append(" Generatoren, davon ").append(anzGeneratorenRunning()).append(" running)")
		  .append("\nDerzeitige Windgeschwindigkeit:  ").append(windstaerke)
		  .append(" km/h");
		for (Generator g : generatoren)
			sb.append('\n').append("Generator ").append(nr++).append(": ").append(g);
		return sb.toString();
	}
	//------------------------------------------- Files ----------------------------------------
	public void saveGeneratoren(String filename) throws KraftwerkException
	{
		if (filename != null)
		{
			try
			{
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
				oos.writeObject(generatoren);
				oos.close();
			}
			catch (FileNotFoundException e)
			{
				throw new KraftwerkException ("FileNotFoundException beim Aufbau des ObjectOutputStream("+
											  filename+") !!");
			}
			catch (IOException e)
			{
				throw new KraftwerkException ("IOException beim Aufbau des ObjectOutputStream("+
						  filename+") !!");
			} 
		}
		else
			throw new KraftwerkException ("Null-Referenz für Filename bei safeGeneratoren(filename)!!!");
	}
	@SuppressWarnings("unchecked")
	public void loadGeneratoren(String filename) throws KraftwerkException
	{
		if (filename != null)
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
				LinkedList<Generator> temp = (LinkedList<Generator>)ois.readObject();
				for (Generator g : temp)
					addGenerator(g);
				ois.close();
			}
			catch (ClassNotFoundException e)
			{
				throw new KraftwerkException ("ClassNotFoundException beim Einlesen des ObjectInputStream("+
											  filename+") !!");
			}
			catch (FileNotFoundException e)
			{
				throw new KraftwerkException ("FileNotFoundException beim Aufbau des ObjectInputStream("+
											  filename+") !!");
			}
			catch (IOException e)
			{
				throw new KraftwerkException ("IOException beim Aufbau des ObjectInputStream("+
						  filename+") !!");
			} 
		}
		else
			throw new KraftwerkException ("Null-Referenz für Filename bei safeGeneratoren(filename)!!!");
	}
	public void exportWindraeder(String filename) throws KraftwerkException
	{
		if (filename != null)
		{
			try
			{
				BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
				for (Generator g : generatoren)
				{
					if (g instanceof Windrad)
						bw.write(String.format("%-20s%5.2f\n",g.getStandort(),g.getNennLeistung()));
				}
				bw.close();
			}
			catch (IOException e)
			{
				throw new KraftwerkException ("IOException beim Aufbau des BufferedWriters in exportGeneratoren(filename)!!!");
			}
			
		}
		else
			throw new KraftwerkException ("Null-Referenz für Filename bei exportGeneratoren(filename)!!!");
	}
}
