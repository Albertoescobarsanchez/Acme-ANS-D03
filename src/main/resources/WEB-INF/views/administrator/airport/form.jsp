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

<acme:form>
		<acme:input-textbox code="administrator.airport.list.label.name" path="name" />
		<acme:input-textbox code="administrator.airport.list.label.iata-code" path="iataCode"/>
		<acme:input-select code="administrator.airport.list.label.operationalScope" path="operationalScope" choices="${operationalScopes}" />		
		<acme:input-textbox code="administrator.airport.list.label.city" path="city"/>
		<acme:input-textbox code="administrator.airport.list.label.country" path="country"/>
		<acme:input-url code="administrator.airport.list.label.website" path="website"/>		
		<acme:input-email code="administrator.airport.list.label.email" path="email" />
		<acme:input-textbox code="administrator.airport.list.label.phoneNumber" path="phoneNumber"/>
		<acme:input-checkbox code="administrator.airport.form.label.confirmation" path="confirmation"/>
		<jstl:choose>
			<jstl:when test="${acme:anyOf(_command, 'show|update')}">
				<acme:submit code="administrator.airport.form.button.update" action="/administrator/airport/update"/>
			</jstl:when>
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="administrator.airport.form.button.create" action="/administrator/airport/create"/>
			</jstl:when>		
		</jstl:choose>					
</acme:form>