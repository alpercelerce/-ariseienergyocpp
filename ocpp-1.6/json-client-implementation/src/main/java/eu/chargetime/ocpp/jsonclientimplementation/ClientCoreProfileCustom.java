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
        return bNR;
    }
}
