
package acme.datatypes;

import java.text.DecimalFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics {

	private DecimalFormat		formatter			= new DecimalFormat("#.##");
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;
	// Attributes -------------------------------------------------------------

	Integer						count;
	Double						average;
	Double						min;
	Double						max;
	Double						deviation;


	public String getAverageString() {
		return this.formatter.format(this.average);
	}

	public String getDeviationString() {
		return this.formatter.format(this.deviation);
	}

	public String getMinimumString() {
		return this.formatter.format(this.min);
	}

	public String getMaximumString() {
		return this.formatter.format(this.max);
	}

	public String getCountString() {
		return String.valueOf(this.count);
	}

}
