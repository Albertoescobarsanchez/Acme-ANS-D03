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
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.duty" path="duty" readonly="true"/>
	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.lastUpdate" path="lastUpdate" readonly="true" />
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.status" path="status" readonly = "true"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks" readonly = "true"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.leg" path="leg" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.member" path="flightCrewMember" readonly="true"/>
</acme:form>
