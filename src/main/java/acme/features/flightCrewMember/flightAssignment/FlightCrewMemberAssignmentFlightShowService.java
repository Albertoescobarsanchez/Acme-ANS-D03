
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.StatusAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentFlightShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class) && flightAssignment != null && super.getRequest().getPrincipal().getActiveRealm().getId() == flightAssignment.getMember().getId();
		super.getResponse().setAuthorised(status);
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
	public void unbind(final FlightAssignment object) {
		Dataset dataset;
		dataset = super.unbindObject(object, "duty", "lastUpdate", "status", "remarks", "draftMode", "leg");

		//Choices
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		SelectChoices legChoices;

		dutyChoices = SelectChoices.from(Duty.class, object.getDuty());
		statusChoices = SelectChoices.from(StatusAssignment.class, object.getStatus());
		FlightCrewMember crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		Collection<Leg> legs = this.repository.findAllLegsFromAirline(crewMember.getAirline().getId());
		legChoices = SelectChoices.from(legs, "flightNumber", object.getLeg());

		dataset.put("dutyChoices", dutyChoices);
		dataset.put("statusChoices", statusChoices);
		dataset.put("legChoices", legChoices);

		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);

	}

}
