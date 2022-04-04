package eu.chargetime.ocpp.jsonclientimplementation;

import eu.chargetime.ocpp.feature.profile.ClientCoreProfile;
import eu.chargetime.ocpp.model.core.BootNotificationRequest;

public class ClientCoreProfileCustom extends ClientCoreProfile {

    public ClientCoreProfileCustom(CustomClientCoreEventHandler handler) {
        super(handler);
    }

    public BootNotificationRequest createBootNotificationRequest(String vendor, String model, String chargeBoxSerialNumber) {
        BootNotificationRequest bNR = new BootNotificationRequest(vendor, model);
        bNR.setMeterSerialNumber(chargeBoxSerialNumber);
        bNR.setChargePointSerialNumber("345676");
        bNR.setFirmwareVersion("v1.6");
        bNR.setIccid("89359201436831365884");
        bNR.setImsi("084935541179413645");
        bNR.setMeterType("single phase");
        return bNR;
    }
}
