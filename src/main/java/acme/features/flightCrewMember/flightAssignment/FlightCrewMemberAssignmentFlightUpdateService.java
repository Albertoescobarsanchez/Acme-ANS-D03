/*
 * AuthenticatedAirlineManagerUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

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
public class FlightCrewMemberAssignmentFlightUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		status = flightAssignment != null && flightAssignment.isDraftMode() && super.getRequest().getPrincipal().getAccountId() == flightAssignment.getMember().getUserAccount().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		flightAssignment = new FlightAssignment();
		int flightAssignmentId;
		flightAssignment.setDraftMode(true);
		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment object) {
		assert object != null;

		super.bindObject(object, "duty", "status", "lastUpdate", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment object) {
		assert object != null;
	}

	@Override
	public void unbind(final FlightAssignment object) {
		Dataset dataset;
		dataset = super.unbindObject(object, "duty", "lastUpdate", "status", "remarks", "draftMode", "leg");

		// Choices
		SelectChoices dutyChoices = SelectChoices.from(Duty.class, object.getDuty());
		SelectChoices statusChoices = SelectChoices.from(StatusAssignment.class, object.getStatus());

		FlightCrewMember crewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		Collection<Leg> legs = this.repository.findAllLegsFromAirline(crewMember.getAirline().getId());

		// Aquí usamos el ID del leg para que se seleccione correctamente
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", object.getLeg());

		dataset.put("dutyChoices", dutyChoices);
		dataset.put("statusChoices", statusChoices);
		dataset.put("legChoices", legChoices);

		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		this.repository.save(assignment);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
