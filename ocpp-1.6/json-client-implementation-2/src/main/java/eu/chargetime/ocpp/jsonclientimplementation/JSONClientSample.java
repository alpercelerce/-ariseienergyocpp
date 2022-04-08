package eu.chargetime.ocpp.jsonclientimplementation;

import eu.chargetime.ocpp.IClientAPI;
import eu.chargetime.ocpp.JSONClient;
import eu.chargetime.ocpp.OccurenceConstraintException;
import eu.chargetime.ocpp.UnsupportedFeatureException;
import eu.chargetime.ocpp.feature.profile.ClientCoreEventHandler;
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
        core = new ClientCoreProfileCustom(new ClientCoreEventHandler() {
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
        });
        client = new JSONClient(core, "OCCPLocalClinetIdentity2");
        client.connect("ws://20.107.186.160:8887", null);

        logger.info("connect - " + "finished");
    }

    public void sendBootNotification() throws Exception {

        // Use the feature profile to help create event
        Request request = core.createBootNotificationRequest("ATESS 2", "EVA-07S-P", "17421A45753ch7");

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
        SampledValue sampledCurrentImport = new SampledValue("123");
        sampledCurrentImport.setFormat(ValueFormat.Raw);
        sampledCurrentImport.setLocation(Location.EV);
        sampledCurrentImport.setMeasurand("Current.Import");

        SampledValue sampledCurrentExport = new SampledValue("112");
        sampledCurrentExport.setFormat(ValueFormat.Raw);
        sampledCurrentExport.setLocation(Location.EV);
        sampledCurrentExport.setMeasurand("Current.Export");

        SampledValue sampledCurrentOffered = new SampledValue("1234");
        sampledCurrentOffered.setFormat(ValueFormat.Raw);
        sampledCurrentOffered.setLocation(Location.EV);
        sampledCurrentOffered.setMeasurand("Current.Offered");

        SampledValue sampledPowerActiveExport = new SampledValue("1134");
        sampledPowerActiveExport.setFormat(ValueFormat.Raw);
        sampledPowerActiveExport.setLocation(Location.EV);
        sampledPowerActiveExport.setMeasurand("Power.Active.Export");

        SampledValue sampledPowerActiveImport = new SampledValue("1114");
        sampledPowerActiveImport.setFormat(ValueFormat.Raw);
        sampledPowerActiveImport.setLocation(Location.EV);
        sampledPowerActiveImport.setMeasurand("Power.Active.Import");

        SampledValue sampledPowerFactor = new SampledValue("11343");
        sampledPowerFactor.setFormat(ValueFormat.Raw);
        sampledPowerFactor.setLocation(Location.EV);
        sampledPowerFactor.setMeasurand("Power.Factor");

        SampledValue sampledPowerOffered = new SampledValue("1123414");
        sampledPowerOffered.setFormat(ValueFormat.Raw);
        sampledPowerOffered.setLocation(Location.EV);
        sampledPowerOffered.setMeasurand("Power.Offered");

        SampledValue sampledVoltage = new SampledValue("994");
        sampledVoltage.setFormat(ValueFormat.Raw);
        sampledVoltage.setLocation(Location.EV);
        sampledVoltage.setMeasurand("Voltage");

        SampledValue[] sampledValues = new SampledValue[] {
                sampledCurrentImport,
                sampledCurrentExport,
                sampledCurrentOffered,
                sampledPowerActiveExport,
                sampledPowerActiveImport,
                sampledPowerFactor,
                sampledPowerOffered,
                sampledVoltage
        };

        MeterValue meterValue = new MeterValue(ZonedDateTime.now(), sampledValues);

        SampledValue sampledCurrentImport1 = new SampledValue("9123");
        sampledCurrentImport1.setFormat(ValueFormat.Raw);
        sampledCurrentImport1.setLocation(Location.EV);
        sampledCurrentImport1.setMeasurand("Current.Import");

        SampledValue sampledCurrentExport1 = new SampledValue("9112");
        sampledCurrentExport1.setFormat(ValueFormat.Raw);
        sampledCurrentExport1.setLocation(Location.EV);
        sampledCurrentExport1.setMeasurand("Current.Export");

        SampledValue sampledCurrentOffered1 = new SampledValue("91234");
        sampledCurrentOffered1.setFormat(ValueFormat.Raw);
        sampledCurrentOffered1.setLocation(Location.EV);
        sampledCurrentOffered1.setMeasurand("Current.Offered");

        SampledValue sampledPowerActiveExport1 = new SampledValue("91134");
        sampledPowerActiveExport1.setFormat(ValueFormat.Raw);
        sampledPowerActiveExport1.setLocation(Location.EV);
        sampledPowerActiveExport1.setMeasurand("Power.Active.Export");

        SampledValue sampledPowerActiveImport1 = new SampledValue("91114");
        sampledPowerActiveImport1.setFormat(ValueFormat.Raw);
        sampledPowerActiveImport1.setLocation(Location.EV);
        sampledPowerActiveImport1.setMeasurand("Power.Active.Export");

        SampledValue sampledPowerFactor1 = new SampledValue("911343");
        sampledPowerFactor1.setFormat(ValueFormat.Raw);
        sampledPowerFactor1.setLocation(Location.EV);
        sampledPowerFactor1.setMeasurand("Power.Factor");

        SampledValue sampledPowerOffered1 = new SampledValue("9123414");
        sampledPowerOffered1.setFormat(ValueFormat.Raw);
        sampledPowerOffered1.setLocation(Location.EV);
        sampledPowerOffered1.setMeasurand("Power.Offered");

        SampledValue sampledVoltage1 = new SampledValue("9994");
        sampledVoltage1.setFormat(ValueFormat.Raw);
        sampledVoltage1.setLocation(Location.EV);
        sampledVoltage1.setMeasurand("Voltage");

        SampledValue[] sampledValues1 = new SampledValue[] {
                sampledCurrentImport1,
                sampledCurrentExport1,
                sampledCurrentOffered1,
                sampledPowerActiveExport1,
                sampledPowerActiveImport1,
                sampledPowerFactor1,
                sampledPowerOffered1,
                sampledVoltage1
        };
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
