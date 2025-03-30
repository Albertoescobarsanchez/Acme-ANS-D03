<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
			<acme:input-textbox code="customer.bookingRecord.list.label.booking" path="booking" choices="${bookings}"/>
			<acme:input-textbox code="customer.bookingRecord.list.label.passenger" path="passenger" choices="${passengers}"/>
	
	<jstl:choose>	
				<jstl:when test="${_command == 'create'}" >
						<acme:submit code="customer.bookingRecord.form.button.create" action="/customer/bookingRecord/create"/>
				</jstl:when>
	</jstl:choose>
</acme:form>
