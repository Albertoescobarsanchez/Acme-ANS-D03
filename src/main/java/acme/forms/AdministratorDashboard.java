
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.airline.AirlineType;
import acme.entities.airport.OperationalScope;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long				serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Map<OperationalScope, Integer>	numberAirports;
	private Map<AirlineType, Integer>		numberAirlines;
	private Double							ratiowithEmailAndPhoneNumber;
	private Double							ratioActiveAircraft;
	private Double							ratioNonActiveAircraft;
	private Double							reviewsWithScoreAbove5;
	private Integer							countNumberOfReviews;  //number of reviews posted over the last 10 weeks
	private Double							averageNumberOfReviews;
	private Integer							minimumNumberOfReviews;
	private Integer							maximumNumberOfReviews;
	private Double							standardDeviationNumberOfReviews;
}
