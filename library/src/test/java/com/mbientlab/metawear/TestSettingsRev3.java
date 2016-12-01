/*
 * Copyright 2014-2015 MbientLab Inc. All rights reserved.
 *
 * IMPORTANT: Your use of this Software is limited to those specific rights granted under the terms of a software
 * license agreement between the user who downloaded the software, his/her employer (which must be your
 * employer) and MbientLab Inc, (the "License").  You may not use this Software unless you agree to abide by the
 * terms of the License which can be found at www.mbientlab.com/terms.  The License limits your use, and you
 * acknowledge, that the Software may be modified, copied, and distributed when used in conjunction with an
 * MbientLab Inc, product.  Other than for the foregoing purpose, you may not use, reproduce, copy, prepare
 * derivative works of, modify, distribute, perform, display or sell this Software and/or its documentation for any
 * purpose.
 *
 * YOU FURTHER ACKNOWLEDGE AND AGREE THAT THE SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS" WITHOUT WARRANTY
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION, ANY WARRANTY OF MERCHANTABILITY, TITLE,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL MBIENTLAB OR ITS LICENSORS BE LIABLE OR
 * OBLIGATED UNDER CONTRACT, NEGLIGENCE, STRICT LIABILITY, CONTRIBUTION, BREACH OF WARRANTY, OR OTHER LEGAL EQUITABLE
 * THEORY ANY DIRECT OR INDIRECT DAMAGES OR EXPENSES INCLUDING BUT NOT LIMITED TO ANY INCIDENTAL, SPECIAL, INDIRECT,
 * PUNITIVE OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF PROCUREMENT OF SUBSTITUTE GOODS, TECHNOLOGY,
 * SERVICES, OR ANY CLAIMS BY THIRD PARTIES (INCLUDING BUT NOT LIMITED TO ANY DEFENSE THEREOF), OR OTHER SIMILAR COSTS.
 *
 * Should you have any questions regarding your right to use this Software, contact MbientLab via email:
 * hello@mbientlab.com.
 */

package com.mbientlab.metawear;

import com.mbientlab.metawear.module.Haptic;
import com.mbientlab.metawear.module.Settings;
import com.mbientlab.metawear.module.Settings.BatteryState;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;

import bolts.Capture;
import bolts.Continuation;
import bolts.Task;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by etsai on 10/3/16.
 */
@RunWith(Parameterized.class)
public class TestSettingsRev3 extends UnitTestBase {
    @Parameters(name = "board: {0}")
    public static Collection<Object[]> data() {
        return UnitTestBase.allBoardsParams();
    }

    private Settings settings;

    @Parameter
    public MetaWearBoardInfo info;

    @Before
    public void setup() throws Exception {
        btlePlaform.addCustomModuleInfo(new byte[] { 0x11, (byte) 0x80, 0x00, 0x03 });
        btlePlaform.boardInfo = info;
        connectToBoard();

        settings = mwBoard.getModule(Settings.class);
    }

    @Test
    public void disconnectEvent() throws InterruptedException {
        byte[][] expected= new byte[][] {
                {0x0a, 0x02, 0x11, 0x0a, (byte) 0xff, 0x08, 0x01, 0x04},
                {0x0a, 0x03, (byte) 0xf8, (byte) 0xb8, 0x0b, 0x00}
        };
        final Haptic haptic= mwBoard.getModule(Haptic.class);

        settings.onDisconnect(new CodeBlock() {
            @Override
            public void program() {
                haptic.startMotor(100f, (short) 3000);
            }
        }).continueWith(new Continuation<Observer, Void>() {
            @Override
            public Void then(Task<Observer> task) throws Exception {
                synchronized (TestSettingsRev3.this) {
                    TestSettingsRev3.this.notifyAll();
                }
                return null;
            }
        });

        synchronized (this) {
            this.wait();
        }

        assertArrayEquals(expected, btlePlaform.getCommands());
    }

    @Test
    public void readBattery() {
        byte[] expected= new byte[] {0x11, (byte) 0xcc};

        settings.battery().read();
        assertArrayEquals(expected, btlePlaform.getLastCommand());
    }

    @Test
    public void interpretBatteryData() {
        BatteryState expected= new BatteryState((byte) 99, (short) 4148);
        final Capture<BatteryState> actual= new Capture<>();

        settings.battery().addRoute(new RouteBuilder() {
            @Override
            public void configure(RouteElement source) {
                source.stream(new Subscriber() {
                    @Override
                    public void apply(Data data, Object ... env) {
                        ((Capture<BatteryState>) env[0]).set(data.value(BatteryState.class));
                    }
                });
            }
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                task.getResult().setEnvironment(0, actual);
                return null;
            }
        });
        sendMockResponse(new byte[] {0x11, (byte) 0x8c, 0x63, 0x34, 0x10});

        assertEquals(expected, actual.get());
    }

    @Test
    public void interpretComponentBatteryData() {
        short[] expected = new short[] {99, 4148};
        final short[] actual= new short[2];

        settings.battery().addRoute(new RouteBuilder() {
            @Override
            public void configure(RouteElement source) {
                source.split()
                        .index(0).stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {
                                ((short[]) env[0])[0]= data.value(Short.class);
                            }
                        })
                        .index(1).stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {
                                ((short[]) env[0])[1]= data.value(Short.class);
                            }
                        });
            }
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                task.getResult().setEnvironment(0, (Object) actual);
                task.getResult().setEnvironment(1, (Object) actual);
                return null;
            }
        });
        sendMockResponse(new byte[] {0x11, (byte) 0x8c, 0x63, 0x34, 0x10});

        assertArrayEquals(expected, actual);
    }
}