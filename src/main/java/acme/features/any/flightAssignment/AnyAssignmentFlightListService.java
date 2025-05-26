
package acme.features.any.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class AnyAssignmentFlightListService extends AbstractGuiService<Any, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyFlightAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		super.getBuffer().addData(this.repository.findFlightAssignmentPublished());
	}

	@Override
	public void unbind(final FlightAssignment object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "duty", "status", "draftMode", "leg", "lastUpdate", "remarks");
		dataset.put("leg", object.getLeg().getFlightNumber());
		if (object.isDraftMode())
			dataset.put("draftMode", "✓");
		else
			dataset.put("draftMode", "✗");
		super.getResponse().addData(dataset);
	}

}
