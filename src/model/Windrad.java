package model;

@SuppressWarnings("serial")
public class Windrad extends Generator
{
	private Kraftwerksanlage zentrale;
	
	public Windrad(String standort, double nennLeistung) throws KraftwerkException
	{
		super(standort);
		setNennLeistung(nennLeistung);
	}
	public Kraftwerksanlage getZentrale()
	{
		return zentrale;
	}
	//---------------------------- setter ------------------
	public void setNennLeistung(double nennLeistung) throws KraftwerkException
	{
		if (nennLeistung >= .1f && nennLeistung <= 10 )
			super.setNennLeistung(nennLeistung);
		else
			throw new KraftwerkException("Falscher Wert für Windrad.setNennLeistung("+nennLeistung+") !!");

	}
	public void setZentrale(Kraftwerksanlage zentrale) throws KraftwerkException
	{
		if (zentrale != null)
			this.zentrale = zentrale;
		else
			throw new KraftwerkException("Null-Referenz für Windrad.setZentrale");
	}
	//---------------------------- sonstige ------------------
	public double berechneLeistung()
	{
		if (isRunning())
		{
			double diff = zentrale.getWindstaerke()-50f;
			double proz = diff * 2;
			return (getNennLeistung()*(100+proz))/100;
		}
		else
			return 0f;
	}
	public void emergencyDown()
	{
		zentrale.alleWindraederEinAus(false);
	}
	public String toString()
	{
		StringBuilder sb = new StringBuilder(200);
		sb.append("Windrad: " ).append(super.toString()).append(" -> derzeit ")
		  .append(zentrale!=null?berechneLeistung():"keine Zentrale -> 0").append(" MW");
		return sb.toString();
	}
	//----------------------- hashCode & equals ----------------------------
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((zentrale == null) ? 0 : zentrale.hashCode());
		return result;
	}
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Windrad other = (Windrad) obj;
		if (zentrale == null)
		{
			if (other.zentrale != null)
				return false;
		}
		else
			if (!zentrale.equals(other.zentrale))
				return false;
		return true;
	}
}
