<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-select code="booking.list.label.flight" path="flight" choices="${flights}"/>
		<acme:input-textbox code="booking.list.label.locatorCode" path="locatorCode"/>
		<acme:input-moment code="booking.list.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
		<acme:input-select code="booking.list.label.travelClass" path="travelClass" choices="${travelClass}"/>
		<acme:input-money code="booking.list.label.price" path="price" readonly="true"/>
		<acme:input-textbox code="booking.list.label.lastNibble" path="lastNibble"/>
		<acme:input-checkbox code="booking.list.label.draftMode" path="draftMode" readonly="true"/>
		<acme:input-textarea code="booking.list.label.passenger" path="passengers" readonly="true"/>
	<jstl:choose>
			<jstl:when test="${(_command == 'update' || _command == 'show' || _command == 'publish') && draftMode == true}">
				<acme:submit code="booking.form.button.update" action="/customer/booking/update"/>
				<acme:submit code="booking.form.button.publish" action="/customer/booking/publish"/>
				<acme:submit code="booking.form.button.delete" action="/customer/booking/delete"/>
				
			</jstl:when>
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="booking.form.button.create" action="/customer/booking/create"/>
			</jstl:when>
					
	</jstl:choose>	
	
	<jstl:if test="${passengers.size() != 0 && (_command == 'update' || _command == 'show' || _command == 'publish')}">
			<acme:button code="booking.form.button.passenger" action="/customer/passenger/list?bookingId=${bookingId}"/>
	</jstl:if>
		
</acme:form>