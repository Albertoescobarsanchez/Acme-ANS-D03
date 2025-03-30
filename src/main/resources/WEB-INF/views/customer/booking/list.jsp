<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="booking.list.label.locatorCode" path="locatorCode" width="10%"/>
	<acme:list-column code="booking.list.label.purchaseMoment" path="purchaseMoment" width="20%"/>
	<acme:list-column code="booking.list.label.travelClass" path="travelClass" width="20%"/>
</acme:list>

<acme:button code="booking.form.button.create" action="/customer/booking/create"/>
<acme:button code="booking-record.form.button.create" action="/customer/booking-record/create"/>