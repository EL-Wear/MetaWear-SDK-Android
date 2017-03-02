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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by etsai on 8/31/16.
 */
public class MetaWearBoardInfo {
    public final String name;
    public final byte[] modelNumber, hardwareRevision, manufacturer, serialNumber;
    public final Map<Byte, byte[]> moduleResponses;
    public final Model model;

    public MetaWearBoardInfo(Model model, String name, String modelNumber, String hardwareRevision, byte[][] moduleResponsesArray) {
        this.model = model;
        this.name= name;
        this.modelNumber = modelNumber.getBytes();
        this.hardwareRevision = hardwareRevision.getBytes();
        this.manufacturer = new byte[] {0x4d, 0x62, 0x69, 0x65, 0x6e, 0x74, 0x4c, 0x61, 0x62, 0x20, 0x49, 0x6e, 0x63};
        this.serialNumber = new byte[] {0x30, 0x30, 0x33, 0x42, 0x46, 0x39};

        this.moduleResponses = new HashMap<>();
        for(byte[] response: moduleResponsesArray) {
            this.moduleResponses.put(response[0], response);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public static final MetaWearBoardInfo CPRO= new MetaWearBoardInfo(Model.METAWEAR_CPRO, "cpro", "2", "0.2", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x00},
            {0x03, (byte) 0x80, 0x01, 0x01},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x03, 0x01, 0x02},
            {0x05, (byte) 0x80, 0x00, 0x00, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x00, 0x1C},
            {0x0A, (byte) 0x80, 0x00, 0x00, 0x1C},
            {0x0B, (byte) 0x80, 0x00, 0x02, 0x08, (byte) 0x80, 0x2B, 0x00, 0x00},
            {0x0C, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0D, (byte) 0x80, 0x00, 0x00},
            {0x0F, (byte) 0x80, 0x00, 0x00},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x00},
            {0x12, (byte) 0x80, 0x00, 0x00},
            {0x13, (byte) 0x80, 0x00, 0x01},
            {0x14, (byte) 0x80, 0x00, 0x00},
            {0x15, (byte) 0x80, 0x00, 0x00},
            {0x16, (byte) 0x80},
            {0x17, (byte) 0x80},
            {0x18, (byte) 0x80},
            {0x19, (byte) 0x80},
            {(byte) 0xFE, (byte) 0x80, 0x00, 0x00}
    }),
    DETECTOR= new MetaWearBoardInfo(Model.METADETECT, "detector", "2", "0.2", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x00},
            {0x03, (byte) 0x80, 0x03, 0x01},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x03, 0x01, 0x02},
            {0x05, (byte) 0x80, 0x00, 0x01, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0a, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0b, (byte) 0x80, 0x00, 0x02, 0x08, (byte) 0x80, 0x2b, 0x00, 0x00},
            {0x0c, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0d, (byte) 0x80, 0x00, 0x00},
            {0x0f, (byte) 0x80, 0x00, 0x01, 0x08},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x03},
            {0x12, (byte) 0x80},
            {0x13, (byte) 0x80},
            {0x14, (byte) 0x80, 0x00, 0x00},
            {0x15, (byte) 0x80},
            {0x16, (byte) 0x80},
            {0x17, (byte) 0x80},
            {0x18, (byte) 0x80, 0x00, 0x00},
            {0x19, (byte) 0x80},
            {(byte) 0xfe, (byte) 0x80, 0x00, 0x00},
    }),
    ENVIRONMENT= new MetaWearBoardInfo(Model.METAENV, "environment", "2", "0.2", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x00},
            {0x03, (byte) 0x80, 0x03, 0x01},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x03, 0x01, 0x02},
            {0x05, (byte) 0x80, 0x00, 0x01, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0a, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0b, (byte) 0x80, 0x00, 0x02, 0x08, (byte) 0x80, 0x2b, 0x00, 0x00},
            {0x0c, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0d, (byte) 0x80, 0x00, 0x00},
            {0x0f, (byte) 0x80, 0x00, 0x01, 0x08},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x03},
            {0x12, (byte) 0x80, 0x01, 0x00},
            {0x13, (byte) 0x80},
            {0x14, (byte) 0x80},
            {0x15, (byte) 0x80},
            {0x16, (byte) 0x80, 0x00, 0x00},
            {0x17, (byte) 0x80, 0x00, 0x00},
            {0x18, (byte) 0x80},
            {0x19, (byte) 0x80},
            {(byte) 0xfe, (byte) 0x80, 0x00, 0x00},
    }),
    RPRO= new MetaWearBoardInfo(Model.METAWEAR_RPRO, "rpro", "1", "0.3", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x00},
            {0x03, (byte) 0x80, 0x01, 0x01},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x03, 0x01, 0x02},
            {0x05, (byte) 0x80, 0x00, 0x00, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x00, 0x1C},
            {0x0A, (byte) 0x80, 0x00, 0x00, 0x1C},
            {0x0B, (byte) 0x80, 0x00, 0x02, 0x08, (byte) 0x80, 0x2D, 0x00, 0x00},
            {0x0C, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0D, (byte) 0x80, 0x00, 0x00},
            {0x0F, (byte) 0x80, 0x00, 0x00},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x00},
            {0x12, (byte) 0x80, 0x00, 0x00},
            {0x13, (byte) 0x80, 0x00, 0x01},
            {0x14, (byte) 0x80, 0x00, 0x00},
            {0x15, (byte) 0x80},
            {0x16, (byte) 0x80},
            {0x17, (byte) 0x80},
            {0x18, (byte) 0x80},
            {0x19, (byte) 0x80},
            {(byte) 0xFE, (byte) 0x80, 0x00, 0x00}
    }),
    R= new MetaWearBoardInfo(Model.METAWEAR_R, "r", "0", "0.2", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x00},
            {0x03, (byte) 0x80, 0x00, 0x01},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x01},
            {0x05, (byte) 0x80, 0x00, 0x00, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x00, 0x1C},
            {0x0A, (byte) 0x80, 0x00, 0x00, 0x1C},
            {0x0B, (byte) 0x80, 0x00, 0x02, 0x08, (byte) 0x80, 0x31, 0x00, 0x00},
            {0x0C, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0D, (byte) 0x80, 0x00, 0x00},
            {0x0F, (byte) 0x80, 0x00, 0x00},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x00},
            {0x12, (byte) 0x80},
            {0x13, (byte) 0x80},
            {0x14, (byte) 0x80},
            {0x15, (byte) 0x80},
            {0x16, (byte) 0x80},
            {0x17, (byte) 0x80},
            {0x18, (byte) 0x80},
            {0x19, (byte) 0x80},
            {(byte) 0xFE, (byte) 0x80, 0x00, 0x00}
    }),
    RG= new MetaWearBoardInfo(Model.METAWEAR_RG, "rg", "1", "0.2", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x00},
            {0x03, (byte) 0x80, 0x01, 0x01},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x03, 0x01, 0x02},
            {0x05, (byte) 0x80, 0x00, 0x00, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0a, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0b, (byte) 0x80, 0x00, 0x02, 0x08, (byte) 0x80, 0x2D, 0x00, 0x00},
            {0x0c, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0d, (byte) 0x80, 0x00, 0x00},
            {0x0f, (byte) 0x80, 0x00, 0x00},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x00},
            {0x12, (byte) 0x80},
            {0x13, (byte) 0x80, 0x00, 0x01},
            {0x14, (byte) 0x80},
            {0x15, (byte) 0x80},
            {0x16, (byte) 0x80},
            {0x17, (byte) 0x80},
            {0x18, (byte) 0x80},
            {0x19, (byte) 0x80},
            {(byte) 0xfe, (byte) 0x80, 0x00, 0x00}
    }),
    MOTION_R = new MetaWearBoardInfo(Model.METAMOTION_R, "motionr", "5", "0.1", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x00},
            {0x03, (byte) 0x80, 0x01, 0x01},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x03, 0x01, 0x02},
            {0x05, (byte) 0x80, 0x00, 0x00, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x01, 0x1c},
            {0x0a, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0b, (byte) 0x80, 0x00, 0x02, 0x08, (byte) 0x80, 0x2D, 0x00, 0x00},
            {0x0c, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0d, (byte) 0x80, 0x00, 0x01},
            {0x0f, (byte) 0x80, 0x00, 0x01, 0x08},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x05, 0x03},
            {0x12, (byte) 0x80, 0x00, 0x00},
            {0x13, (byte) 0x80, 0x00, 0x01},
            {0x14, (byte) 0x80, 0x00, 0x00},
            {0x15, (byte) 0x80, 0x00, 0x01},
            {0x16, (byte) 0x80},
            {0x17, (byte) 0x80},
            {0x18, (byte) 0x80},
            {0x19, (byte) 0x80, 0x00, 0x00, 0x03, 0x00, 0x06, 0x00, 0x02, 0x00, 0x01, 0x00},
            {(byte) 0xfe, (byte) 0x80, 0x00, 0x00}
    }),
    TRACKER = new MetaWearBoardInfo(Model.METATRACKER, "tracker", "4", "0.1", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x01, 0x03, 0x00},
            {0x03, (byte) 0x80, 0x01, 0x01},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x03, 0x01, 0x02},
            {0x05, (byte) 0x80, 0x00, 0x02, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0a, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0b, (byte) 0x80, 0x00, 0x02, 0x08, 0x00, 0x00, 0x04, 0x00},
            {0x0c, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0d, (byte) 0x80, 0x00, 0x01},
            {0x0f, (byte) 0x80, 0x00, 0x01, 0x08},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x04},
            {0x12, (byte) 0x80, 0x01, 0x00},
            {0x13, (byte) 0x80, 0x00, 0x01},
            {0x14, (byte) 0x80, 0x00, 0x00},
            {0x15, (byte) 0x80},
            {0x16, (byte) 0x80, 0x00, 0x00},
            {0x17, (byte) 0x80},
            {0x18, (byte) 0x80},
            {0x19, (byte) 0x80},
            {(byte) 0xfe, (byte) 0x80, 0x00, 0x01}
    }),
    C = new MetaWearBoardInfo(Model.METAWEAR_C, "c", "2", "0.3", new byte[][] {
            {0x01, (byte) 0x80, 0x00, 0x00},
            {0x02, (byte) 0x80, 0x00, 0x00},
            {0x03, (byte) 0x80, 0x01, 0x00},
            {0x04, (byte) 0x80, 0x01, 0x00, 0x00, 0x03, 0x01, 0x02},
            {0x05, (byte) 0x80, 0x00, 0x02, 0x03, 0x03, 0x03, 0x03, 0x01, 0x01, 0x01, 0x01},
            {0x06, (byte) 0x80, 0x00, 0x00},
            {0x07, (byte) 0x80, 0x00, 0x00},
            {0x08, (byte) 0x80, 0x00, 0x00},
            {0x09, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0a, (byte) 0x80, 0x00, 0x00, 0x1c},
            {0x0b, (byte) 0x80, 0x00, 0x02, 0x08, (byte) 0x80, 0x1f, 0x00, 0x00},
            {0x0c, (byte) 0x80, 0x00, 0x00, 0x08},
            {0x0d, (byte) 0x80, 0x00, 0x00},
            {0x0f, (byte) 0x80, 0x00, 0x01, 0x08},
            {0x10, (byte) 0x80},
            {0x11, (byte) 0x80, 0x00, 0x03},
            {0x12, (byte) 0x80},
            {0x13, (byte) 0x80, 0x00, 0x00},
            {0x14, (byte) 0x80},
            {0x15, (byte) 0x80},
            {0x16, (byte) 0x80},
            {0x17, (byte) 0x80},
            {0x18, (byte) 0x80},
            {0x19, (byte) 0x80},
            {(byte) 0xfe, (byte) 0x80, 0x00, 0x00}
    });
}
