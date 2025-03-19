
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.airport.Airport;
import acme.entities.leg.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineManagerDashboard extends AbstractForm {
	// Serialisation identifier -----------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer					ranking; //based on years of experience, the more years, the best ranking
	private Integer					yearsToRetire; //managers retire at 65 years old
	private Double					ratioOfOntimeAndDelayedFlights; // ratio = ontimeFlights/delayedFlights
	private Airport					mostPopularAirport; //basado en el numero de vuelos que tengan al aeropuerto 
	private Airport					lessPopularAirport;// como origen o destino
	private Map<Status, Integer>	numberofLegsByStatus;
	private Money					averageFlightCost;
	private Money					deviationFlightCost;
	private Money					maximumFlightCost;
	private Money					minimumFlightCost;
}
