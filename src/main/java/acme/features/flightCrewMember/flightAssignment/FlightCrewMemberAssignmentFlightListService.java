
package acme.features.flightCrewMember.flightAssignment;

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
		int id;
		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean completed = super.getRequest().getData("completed", boolean.class);
		if (!completed)
			//--------------------------------SALEN AL REVES---------------------------------
			super.getBuffer().addData(this.repository.findFlightAssignmentCompletedByMemberId(id));
		else
			super.getBuffer().addData(this.repository.findFlightAssignmentNotCompletedByMemberId(id));
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
