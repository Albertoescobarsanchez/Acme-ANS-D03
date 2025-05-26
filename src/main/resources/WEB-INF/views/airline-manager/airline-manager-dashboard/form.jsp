<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airline-manager.dashboard.form.label.ranking" path="ranking"/>
	<acme:input-textbox code="airline-manager.dashboard.form.label.yearsToRetire" path="yearsToRetire"/>
	<acme:input-double code="airline-manager.dashboard.form.label.ratioOfOntimeAndDelayedFlights" path="ratioOfOntimeAndDelayedFlights"/>
	<acme:input-textbox code="airline-manager.dashboard.form.label.mostPopularAirport" path="mostPopularAirport"/>
	<acme:input-textbox code="airline-manager.dashboard.form.label.lessPopularAirport" path="lessPopularAirport"/>
	<acme:input-textbox code="airline-manager.dashboard.form.label.numberofLegsByStatus" path="numberofLegsByStatus"/>
	<acme:input-textbox code="airline-manager.dashboard.form.label.averageFlightCost" path="averageFlightCost"/>
	<acme:input-textbox code="airline-manager.dashboard.form.label.deviationFlightCost" path="deviationFlightCost"/>
	<acme:input-textbox code="airline-manager.dashboard.form.label.maximumFlightCost" path="maximumFlightCost"/>
	<acme:input-textbox code="airline-manager.dashboard.form.label.minimumFlightCost" path="minimumFlightCost"/>
</acme:form>
