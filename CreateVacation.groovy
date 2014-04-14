/***
 *  CreateVacation with Ecobee Thermostat(s)
 *  create a vacation period for the Ecobee thermostat(s)
 * 
 *  Author: Yves Racine
 *  linkedIn profile: ca.linkedin.com/pub/yves-racine-m-sc-a/0/406/4b/
 *  Date: 2014-03-31
*/


preferences {
    
    section("Initialize this ecobee thermostat") {
        input "ecobee", "capability.thermostat", title: "Ecobee Thermostat"
    }
    section("Vacation Name") { 
        input "vacationName", "text", title: "Vacation Name"
    }        
    section("Cool Temp for vacation, default = 27C") {
        input "givenCoolTemp", "number", title: "Cool Temp", required: false
    }        
    section("Heat Temp for vacation, default=14C") {
        input "givenHeatTemp", "number", title: "Cool Temp", required: false
    }        
    section("Start date for the vacation, format = DD-MM-YYYY, default=now") {
        input "givenStartDate", "text", title: "Beginning Date"
    }        
    section("Start time for the vacation HH:MM, default= now") {
        input "givenStartTime", "text", title: "Beginning time"
    }        
	section("End date for the vacation format = DD-MM-YYYY") {
        input "givenEndDate", "text", title: "End Date"
    }        
    section("End time for the vacation HH:MM") {
        input "givenEndTime", "text", title: "End time"
    }        
    

}



def installed() {
    
    initialize()
     
}




def updated() {
  
    
    initialize()

}


def initialize() {
    def minHeatTemp = givenHeatTemp ?: 14  // by default, 14C is the minimum heat temp
    def minCoolTemp = givenCoolTemp ?: 27  // by default, 27C is the minimum cool temp
    def vacationStartDateTime=null
    
    if (givenStartDate != "") {
        String dateTime = givenStartDate + givenStartTime
        vacationStartDateTime = new Date().parse('d-M-yyyy H:m', dateTime)
    } 
    else {
    
        vacationStartDateTime = now()
      
    }
    dateTime = givenEndDate + givenEndTime
    def vacationEndDateTime = new Date().parse('d-M-yyyy H:m', dateTime)

    // if you have EMS thermostat(s), replace 'registered' with 'managementSet'
    
    ecobee.iterateCreateVacation('registered', vacationName, minCoolTemp, minHeatTemp, vacationStartDateTime, vacationEndDateTime)
    send("CreateVacation> vacationName ${vacationName} created")
}


private send(msg) {
    if ( sendPushMessage != "No" ) {
        log.debug( "sending push message" )
        sendPush( msg )
    }

    if ( phoneNumber ) {
        log.debug( "sending text message" )
        sendSms( phoneNumber, msg )
    }

    log.debug msg
}