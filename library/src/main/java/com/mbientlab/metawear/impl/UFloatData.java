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

package com.mbientlab.metawear.impl;

import com.mbientlab.metawear.Data;
import com.mbientlab.metawear.builder.filter.DifferentialOutput;

import java.nio.ByteBuffer;
import java.util.Calendar;

import static com.mbientlab.metawear.impl.Constant.Module.DATA_PROCESSOR;

/**
 * Created by etsai on 9/5/16.
 */
class UFloatData extends DataTypeBase {
    private static final long serialVersionUID = -7247066455362777661L;

    UFloatData(Constant.Module module, byte register, byte id, DataAttributes attributes) {
        super(module, register, id, attributes);
    }

    UFloatData(Constant.Module module, byte register, DataAttributes attributes) {
        super(module, register, attributes);
    }

    UFloatData(DataTypeBase input, Constant.Module module, byte register, byte id, DataAttributes attributes) {
        super(input, module, register, id, attributes);
    }

    UFloatData(DataTypeBase input, Constant.Module module, byte register, DataAttributes attributes) {
        super(input, module, register, attributes);
    }

    @Override
    public DataTypeBase copy(DataTypeBase input, Constant.Module module, byte register, byte id, DataAttributes attributes) {
        if (input == null) {
            if (this.input == null) {
                throw new NullPointerException("SFloatData cannot have null input variable");
            }
            return this.input.copy(null, module, register, id, attributes);
        }

        return new UFloatData(input, module, register, id, attributes);
    }

    @Override
    public Number convertToFirmwareUnits(MetaWearBoardPrivate mwPrivate, Number value) {
        return value.floatValue() * scale(mwPrivate);
    }

    @Override
    public Data createMessage(boolean logData, final MetaWearBoardPrivate mwPrivate, final byte[] data, final Calendar timestamp) {
        final ByteBuffer buffer = Util.bytesToUIntBuffer(logData, data, attributes);
        final float scaled= buffer.getLong(0) / scale(mwPrivate);

        return new DataPrivate(timestamp, data) {
            @Override
            public float scale() {
                return UFloatData.this.scale(mwPrivate);
            }

            @Override
            public Class<?>[] types() {
                return new Class<?>[] {Float.class};
            }

            @Override
            public <T> T value(Class<T> clazz) {
                if (clazz.equals(Float.class)) {
                    return clazz.cast(scaled);
                }
                return super.value(clazz);
            }
        };
    }

    @Override
    Pair<? extends DataTypeBase, ? extends DataTypeBase> dataProcessorTransform(DataProcessorConfig config) {
        switch(config.id) {
            case DataProcessorConfig.Maths.ID: {
                DataProcessorConfig.Maths casted = (DataProcessorConfig.Maths) config;
                DataTypeBase processor;
                switch(casted.op) {
                    case ADD: {
                        DataAttributes newAttrs= attributes.dataProcessorCopySize((byte) 4);
                        processor = casted.rhs < 0 ? new SFloatData(this, DATA_PROCESSOR, DataProcessorImpl.NOTIFY, newAttrs) :
                                dataProcessorCopy(this, newAttrs);
                        break;
                    }
                    case MULTIPLY: {
                        DataAttributes newAttrs= attributes.dataProcessorCopySize(Math.abs(casted.rhs) < 1 ? attributes.sizes[0] : 4);
                        processor = casted.rhs < 0 ? new SFloatData(this, DATA_PROCESSOR, DataProcessorImpl.NOTIFY, newAttrs) :
                                dataProcessorCopy(this, newAttrs);
                        break;
                    }
                    case DIVIDE: {
                        DataAttributes newAttrs = attributes.dataProcessorCopySize(Math.abs(casted.rhs) < 1 ? 4 : attributes.sizes[0]);
                        processor = casted.rhs < 0 ? new SFloatData(this, DATA_PROCESSOR, DataProcessorImpl.NOTIFY, newAttrs) :
                                dataProcessorCopy(this, newAttrs);
                        break;
                    }
                    case SUBTRACT:
                        processor = new SFloatData(this, DATA_PROCESSOR, DataProcessorImpl.NOTIFY, attributes.dataProcessorCopySigned(true));
                        break;
                    case ABS_VALUE:
                        processor = dataProcessorCopy(this, attributes.dataProcessorCopySigned(false));
                        break;
                    default:
                        processor = null;
                        break;
                }

                if (processor != null) {
                    return new Pair<>(processor, null);
                }
                break;
            }
            case DataProcessorConfig.Differential.ID: {
                DataProcessorConfig.Differential casted = (DataProcessorConfig.Differential) config;
                if (casted.mode == DifferentialOutput.DIFFERENCE) {
                    return new Pair<>(new SFloatData(this, DATA_PROCESSOR, DataProcessorImpl.NOTIFY, attributes.dataProcessorCopySigned(true)), null);
                }
            }
        }
        return super.dataProcessorTransform(config);
    }
}
