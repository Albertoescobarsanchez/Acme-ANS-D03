<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flight-crew-member.dashboard.form.label.lastFiveDestinations" path="lastFiveDestinations" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.dashboard.form.label.legsWithIncidentSeverity3" path="legsWithIncidentSeverity3" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.dashboard.form.label.legsWithIncidentSeverity7" path="legsWithIncidentSeverity7" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.dashboard.form.label.legsWithIncidentSeverity10" path="legsWithIncidentSeverity10" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.dashboard.form.label.lastLegCrewMembers" path="lastLegCrewMembers" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.dashboard.form.label.flightAssignmentsGroupedByStatus" path="flightAssignmentsGroupedByStatus" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.dashboard.form.label.flightAssignmentsStatsLastMonth" path="flightAssignmentsStatsLastMonth" readonly="true"/>

</acme:form>
