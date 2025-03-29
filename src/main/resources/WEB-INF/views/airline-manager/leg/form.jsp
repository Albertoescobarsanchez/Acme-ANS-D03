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
	<acme:input-textbox code="airline-manager.leg.form.label.flightNumber" path="flightNumber"/>
	<acme:input-moment code="airline-manager.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="airline-manager.leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-select code="airline-manager.leg.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-select code="airline-manager.leg.form.label.departureAirport" path="departureAirport" choices="${departureAirports}"/>
	<acme:input-select code="airline-manager.leg.form.label.arrivalAirport" path="arrivalAirport" choices="${arrivalAirports}"/>
	<acme:input-select code="airline-manager.leg.form.label.aircraft" path="aircraft" choices="${aircraftChoices}"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="airline-manager.leg.form.button.create" action="/airline-manager/leg/create?masterId=${masterId}"/>
	</jstl:if>
	
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish')&& draftMode==true}">
		<acme:submit code="airline-manager.leg.form.button.update" action="/airline-manager/leg/update"/>
		<acme:submit code="airline-manager.leg.form.button.delete" action="/airline-manager/leg/delete"/>
		<acme:submit code="airline-manager.leg.form.button.publish" action="/airline-manager/leg/publish"/>
	</jstl:if>
</acme:form>
