
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import acme.entities.flightAssignment.StatusAssignment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	//	Serialisation identifier -----------------------------------------------

	protected static final long				serialVersionUID	= 1L;

	//	Attributes -------------------------------------------------------------

	//	The last five destinations to which they have been assigned. 
	private List<String>					lastFiveDestinations;

	//	The number of legs that have an activity log record with an incident severity rang-ing from 0 up to 3, 4 up to 7, and 8 up to 10
	private Integer							legsWithIncidentSeverity3;
	private Integer							legsWithIncidentSeverity7;
	private Integer							legsWithIncidentSeverity10;

	//	The crew members who were assigned with him or her in their last leg.  
	private List<String>					lastLegCrewMembers;

	//	Their flight assignments grouped by their statuses.
	private Map<StatusAssignment, Integer>	flightAssignmentsGroupedByStatus;

	//	Minimum, maximum, average and deviation of flight assignments in the last month
	private Statistics						flightAssignmentsStatsLastMonth;
}
