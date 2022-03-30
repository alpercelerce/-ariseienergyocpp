package eu.chargetime.ocpp.jsonclientimplementation;

import eu.chargetime.ocpp.feature.profile.ClientCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ClientCoreProfile;
import eu.chargetime.ocpp.model.core.BootNotificationRequest;

public class ClientCoreProfileCustom extends ClientCoreProfile {

    public ClientCoreProfileCustom(ClientCoreEventHandler handler) {
        super(handler);
    }

    public BootNotificationRequest createBootNotificationRequest(String vendor, String model, String chargeBoxSerialNumber) {
        BootNotificationRequest bNR = new BootNotificationRequest(vendor, model);
        bNR.setMeterSerialNumber(chargeBoxSerialNumber);
        bNR.setChargePointSerialNumber("345677");
        bNR.setFirmwareVersion("v1.6");
        bNR.setIccid("89359201436831365885");
        bNR.setImsi("084935541179413646");
        bNR.setMeterType("single phase");
        return bNR;
    }
}
