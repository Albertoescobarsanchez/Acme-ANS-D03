
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.features.airlineManager.flight.AirlineManagerFlightRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingsShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingsRepository		repository;

	@Autowired
	private AirlineManagerFlightRepository	flightRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		boolean status = booking.getCustomer().getUserAccount().getId() == customerId && super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int bookingId = super.getRequest().getData("id", int.class);

		booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;

		Collection<Flight> flights = this.flightRepository.findAllFlights();
		flightChoices = SelectChoices.from(flights, "Destination", booking.getFlight());
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Passenger> passengerNumber = this.repository.findPassengersByBookingId(booking.getId());
		Collection<String> passengers = passengerNumber.stream().map(x -> x.getFullName()).toList();

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "lastNibble", "draftMode");
		dataset.put("travelClass", choices);
		dataset.put("passengers", passengers);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

	//	@Override
	//	public void unbind(final Booking booking) {
	//		Dataset dataset;
	//		SelectChoices choices;
	//		SelectChoices flightChoices;
	//		//		Date today = MomentHelper.getCurrentMoment();
	//
	//		Collection<Flight> flights = this.flightRepository.findAllFlights().stream().filter(f -> !f.getDraftMode()).toList();
	//
	//		// Si booking tiene un vuelo, lo usamos; de lo contrario, usamos el primer vuelo disponible (si existe)
	//		Flight selectedFlight = booking.getFlight() != null ? booking.getFlight() : !flights.isEmpty() ? flights.iterator().next() : null;
	//
	//		flightChoices = SelectChoices.from(flights, "tag", selectedFlight);
	//		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
	//
	//		Collection<Passenger> passengerN = this.repository.findPassengersByBookingId(booking.getId());
	//		Collection<String> passengers = passengerN.stream().map(p -> p.getFullName()).toList();
	//
	//		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "draftMode", "lastNibble");
	//		dataset.put("travelClass", choices);
	//		dataset.put("passengers", passengers);
	//
	//		dataset.put("flight", flightChoices.getSelected() != null ? flightChoices.getSelected().getKey() : selectedFlight != null ? selectedFlight.getId() : null);
	//		dataset.put("flights", flightChoices);
	//
	//		super.getResponse().addData(dataset);
	//	}

}
