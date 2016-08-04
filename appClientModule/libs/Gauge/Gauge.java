package libs.Gauge;

public abstract class Gauge
{
	protected String description;
	protected String lowerScale, upperScale;

	protected int x, y; // upper left corner of gauge
	protected int width, height; // inpixels
	protected float bottomRedPercent, bottomYellowPercent, greenPercent,
			sweetSpotPercent, yellowPercent, redPercent;
	protected float indicatorPosition, indicatorPositionPercent;

	public Gauge()
	{
		// Do nothing..
	}

	public Gauge( String d, String ls, String us, int positionX, int positionY,
			int w, int h, float gp, float yp, float rp, int ip )
	{
		description = d;
		lowerScale = ls;
		upperScale = us;
		x = positionX;
		y = positionY;
		width = w;
		height = h;
		greenPercent = gp;
		yellowPercent = yp;
		redPercent = rp;
		indicatorPosition = ip;
	}

	public Gauge( String d, String ls, String us, int positionX, int positionY,
			int w, float rp, float yp, float gp, float ipp )
	{
		description = d;
		lowerScale = ls;
		upperScale = us;
		x = positionX;
		y = positionY;
		width = w;
		redPercent = rp;
		yellowPercent = yp;
		greenPercent = gp;
		indicatorPositionPercent = ipp;
	}

	public Gauge( String d, String ls, String us, int positionX, int positionY,
			int w, float ipp )
	{
		description = d;
		lowerScale = ls;
		upperScale = us;
		x = positionX;
		y = positionY;
		width = w;
		indicatorPositionPercent = ipp;
	}

	public float getIndicatorPosition()
	{
		return indicatorPosition;
	}

	public void setIndicatorPosition( float newIP )
	{
		indicatorPosition = newIP;
	}

	// returns the width of the green segment in pixels
	protected abstract int greenWidth();

	// returns the width of the yellow segment in pixels
	protected abstract int yellowWidth();

	// returns the width of the red segment in pixels
	protected abstract int redWidth();
}
