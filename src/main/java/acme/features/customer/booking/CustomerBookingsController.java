
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiController
public class CustomerBookingsController extends AbstractGuiController<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingsListService		listService;

	@Autowired
	private CustomerBookingsShowService		showService;

	@Autowired
	private CustomerBookingsCreateService	createService;

	@Autowired
	private CustomerBookingsUpdateService	updateService;

	@Autowired
	private CustomerBookingsPublishService	publishService;

	@Autowired
	private CustomerBookingsDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
