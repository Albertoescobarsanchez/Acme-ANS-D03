
package acme.features.customerBookings;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.features.airlineManager.flight.AirlineManagerFlightRepository;
import acme.realms.AirlineManager;
import acme.realms.Customer;

@GuiService
public class CustomerBookingsShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class) && flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().getActiveRealm().getId() == flight.getAirlineManager().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "origin", "destination", "layovers", "draftMode");

		super.getResponse().addData(dataset);
	}
}
