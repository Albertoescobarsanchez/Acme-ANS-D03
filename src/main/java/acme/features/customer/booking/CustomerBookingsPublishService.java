
package acme.features.customer.booking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.entities.booking.Booking;
import acme.entities.leg.Leg;
import acme.realms.Customer;

public class CustomerBookingsPublishService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class) && booking != null && booking && super.getRequest().getPrincipal().getActiveRealm().getId() == booking.getCustomer().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int bookingId;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking object) {
		assert object != null;

		super.bindObject(object, "locatorMode", "lastNibble");
	}

	@Override
	public void validate(final Booking object) {
		assert object != null;
		List<Leg> legs = new ArrayList<>(this.repository.findLegsByFlightId(object.getId()));
		super.state(!legs.isEmpty(), "*", "airline-manager.flight.form.error.publishingWithoutAnyLeg");

		boolean allpublished = legs.stream().allMatch(l -> !l.getDraftMode());
		super.state(allpublished, "*", "airline-manager.flight.form.error.notPublishedLegs");
	}

	@Override
	public void perform(final Booking object) {
		assert object != null;
		object.setLastNibble(null);
		;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Booking object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
