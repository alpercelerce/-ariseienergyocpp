package eu.chargetime.ocpp.jsonclientimplementation;

import eu.chargetime.ocpp.feature.profile.ClientCoreEventHandler;
import eu.chargetime.ocpp.model.core.*;

public interface CustomClientCoreEventHandler extends ClientCoreEventHandler {

    StartTransactionConfirmation handleStartTransactionRequest(StartTransactionRequest var1);

    StopTransactionConfirmation handleStopTransactionRequest(StopTransactionRequest var1);

    MeterValuesConfirmation handleMeterValuesRequest(MeterValuesRequest request);
}
