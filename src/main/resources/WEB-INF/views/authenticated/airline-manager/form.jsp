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
	<acme:input-textbox code="authenticated.airline-manager.form.label.identifierNumber" path="identifierNumber"/>
	<acme:input-integer code="authenticated.airline-manager.form.label.yearsOfExperience" path="yearsOfExperience"/>
	<acme:input-moment code="authenticated.airline-manager.form.label.birthDate" path="birthDate"/>
	<acme:input-textbox code="authenticated.airline-manager.form.label.pictureLink" path="pictureLink"/>
	
	<jstl:if test="${_command == 'create'}" >
	<acme:input-select code="authenticated.airline-manager.form.label.airline" path="airline" choices="${airlineChoices}"/>
		<acme:submit code="authenticated.airline-manager.form.button.create" action="/authenticated/airline-manager/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}" >
		<acme:input-select code="authenticated.airline-manager.form.label.airline" path="airline" choices="${airlineChoices}" readonly="true"/>
		<acme:submit code="authenticated.airline-manager.form.button.update" action="/authenticated/airline-manager/update"/>
	</jstl:if>
	
</acme:form>
