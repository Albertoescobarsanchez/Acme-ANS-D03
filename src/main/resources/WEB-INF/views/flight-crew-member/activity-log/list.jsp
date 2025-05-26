<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.activity-log.list.label.moment" path="moment" width="10%"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.type" path="logType" width="20%" sortable="true"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.description" path="description" width="20%" sortable="true"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.severityLevel" path="severityLevel" width="20%" sortable="true"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.draftMode" path="draftMode" width="20%%" sortable="true"/>
</acme:list>
<jstl:if test="${masterDraftMode==false}">
	<acme:button code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create?masterId=${masterId}"/>
</jstl:if>