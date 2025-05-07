/*
 * AnyLegListService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.airlineManager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegListService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		int flightId = super.getRequest().getData("masterId", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		status = flight != null && flight.getAirlineManager().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Leg> objects;
		int flightId = super.getRequest().getData("masterId", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		super.getResponse().addGlobal("masterId", flightId);
		objects = this.repository.findLegsByFlightId(flightId);
		super.getResponse().addGlobal("masterDraftMode", flight.getDraftMode());
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Leg object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
		dataset.put("departureAirport", object.getDepartureAirport().getIataCode());
		dataset.put("arrivalAirport", object.getArrivalAirport().getIataCode());
		if (object.getDraftMode())
			dataset.put("draftMode", "✓");
		else
			dataset.put("draftMode", "✗");

		super.getResponse().addData(dataset);
	}

}
