/*
 * AirlineManagerLegUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.airlineManager.flight;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		status = flight != null && flight.getDraftMode() && super.getRequest().getPrincipal().getActiveRealm().getId() == flight.getAirlineManager().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int flightId;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight object) {
		assert object != null;

		super.bindObject(object, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight object) {
		assert object != null;
		List<Leg> legs = new ArrayList<>(this.repository.findLegsByFlightId(object.getId()));
		super.state(!legs.isEmpty(), "*", "airline-manager.flight.form.error.publishingWithoutAnyLeg");

		boolean allpublished = legs.stream().allMatch(l -> !l.getDraftMode());
		super.state(allpublished, "*", "airline-manager.flight.form.error.notPublishedLegs");
	}

	@Override
	public void perform(final Flight object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Flight object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "origin", "destination", "layovers", "draftMode");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
