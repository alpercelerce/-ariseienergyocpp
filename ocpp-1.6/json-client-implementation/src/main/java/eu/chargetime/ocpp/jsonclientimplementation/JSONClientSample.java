package eu.chargetime.ocpp.jsonclientimplementation;

import eu.chargetime.ocpp.IClientAPI;
import eu.chargetime.ocpp.JSONClient;
import eu.chargetime.ocpp.OccurenceConstraintException;
import eu.chargetime.ocpp.UnsupportedFeatureException;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

/*
 * ChargeTime.eu - Java-OCA-OCPP
 * Copyright (C) 2015-2016 Thomas Volden <tv@chargetime.eu>
 *
 * MIT License
 *
 * Copyright (C) 2016-2018 Thomas Volden
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class JSONClientSample {

    private static final Logger logger = LoggerFactory.getLogger(JSONClientSample.class);

    private IClientAPI client;
    private ClientCoreProfileCustom core;

    public void connect() throws Exception {

        logger.info("connect - " + "started");

        // The core profile is mandatory
        core = new ClientCoreProfileCustom(new CustomClientCoreEventHandler() {
            @Override
            public ChangeAvailabilityConfirmation handleChangeAvailabilityRequest(ChangeAvailabilityRequest request) {

                logger.info("handleChangeAvailabilityRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return new ChangeAvailabilityConfirmation(AvailabilityStatus.Accepted);
            }

            @Override
            public GetConfigurationConfirmation handleGetConfigurationRequest(GetConfigurationRequest request) {

                logger.info("handleGetConfigurationRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ChangeConfigurationConfirmation handleChangeConfigurationRequest(ChangeConfigurationRequest request) {

                logger.info("handleChangeConfigurationRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ClearCacheConfirmation handleClearCacheRequest(ClearCacheRequest request) {

                logger.info("handleClearCacheRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(DataTransferRequest request) {

                logger.info("handleDataTransferRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public RemoteStartTransactionConfirmation handleRemoteStartTransactionRequest(RemoteStartTransactionRequest request) {

                logger.info("handleRemoteStartTransactionRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public RemoteStopTransactionConfirmation handleRemoteStopTransactionRequest(RemoteStopTransactionRequest request) {

                logger.info("handleRemoteStopTransactionRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public StartTransactionConfirmation handleStartTransactionRequest(StartTransactionRequest request) {

                logger.info("handleStartTransactionRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public StopTransactionConfirmation handleStopTransactionRequest(StopTransactionRequest request) {

                logger.info("handleStopTransactionRequest - " + request.toString());

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ResetConfirmation handleResetRequest(ResetRequest request) {

                logger.info("handleResetRequest - " + request.toString());


                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public UnlockConnectorConfirmation handleUnlockConnectorRequest(UnlockConnectorRequest request) {

                logger.info("handleUnlockConnectorRequest - " + request.toString());


                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public MeterValuesConfirmation handleMeterValuesRequest(MeterValuesRequest request) {

                logger.info("handleMeterValuesRequest - " + request.toString());


                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

        });
        client = new JSONClient(core, "OCCPLocalClinetIdentity1");
        client.connect("ws://20.107.186.160:8887", null);

        logger.info("connect - " + "finished");
    }

    public void sendBootNotification() throws Exception {

        // Use the feature profile to help create event
        Request request = core.createBootNotificationRequest("ATESS1", "EVA-07S-P", "17421A45753ch6");

        // Client returns a promise which will be filled once it receives a confirmation.
        client.send(request).whenComplete((s, ex) -> System.out.println(s));
    }

    public void sendHeartbeat() throws Exception {

        // Use the feature profile to help create event
        //Request request = core.createBootNotificationRequest("some vendor", "some model");

        Request request = core.createHeartbeatRequest();

        // Client returns a promise which will be filled once it receives a confirmation.
        client.send(request).whenComplete((s, ex) -> System.out.println(s));
    }

    public void sendChargingStationStatus() throws Exception {

        // Use the feature profile to help create event
        //Request request = core.createBootNotificationRequest("some vendor", "some model");

        Request request = core.createStatusNotificationRequest(1234, ChargePointErrorCode.NoError, ChargePointStatus.SuspendedEV);

        // Client returns a promise which will be filled once it receives a confirmation.
        client.send(request).whenComplete((s, ex) -> System.out.println(s));
    }

    public void disconnect() {
        client.disconnect();
    }

    public void meterValues() throws OccurenceConstraintException, UnsupportedFeatureException {
        SampledValue sampledValue = new SampledValue("123");
        sampledValue.setFormat(ValueFormat.Raw);
        sampledValue.setLocation(Location.EV);

        SampledValue sampledValue1 = new SampledValue("1234");
        sampledValue1.setFormat(ValueFormat.Raw);
        sampledValue1.setLocation(Location.EV);
        SampledValue[] sampledValues = new SampledValue[] {sampledValue, sampledValue1};

        MeterValue meterValue = new MeterValue(ZonedDateTime.now(), sampledValues);

        SampledValue sampledValue2 = new SampledValue("12345");
        sampledValue2.setFormat(ValueFormat.Raw);
        sampledValue2.setLocation(Location.EV);

        SampledValue sampledValue3 = new SampledValue("123456");
        sampledValue3.setFormat(ValueFormat.Raw);
        sampledValue3.setLocation(Location.EV);
        SampledValue[] sampledValues1 = new SampledValue[] {sampledValue2, sampledValue3};
        MeterValue meterValue1 = new MeterValue(ZonedDateTime.now(), sampledValues1);

        MeterValue[] meterValues = new MeterValue[] {meterValue1, meterValue};

        MeterValuesRequest request = core.createMeterValuesRequest(1, meterValues);
        request.setTransactionId(123);
        client.send(request).whenComplete((s, ex) -> System.out.println(s));
    }


    public void startTransaction() throws OccurenceConstraintException, UnsupportedFeatureException {
        StartTransactionRequest request = core.createStartTransactionRequest(1, "TEST", 1, ZonedDateTime.now());

        client.send(request).whenComplete((s, ex) -> System.out.println(s));
    }

    public void stopTransaction() throws OccurenceConstraintException, UnsupportedFeatureException {
        StopTransactionRequest request = core.createStopTransactionRequest(2, ZonedDateTime.now(), 12);

        client.send(request).whenComplete((s, ex) -> System.out.println(s));
    }
}
