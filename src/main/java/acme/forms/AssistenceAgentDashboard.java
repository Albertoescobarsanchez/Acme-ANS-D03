
package acme.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistenceAgentDashboard {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer				ratioOfResolvedClaims;
	private Integer				ratioOfRejectedClaims;
	private String				top3MonthsWithTheMostClaims;
	private Double				averageOfLogs;
	private Double				minOfLogs;
	private Double				maxOfLogs;
	private Double				standardDesviationOfLogs;
	private Double				averageOfAssistedClaims;
	private Double				minOfAssistedClaims;
	private Double				maxOfAssistedClaims;
	private Double				standardDesviationOfAssistedClaims;
}
