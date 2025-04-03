<%--
- list.jsp
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

<acme:list>
	<acme:list-column code="airline-manager.leg.list.label.flightNumber" path="flightNumber" width="10%" sortable="false"/>
	<acme:list-column code="airline-manager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="20%" sortable="true"/>
	<acme:list-column code="airline-manager.leg.list.label.scheduledArrival" path="scheduledArrival" width="20%" sortable="false"/>
	<acme:list-column code="airline-manager.leg.list.label.departureAirport" path="departureAirport" width="20%" sortable="false"/>
	<acme:list-column code="airline-manager.leg.list.label.arrivalAirport" path="arrivalAirport" width="20%%" sortable="false"/>
	<acme:list-column code="airline-manager.leg.list.label.status" path="status" width="20%" sortable="false"/>
	<acme:list-column code="airline-manager.leg.list.label.draftMode" path="draftMode" width="10%" sortable="false"/>
</acme:list>
<jstl:if test="${masterDraftMode==true}">
	<acme:button code="airline-manager.leg.list.button.create" action="/airline-manager/leg/create?masterId=${masterId}"/>
</jstl:if>