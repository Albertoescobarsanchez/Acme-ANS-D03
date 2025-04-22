<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.employeeCode" path="employeeCode"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.phoneNumber" path="phoneNumber"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.languageSkills" path="languageSkills"/>
	<acme:input-select code="authenticated.flight-crew-member.form.label.status" path="status" choices="${statusChoices}"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.salary" path="salary"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.yearsExperience" path="yearsExperience"/>
	
	<jstl:if test="${_command == 'create'}" >
	<acme:input-select code="authenticated.flight-crew-member.form.label.airline" path="airline" choices="${airlineChoices}"/>
		<acme:submit code="authenticated.flight-crew-member.form.button.create" action="/authenticated/flight-crew-member/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}" >
		<acme:input-select code="authenticated.flight-crew-member.form.label.airline" path="airline" choices="${airlineChoices}" readonly="true"/>
		<acme:submit code="authenticated.flight-crew-member.form.button.update" action="/authenticated/flight-crew-member/update"/>
	</jstl:if>
	
</acme:form>
