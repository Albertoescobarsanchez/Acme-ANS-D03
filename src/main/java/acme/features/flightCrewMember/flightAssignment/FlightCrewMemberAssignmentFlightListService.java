
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentFlightListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> objects;
		objects = this.repository.findFlightsAssignmentByFlightCrewMemberId(super.getRequest().getPrincipal().getActiveRealm().getId());
		super.getBuffer().addData(objects);
	}
	/*
	 * @Override
	 * public void loadCompleted() {
	 * int id;
	 * Status status;
	 * FlightAssignment fligthAssignment;
	 * id = super.getRequest().getPrincipal().getActiveRealm().getId();
	 * fligthAssignment = this.repository.findFlightAssignmentById(id);
	 * status = fligthAssignment.getLeg().getStatus();
	 * if (status == Status.DELAYED)
	 * super.getBuffer().addData(fligthAssignment);
	 * }
	 */
	@Override
	public void unbind(final FlightAssignment object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "duty", "status", "publish", "leg");
		dataset.put("leg", object.getLeg().getFlightNumber());
		if (object.isPublish())
			dataset.put("publish", "✓");
		else
			dataset.put("publish", "✗");
		super.getResponse().addData(dataset);
	}

}
