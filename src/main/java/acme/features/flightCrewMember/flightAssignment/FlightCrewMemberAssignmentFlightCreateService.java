
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.StatusAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentFlightCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean isCorrect = false;

		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		if (super.getRequest().getMethod().equals("GET"))
			isCorrect = true;

		if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class).equals(0)) {

			FlightCrewMember member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
			Collection<Leg> legs = this.repository.findAllLegsFromAirline(member.getAirline().getId());

			Leg legSelected = super.getRequest().getData("leg", Leg.class);
			if (legSelected == null || legs.contains(legSelected))
				isCorrect = true;
		}

		super.getResponse().setAuthorised(status && isCorrect);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		flightAssignment = new FlightAssignment();
		flightAssignment.setDuty(Duty.LEAD_ATTENDANT);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		flightAssignment.setStatus(StatusAssignment.CONFIRMED);
		flightAssignment.setRemarks("This is remarks");
		flightAssignment.setDraftMode(true);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember member = this.repository.findFlightCrewMemberById(memberId);
		flightAssignment.setMember(member);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment object) {
		assert object != null;

		super.bindObject(object, "duty", "status", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment object) {
		assert object != null;

		//Solo miembros de estado "AVAILABLE" pueden ser asignados
		//No se puede asignar a multiples legs simultaneos
		/*
		 * if (object.getMember() != null) {
		 * boolean available = object.getMember().getStatus().equals(StatusCrewMember.AVAILABLE);
		 * super.state(available, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.available");
		 * boolean assigned = this.repository.hasLegAssociated(object.getMember().getId(), MomentHelper.getCurrentMoment());
		 * super.state(!assigned, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.multipleLegs");
		 * }
		 */

	}

	@Override
	public void perform(final FlightAssignment object) {
		assert object != null;
		object.setId(0);
		this.repository.save(object);

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
		legChoices = SelectChoices.from(legs, "flightNumber", null);

		dataset.put("dutyChoices", dutyChoices);
		dataset.put("statusChoices", statusChoices);
		dataset.put("legChoices", legChoices);

		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
