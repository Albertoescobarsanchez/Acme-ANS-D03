<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="77844410J: Escobar Sanchez, Alberto" action="https://www.minecraft.net/es-es"/>
			<acme:menu-suboption code="30276353G: Cantalejo Gomez, Olga" action="https://www.zara.com/es/"/>
			<acme:menu-suboption code="77863099A: Gonzalez Lucena, Juan Antonio" action="https://about.meta.com/es/"/>
			<acme:menu-suboption code="49237577M: Suarez Coronel, Celia" action="https://ev.us.es/ultra/course"/>
			<acme:menu-suboption code="44053812A: Paradas Borrego, Alvaro" action="https://mercadoracing.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-airlines" action="/administrator/airline/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-airports" action="/administrator/airport/list"/>
			<acme:menu-separator/>			
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.any">
      		<acme:menu-suboption code="master.menu.any.flights" action="/any/flight/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.airline-manager" access="hasRealm('AirlineManager')">
			<acme:menu-suboption code="master.menu.airline-manager.flight.list" action="/airline-manager/flight/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.flight-crew-member" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list.completed" action="/flight-crew-member/flight-assignment/list?completed=true"/>
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list.not-completed" action="/flight-crew-member/flight-assignment/list?completed=false"/>
			
		</acme:menu-option>

		<acme:menu-option code="master.menu.assistance-agent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistance-agent.claim.listCompleted" action="/assistance-agent/claim/listCompleted"/>
			<acme:menu-suboption code="master.menu.assistance-agent.claim.listPending" action="/assistance-agent/claim/listPending"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.booking.list" action="/customer/booking/list"/>
	  		<acme:menu-suboption code="master.menu.customer.passenger.list" action="/customer/passenger/list"/> 
	  		<acme:menu-suboption code="master.menu.customer.booking-record.list" action="/customer/booking-record/list" />		
 		</acme:menu-option>		
	</acme:menu-left>

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/airline-manager/create" access="!hasRealm('AirlineManager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-member" action="/authenticated/flight-crew-member/create" access="!hasRealm('FlightCrewMember')"/>
			<acme:menu-suboption code="master.menu.user-account.member-profile" action="/authenticated/flight-crew-member/update" access="hasRealm('FlightCrewMember')"/>
			<acme:menu-suboption code="master.menu.user-account.manager-profile" action="/authenticated/airline-manager/update" access="hasRealm('AirlineManager')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

