
package acme.features.airlineManager.airlineManagerDashboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;
import acme.forms.AirlineManagerDashboard;
import acme.realms.AirlineManager;

@GuiService
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
		int yearsToRetire = 65 - (MomentHelper.getCurrentMoment().getYear() - manager.getBirthDate().getYear());
		//ratio
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
		//popular airport
		List<Flight> flights = new ArrayList<>(this.repository.findFlightsByAirlineManagerId(managerId));
		Map<Airport, Integer> popularAirport = new HashMap<>();
		for (Airport airport : this.repository.findAllAirports())
			popularAirport.put(airport, 0);
		for (Flight flight : flights) {
			List<Leg> legs = new ArrayList<>(this.repository.findLegsByFlightId(flight.getId())).stream().sorted(Comparator.comparing(leg -> leg.getScheduledDeparture())).toList();
			if (legs.isEmpty())
				continue;
			popularAirport.put(legs.get(0).getDepartureAirport(), popularAirport.get(legs.get(0).getDepartureAirport()) + 1);
			popularAirport.put(legs.get(legs.size() - 1).getArrivalAirport(), popularAirport.get(legs.get(legs.size() - 1).getArrivalAirport()) + 1);
		}
		Airport mostPopularAirport = popularAirport.keySet().stream().sorted(Comparator.comparingInt(airport -> popularAirport.get(airport))).findFirst().get();
		Airport lessPopularAirport = popularAirport.keySet().stream().sorted(Comparator.comparingInt(airport -> popularAirport.get(airport)).reversed()).findFirst().get();
		//numberOfLegsByStatus
		Map<Status, Integer> numberOfLegsByStatus = new HashMap<>();
		for (Status status : Status.values())
			numberOfLegsByStatus.put(status, 0);
		for (Flight flight : flights)
			for (Leg leg : this.repository.findLegsByFlightId(flight.getId()))
				numberOfLegsByStatus.put(leg.getStatus(), numberOfLegsByStatus.get(leg.getStatus()) + 1);

		//money avg, dev, max min
		List<Money> averageFlightCost = new ArrayList<>();
		List<Money> deviationFlightCost = new ArrayList<>();
		List<Money> maximumFlightCost = new ArrayList<>();
		List<Money> minimumFlightCost = new ArrayList<>();
		Set<String> currencies = new HashSet<>();
		for (Flight flight : flights)
			currencies.add(flight.getCost().getCurrency());
		for (String currency : currencies) {
			Money avg = new Money();
			avg.setCurrency(currency);
			avg.setAmount(this.repository.avgFlightCostByCurrency(currency));
			averageFlightCost.add(avg);
			Money dev = new Money();
			dev.setCurrency(currency);
			dev.setAmount(this.repository.devFlightCostByCurrency(currency));
			deviationFlightCost.add(dev);
			Money max = new Money();
			max.setCurrency(currency);
			max.setAmount(this.repository.maxFlightCostByCurrency(currency));
			maximumFlightCost.add(max);
			Money min = new Money();
			min.setCurrency(currency);
			min.setAmount(this.repository.minFlightCostByCurrency(currency));
			minimumFlightCost.add(min);
		}
		dashboard = new AirlineManagerDashboard();
		dashboard.setRanking(ranking);
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setRatioOfOntimeAndDelayedFlights(onTimeFlights / delayedFlights);
		dashboard.setMostPopularAirport(mostPopularAirport);
		dashboard.setLessPopularAirport(lessPopularAirport);
		dashboard.setNumberofLegsByStatus(numberOfLegsByStatus);
		dashboard.setAverageFlightCost(averageFlightCost);
		dashboard.setDeviationFlightCost(deviationFlightCost);
		dashboard.setMaximumFlightCost(maximumFlightCost);
		dashboard.setMinimumFlightCost(minimumFlightCost);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AirlineManagerDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "ranking", "yearsToRetire", "ratioOfOntimeAndDelayedFlights");

		dataset.put("mostPopularAirport", object.getMostPopularAirport().getIataCode());
		dataset.put("lessPopularAirport", object.getLessPopularAirport().getIataCode());
		String numberofLegsByStatus = "";
		for (Status status : object.getNumberofLegsByStatus().keySet())
			numberofLegsByStatus += status.toString() + ": " + object.getNumberofLegsByStatus().get(status) + ", ";
		numberofLegsByStatus = numberofLegsByStatus.substring(0, numberofLegsByStatus.length() - 2);
		dataset.put("numberofLegsByStatus", numberofLegsByStatus);
		dataset.put("averageFlightCost", this.getMoneyText(object.getAverageFlightCost()));
		dataset.put("deviationFlightCost", this.getMoneyText(object.getDeviationFlightCost()));
		dataset.put("maximumFlightCost", this.getMoneyText(object.getMaximumFlightCost()));
		dataset.put("minimumFlightCost", this.getMoneyText(object.getMinimumFlightCost()));
		super.getResponse().addData(dataset);
	}
	private String getMoneyText(final List<Money> moneyList) {
		String moneyText = "";
		for (Money money : moneyList)
			moneyText += money.getCurrency() + " " + money.getAmount() + ", ";
		if (moneyText.endsWith(", "))
			moneyText = moneyText.substring(0, moneyText.length() - 2);
		return moneyText;
	}
}
