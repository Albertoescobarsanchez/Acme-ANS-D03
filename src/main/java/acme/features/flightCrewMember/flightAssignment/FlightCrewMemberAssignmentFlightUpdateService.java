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
import acme.realms.StatusCrewMember;

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
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment object) {
		assert object != null;

		super.bindObject(object, "duty", "status", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment object) {
		super.state(object.getMember() != null, "flightCrewMember", "acme.validation.flightAssignment.flightcrewmember");
		super.state(object.getLeg() != null, "leg", "acme.validation.flightAssignment.leg");

		//Solo 1 piloto y 1 copiloto por vuelo
		if (object.getDuty() != null && object.getLeg() != null) {
			boolean isDutyAssigned = this.repository.hasDutyAssigned(object.getLeg().getId(), object.getDuty(), object.getId());
			super.state(!isDutyAssigned, "duty", "acme.validation.flightAssignment.duty");
		}

		if (object.getLeg() != null) {
			boolean linkPastLeg = object.getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());
			super.state(!linkPastLeg, "leg", "acme.validation.flightAssignment.leg.moment");
		}

		//Solo miembros de estado "AVAILABLE" pueden ser asignados
		//No se puede asignar a multiples legs simultaneos
		if (object.getMember() != null) {
			boolean available = object.getMember().getStatus().equals(StatusCrewMember.AVAILABLE);
			super.state(available, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.available");
			boolean assigned = this.repository.hasLegAssociated(object.getMember().getId(), MomentHelper.getCurrentMoment());
			super.state(!assigned, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.multipleLegs");
		}
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

		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
