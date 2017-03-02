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

import com.mbientlab.metawear.module.Logging;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;

import bolts.Capture;
import bolts.Continuation;
import bolts.Task;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by etsai on 9/3/16.
 */
public class TestLoggingDownload extends UnitTestBase {
    private Logging logging;

    @Before
    public void setup() throws Exception {
        junitPlatform.boardInfo= MetaWearBoardInfo.CPRO;
        connectToBoard();

        logging= mwBoard.getModule(Logging.class);
    }

    @Test
    public void readoutPageConfirm() {
        byte[] expected= new byte[] {0x0b, 0x0e};

        sendMockResponse(new byte[] {0xb, 0xd});
        assertArrayEquals(expected, junitPlatform.getLastCommand());
    }
    
    @Test
    public void readoutProgress() {
        long expected[]= new long[] {
                0x019e,
                0x0271, 0x0251, 0x0231, 0x0211, 0x01f1,
                0x01d1, 0x01b1, 0x0191, 0x0171, 0x0151,
                0x0131, 0x0111, 0x00f1, 0x00d1, 0x00b1,
                0x0091, 0x0071, 0x0051, 0x0031, 0x0011,
                0x0000
        };
        final long actual[]= new long[22];

        byte[][] progress_responses= new byte[][]{
                {0x0b, 0x08, 0x71, 0x02, 0x00, 0x00},
                {0x0b, 0x08, 0x51, 0x02, 0x00, 0x00},
                {0x0b, 0x08, 0x31, 0x02, 0x00, 0x00},
                {0x0b, 0x08, 0x11, 0x02, 0x00, 0x00},
                {0x0b, 0x08, (byte) 0xf1, 0x01, 0x00, 0x00},
                {0x0b, 0x08, (byte) 0xd1, 0x01, 0x00, 0x00},
                {0x0b, 0x08, (byte) 0xb1, 0x01, 0x00, 0x00},
                {0x0b, 0x08, (byte) 0x91, 0x01, 0x00, 0x00},
                {0x0b, 0x08, 0x71, 0x01, 0x00, 0x00},
                {0x0b, 0x08, 0x51, 0x01, 0x00, 0x00},
                {0x0b, 0x08, 0x31, 0x01, 0x00, 0x00},
                {0x0b, 0x08, 0x11, 0x01, 0x00, 0x00},
                {0x0b, 0x08, (byte) 0xf1, 0x00, 0x00, 0x00},
                {0x0b, 0x08, (byte) 0xd1, 0x00, 0x00, 0x00},
                {0x0b, 0x08, (byte) 0xb1, 0x00, 0x00, 0x00},
                {0x0b, 0x08, (byte) 0x91, 0x00, 0x00, 0x00},
                {0x0b, 0x08, 0x71, 0x00, 0x00, 0x00},
                {0x0b, 0x08, 0x51, 0x00, 0x00, 0x00},
                {0x0b, 0x08, 0x31, 0x00, 0x00, 0x00},
                {0x0b, 0x08, 0x11, 0x00, 0x00, 0x00},
                {0x0b, 0x08, 0x00, 0x00, 0x00, 0x00}
        };

        logging.downloadAsync(20, new Logging.LogDownloadUpdateHandler() {
            private int i;
            @Override
            public void receivedUpdate(long nEntriesLeft, long totalEntries) {
                actual[i]= nEntriesLeft;
                i++;
            }
        });

        for(byte[] it: progress_responses) {
            sendMockResponse(it);
        }

        assertArrayEquals(expected, actual);
    }

    @Test
    public void download() {
        byte[][] expected= new byte[][]{
                {0x0b, 0x0d, 0x01},
                {0x0b, 0x07, 0x01},
                {0x0b, 0x08, 0x01},
                {0x0b, (byte) 0x85},
                {0x0b, 0x06, (byte) 0x9e, (byte) 0x01, (byte) 0x00, (byte) 0x00, 0x14, 0x00, 0x00, 0x00}
        };

        logging.downloadAsync(20, new Logging.LogDownloadUpdateHandler() {
            @Override
            public void receivedUpdate(long nEntriesLeft, long totalEntries) {

            }
        });
        assertArrayEquals(expected, junitPlatform.getCommands());
    }

    @Test(expected = RuntimeException.class)
    public void downloadInterrupted() throws Exception {
        final Capture<Exception> actual = new Capture<>();
        logging.downloadAsync().continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                actual.set(task.getError());

                synchronized (TestLoggingDownload.this) {
                    TestLoggingDownload.this.notifyAll();
                }
                return null;
            }
        });

        junitPlatform.scheduleTask(new Runnable() {
            @Override
            public void run() {
                mwBoard.disconnectAsync();
            }
        }, 5000L);

        synchronized (this) {
            this.wait();
            throw actual.get();
        }
    }

    @Test
    public void unknownEntry() {
        Object[] expected= new Object[] {Logging.DownloadError.UNKNOWN_LOG_ENTRY, (byte) 0x1, (short) 0x016c};
        final Object[] actual= new Object[3];

        logging.downloadAsync(new Logging.LogDownloadErrorHandler() {
            @Override
            public void receivedError(Logging.DownloadError errorType, byte logId, Calendar timestamp, byte[] data) {
                actual[0]= errorType;
                actual[1]= logId;
                actual[2]= ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort(0);
            }
        });
        sendMockResponse(new byte[] {0x0b, 0x07, (byte) 0xa1, (byte) 0xcc, 0x4d, 0x00, 0x00, 0x6c, 0x01, 0x00, 0x00});

        assertArrayEquals(expected, actual);
    }
}
