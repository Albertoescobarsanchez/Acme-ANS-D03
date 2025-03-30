
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;
// 5a) porque no pertenece a la zona hija

@GuiService
public class CustomerBookingsListService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Booking> objects;
		objects = this.repository.findBookingByCustomerId(super.getRequest().getPrincipal().getActiveRealm().getId());

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Booking object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "locatorCode", "lastNibble");

		// Comprobar que el último nibble de la tarjeta no es nulo ni vacío
		if (object.getLastNibble() == null || object.getLastNibble().isEmpty())
			throw new IllegalArgumentException("The booking cannot be published because the last credit card nibble has not been stored.");
		dataset = super.unbindObject(object, "locatorCode", "lastNibble");
		super.getResponse().addData(dataset);
	}

}
