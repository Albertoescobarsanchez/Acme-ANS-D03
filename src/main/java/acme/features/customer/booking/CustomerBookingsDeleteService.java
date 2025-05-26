
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.features.airlineManager.flight.AirlineManagerFlightRepository;
import acme.features.customer.bookingRecord.CustomerBookingRecordRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingsDeleteService extends AbstractGuiService<Customer, Booking> {
	// Internal state --------------------------------------------------------

	@Autowired
	private CustomerBookingsRepository		repository;

	@Autowired
	private CustomerBookingRecordRepository	bookingRecordrepository;

	@Autowired
	private AirlineManagerFlightRepository	flightRepository;

	// AbstractGuiService interfaced -----------------------------------------


	@Override
	public void authorise() {
		int id;
		Booking booking;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		boolean status = booking.getCustomer().getUserAccount().getId() == customerId;

		super.getResponse().setAuthorised(status && booking.isDraftMode() && isCustomer);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "purchaseMoment", "price", "lastNibble", "travelClass", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		;

	}

	@Override
	public void perform(final Booking booking) {
		for (BookingRecord bk : this.bookingRecordrepository.findBookingRecordByBookingId(booking.getId()))
			this.bookingRecordrepository.delete(bk);
		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;

		Date today = MomentHelper.getCurrentMoment();
		Collection<Flight> flights = this.flightRepository.findAllFlights().stream().filter(f -> f.getDraftMode() == false).toList();
		flightChoices = SelectChoices.from(flights, "tag", booking.getFlight());
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Passenger> passengerN = this.repository.findPassengersByBookingId(booking.getId());
		Collection<String> passengers = passengerN.stream().map(p -> p.getFullName()).toList();

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "draftMode", "lastNibble");
		dataset.put("travelClass", choices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("passengers", passengers);
		System.out.println(flightChoices);

		super.getResponse().addData(dataset);
	}
	//	@Override
	//	public void unbind(final Booking booking) {
	//		Dataset dataset;
	//		SelectChoices choices;
	//		SelectChoices flightChoices;
	//
	//		Date today = MomentHelper.getCurrentMoment();
	//		Collection<Flight> flights = this.repository.findAllPublishedFlightsWithFutureDeparture(today);
	//		flightChoices = SelectChoices.from(flights, "Destination", booking.getFlight());
	//		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
	//		Collection<String> passengers = this.repository.findPassengersNameByBooking(booking.getId());
	//
	//		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "price", "lastNibble", "draftMode");
	//		dataset.put("travelClass", choices);
	//		dataset.put("passengers", passengers);
	//		dataset.put("flight", flightChoices.getSelected().getKey());
	//		dataset.put("flights", flightChoices);
	//
	//		super.getResponse().addData(dataset);
	//	}
}
