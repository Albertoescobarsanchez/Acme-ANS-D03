
package acme.features.flightCrewMember.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.StatusAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentFlightCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int crewMemberId;
		FlightCrewMember flightCrewMember;

		crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightCrewMember = this.repository.findFlightCrewMemberById(crewMemberId);

		flightAssignment = new FlightAssignment();
		flightAssignment.setDuty(Duty.LEAD_ATTENDANT);
		flightAssignment.setLastUpdate(MomentHelper.getBaseMoment());
		flightAssignment.setStatus(StatusAssignment.CONFIRMED);
		flightAssignment.setRemarks("This is remarks");
		flightAssignment.setPublish(false);
		//flightAssignment.setLeg("Empty");
		//flightAssignment.setMember(0);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment object) {
		assert object != null;

		super.bindObject(object, "duty", "lastUpdate", "status", "remarks", "publish", "leg", "member");
	}

	@Override
	public void validate(final FlightAssignment object) {
		assert object != null;
	}

	@Override
	public void perform(final FlightAssignment object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final FlightAssignment object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "duty", "lastUpdate", "status", "remarks", "publish", "leg", "member");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
