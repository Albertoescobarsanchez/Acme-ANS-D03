
package acme.features.any.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class AnyAssignmentFlightShowService extends AbstractGuiService<Any, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment completedFlightAssignment) {
		Dataset dataset = super.unbindObject(completedFlightAssignment, "duty", "lastUpdate", "status", "remarks", "draftMode", "leg", "member");

		dataset.put("flightCrewMember", completedFlightAssignment.getMember().getIdentity().getFullName());

		// Leg choices
		dataset.put("leg", completedFlightAssignment.getLeg().getFlightNumber());

		dataset.put("status", completedFlightAssignment.getStatus());

		super.getResponse().addData(dataset);
	}

}
