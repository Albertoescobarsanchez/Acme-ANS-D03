
package acme.features.airlineManager.dashboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;
import acme.forms.AirlineManagerDashboard;
import acme.realms.AirlineManager;

public class AirlineManagerDashboardShowService extends AbstractGuiService<AirlineManager, AirlineManagerDashboard> {

	@Autowired
	private AirlineManagerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AirlineManagerDashboard dashboard;
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		AirlineManager manager = this.repository.findAirlineManagerById(managerId);
		List<AirlineManager> managers = new ArrayList<>(this.repository.findAirlineManagers());
		List<AirlineManager> managersRanking = managers.stream().sorted(Comparator.comparing(m -> -m.getYearsOfExperience())).collect(Collectors.toList());
		int ranking = managersRanking.indexOf(manager) + 1;
		int yearsToRetire = MomentHelper.getBaseMoment().getYear() - manager.getBirthDate().getYear();
		double onTimeFlights = 0;
		double delayedFlights = 0;
		for (Flight flight : this.repository.findFlightsByAirlineManagerId(managerId))
			for (Leg leg : this.repository.findLegsByFlightId(flight.getId()))
				if (leg.getStatus() == Status.ON_TIME) {
					onTimeFlights++;
					break;
				} else if (leg.getStatus() == Status.DELAYED) {
					delayedFlights++;
					break;
				}
		dashboard = new AirlineManagerDashboard();
		dashboard.setRanking(ranking);
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setRatioOfOntimeAndDelayedFlights(onTimeFlights / delayedFlights);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AirlineManagerDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "ranking", "yearsToRetire", "ratioOfOntimeAndDelayedFlights", "mostPopularAirport", "lessPopularAirport", "numberofLegsByStatus", "averageFlightCost", "deviationFlightCost", "maximumFlightCost",
			"minimumFlightCost");

		super.getResponse().addData(dataset);
	}
}
