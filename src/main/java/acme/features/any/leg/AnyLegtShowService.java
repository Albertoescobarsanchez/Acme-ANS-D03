/*
 * AnyLegtShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;

@GuiService
public class AnyLegtShowService extends AbstractGuiService<Any, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		status = leg != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
		final SelectChoices departureAirportChoices;
		final SelectChoices arrivalAirportChoices;
		final SelectChoices aircraftChoices;
		final SelectChoices statusChoices;
		statusChoices = SelectChoices.from(Status.class, object.getStatus());
		departureAirportChoices = SelectChoices.from(this.repository.findAirports(), "iataCode", object.getDepartureAirport());
		arrivalAirportChoices = SelectChoices.from(this.repository.findAirports(), "iataCode", object.getArrivalAirport());
		aircraftChoices = SelectChoices.from(this.repository.findActiveAircraftsByAirlineId(object.getFlight().getAirlineManager().getAirline().getId()), "registrationNumber", object.getAircraft());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("arrivalAirports", arrivalAirportChoices);
		dataset.put("aircraftChoices", aircraftChoices);
		dataset.put("statuses", statusChoices);
		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("status", statusChoices.getSelected().getKey());
		super.getResponse().addData(dataset);
	}

}
