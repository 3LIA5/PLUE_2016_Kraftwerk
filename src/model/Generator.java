package model;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public abstract class Generator implements Serializable
{
	private double nennLeistung;
	private boolean running;
	private String standort;
	
	public Generator(String standort) throws KraftwerkException
	{
		setStandort(standort);
		setRunning(false);
	}
	//---------------------------- getter ------------------
	public double getNennLeistung()
	{
		return nennLeistung;
	}
	public boolean isRunning()
	{
		return running;
	}
	public String getStandort()
	{
		return standort;
	}
	//---------------------------- setter ------------------
	public void setNennLeistung(double nennLeistung) throws KraftwerkException
	{   //Plausibilitätsprüfung erfolgt bei den jeweiligen Sub-Klassen
		this.nennLeistung = nennLeistung;
	}
	public void setRunning(boolean running)
	{
		this.running = running;
	}
	public void setStandort(String standort) throws KraftwerkException
	{
		if (standort != null)
			this.standort = standort;
		else 
			throw new KraftwerkException("Null-Referenz für Generator.setStandort");
	}
	//---------------------------- sonstige ------------------
	public abstract double berechneLeistung();
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder(50);
		if (standort.length() >= 4)
			sb.append(standort.substring(0, 4));
		else
			sb.append(standort);
		sb.append(", Nenn-Leistung ").append(nennLeistung).append(" MW, ");
		if (running)
			sb.append("läuft");
		else
			sb.append("steht");
		return sb.toString();
	}
	//--------------------------- Comparatoren -----------------------------
	public static class LeistungComparator implements Comparator<Generator>
	{
		public int compare(Generator gen1, Generator gen2)
		{
			if (gen1.berechneLeistung() < gen2.berechneLeistung())
				return 1;
			else
				if (gen1.berechneLeistung() > gen2.berechneLeistung())
					return -1;
				else
					return 0;
		}
	}
	public static class StandortComparator implements Comparator<Generator>
	{
		public int compare(Generator gen1, Generator gen2)
		{
				return gen1.getStandort().compareTo(gen2.getStandort());
		}
	}
	//----------------------- hashCode & equals ----------------------------
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(nennLeistung);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (running ? 1231 : 1237);
		result = prime * result + ((standort == null) ? 0 : standort.hashCode());
		return result;
	}
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Generator other = (Generator) obj;
		if (Double.doubleToLongBits(nennLeistung) != Double.doubleToLongBits(other.nennLeistung))
			return false;
		if (running != other.running)
			return false;
		if (standort == null)
		{
			if (other.standort != null)
				return false;
		}
		else
			if (!standort.equals(other.standort))
				return false;
		return true;
	}
}
