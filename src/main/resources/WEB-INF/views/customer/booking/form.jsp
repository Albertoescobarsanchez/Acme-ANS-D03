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
 		<acme:input-select code="customer.booking.list.label.flight" path="flight" choices="${flights}"/>
		<acme:input-textbox code="customer.booking.list.label.locatorCode" path="locatorCode"/>
		<acme:input-select code="customer.booking.list.label.travelClass" path="travelClass" choices="${travelClass}"/>
		<acme:input-moment code="customer.booking.list.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
		<acme:input-money code="customer.booking.list.label.price" path="price" readonly="true"/>
		<acme:input-textbox code="customer.booking.list.label.lastNibble" path="lastNibble"/>
  		<acme:input-textarea code="customer.booking.list.label.passenger" path="passengers" readonly="true"/>
	<jstl:choose> 

			<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true }">
				<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
 				<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>	
 				<acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete"/>							
			</jstl:when>	
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
			</jstl:when>
	</jstl:choose>	
			<jstl:if test="${passengers.size() !=0 && (_command == 'update' || _command == 'show' || _command == 'publish')}">
	 			<acme:button code="customer.booking.form.button.passenger" action="/customer/passenger/listBooking?bookingId=${id}"/>
			</jstl:if>

</acme:form> 