/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.server.encoding;

import java.text.ParseException;
import java.util.Date;

import com.akiban.ais.model.Type;
import com.akiban.server.FieldDef;
import com.akiban.server.RowData;

public final class TimestampEncoder extends DateTimeEncoder {
    TimestampEncoder() {
    }

    @Override
    public int fromObject(FieldDef fieldDef, Object value, byte[] dest,
                          int offset) {
        final long v;
        if (value instanceof String) {
            try {
                final Date date = EncodingUtils.getDateFormat(DateTimeEncoder.SDF_DATETIME).parse(
                        (String) value);
                v = (int) (date.getTime() / 1000);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else if (value instanceof Date) {
            final Date date = (Date) value;
            v = (int) (date.getTime() / 1000);
        } else if (value instanceof Long) {
            v = ((Long) value).longValue();
        } else {
            throw new IllegalArgumentException(
                    "Requires a String or a Date");
        }
        return EncodingUtils.putUInt(dest, offset, v, 4);
    }

    @Override
    public Date toObject(FieldDef fieldDef, RowData rowData) throws EncodingException {
        final long location = getLocation(fieldDef, rowData);
        final long time = ((long) rowData.getIntegerValue(
                (int) location, 4)) * 1000;
        return new Date(time);
    }

    @Override
    public boolean validate(Type type) {
        long w = type.maxSizeBytes();
        return type.fixedSize() && w == 4;
    }
}