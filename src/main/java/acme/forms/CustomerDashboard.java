
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.booking.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>				lastFiveDestinations;
	private Money						moneyInBookings;     // money spent in Bookings during the last year
	private Map<TravelClass, Integer>	bookingsByTravelClass;    // their number of bookings grouped by travel class
	private Money						countBookingCost;    // Cost of their bookings in the last five years
	private Money						averageBookingCost;
	private Money						minimumBookingCost;
	private Money						maximumBookingCost;
	private Money						standardDeviationBookingCost;
	private Integer						countNumberPassengers;   // Number of passengers in their bookings
	private Double						averageNumberPassengers;
	private Integer						minimumNumberPassengers;
	private Integer						maximumNumberPassengers;
	private Double						standardDeviationNumberPassengers;

}
