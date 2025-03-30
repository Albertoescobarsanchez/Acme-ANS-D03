
package acme.features.customerPassenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService  interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Passenger passenger;
		int customerId;

		id = super.getRequest().getData("id", int.class);
		customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		passenger = this.repository.findPassengerById(id);
		status = passenger.getCustomer().getUserAccount().getId() == customerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		assert passenger != null;
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "draftMode", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		assert passenger != null;
		if (passenger.isDraftMode() == false)
			super.state(false, "draftMode", "acme.validation.confirmation.message.update");
	}

	@Override
	public void perform(final Passenger passenger) {
		assert passenger != null;
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "draftMode", "specialNeeds");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
