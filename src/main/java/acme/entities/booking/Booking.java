
package acme.entities.booking;

import java.beans.Transient;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLastNibble;
import acme.constraints.ValidLocatorCode;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.features.airlineManager.flight.AirlineManagerFlightRepository;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidLocatorCode
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Optional
	@ValidLastNibble
	@Automapped
	private String				lastNibble;

	// Derived attributes ---------------------------------------------------


	@Transient
	public Money getPrice() {
		Money price;
		AirlineManagerFlightRepository flightRepository = SpringHelper.getBean(AirlineManagerFlightRepository.class);
		BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);
		if (this.getFlight() == null) {
			Money noPrice = new Money();
			noPrice.setAmount(0.0);
			noPrice.setCurrency("EUR");
			return noPrice;
		} else {
			price = flightRepository.findCostByFlight(this.flight.getId());
			Collection<Passenger> pass = bookingRepository.findPassengerByBooking(this.getId());
			Double amount = price.getAmount() * pass.size();
			price.setAmount(amount);
			return price;
		}
	}

	//Relationships -----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer	customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

}
