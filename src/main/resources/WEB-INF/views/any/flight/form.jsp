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
	<acme:input-textbox code="any.flight.form.label.tag" path="tag"/>
	<acme:input-checkbox code="any.flight.form.label.selfTransfer" path="selfTransfer"/>
	<acme:input-money code="any.flight.form.label.cost" path="cost"/>
	<acme:input-textbox code="any.flight.form.label.description" path="description"/>
	
	<acme:input-moment code="any.flight.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
	<acme:input-moment code="any.flight.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
	<acme:input-textbox code="any.flight.form.label.origin" path="origin" readonly="true"/>
	<acme:input-textbox code="any.flight.form.label.destination" path="destination" readonly="true"/>
	<acme:input-textbox code="any.flight.form.label.layovers" path="layovers" readonly="true"/>
	<acme:button code="any.leg.form.button.list" action="/any/leg/list?masterId=${id}"/>
</acme:form>
