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

package acme.features.airlineManager.leg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegUpdateService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		status = leg != null && leg.getDraftMode() && leg.getFlight().getAirlineManager().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

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
	public void bind(final Leg object) {
		assert object != null;

		super.bindObject(object, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");
	}

	@Override
	public void validate(final Leg object) {
		assert object != null;
		super.state(object.getFlightNumber().startsWith(object.getFlight().getAirlineManager().getAirline().getIataCode()), "flightNumber", "airline-manager.leg.form.error.flightNumberNotStartingWithAirlineIATACode");

		boolean duplicatedNumber = this.repository.findLegsByAirlineId(object.getFlight().getAirlineManager().getAirline().getId()).stream().anyMatch(leg -> leg.getFlightNumber().equals(object.getFlightNumber()) && leg.getId() != object.getId());
		super.state(!duplicatedNumber, "flightNumber", "airline-manager.leg.form.error.duplicatedFlightNumber");
		if (!super.getBuffer().getErrors().hasErrors("scheduledDeparture") && !super.getBuffer().getErrors().hasErrors("scheduledArrival")) {

			super.state(object.getScheduledDeparture().before(object.getScheduledArrival()), "scheduledArrival", "airline-manager.leg.form.error.arrivalBeforeDeparture");

			List<Leg> legs = new ArrayList<>(this.repository.findLegsByFlightId(object.getFlight().getId()));

			Leg notModifiedLeg = this.repository.findLegById(object.getId());
			List<Leg> pastLegs = legs.stream().filter(leg -> leg.getId() != object.getId() && leg.getScheduledArrival().before(notModifiedLeg.getScheduledDeparture())).collect(Collectors.toList());
			List<Leg> futureLegs = legs.stream().filter(leg -> leg.getId() != object.getId() && notModifiedLeg.getScheduledArrival().before(leg.getScheduledDeparture())).collect(Collectors.toList());

			super.state(pastLegs.stream().allMatch(leg -> leg.getScheduledArrival().before(object.getScheduledDeparture())), "scheduledDeparture", "airline-manager.leg.form.error.departurebeforeSomePastLegArrival");

			super.state(futureLegs.stream().allMatch(leg -> object.getScheduledArrival().before(leg.getScheduledDeparture())), "scheduledArrival", "airline-manager.leg.form.error.arrivalAfterSomeFutureLegDeparture");
		}
		if (!super.getBuffer().getErrors().hasErrors("departureAirport") && !super.getBuffer().getErrors().hasErrors("arrivalAirport"))
			super.state(!object.getDepartureAirport().getIataCode().equals(object.getArrivalAirport().getIataCode()), "arrivalAirport", "airline-manager.leg.form.error.arrivalAirportCannotBeEqualThanDepartureAirport");

	}

	@Override
	public void perform(final Leg object) {
		assert object != null;
		this.repository.save(object);
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

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
