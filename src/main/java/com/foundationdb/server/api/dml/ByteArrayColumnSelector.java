/**
 * Copyright (C) 2009-2013 FoundationDB, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.foundationdb.server.api.dml;

public class ByteArrayColumnSelector implements ColumnSelector
{
    @Override
    public boolean includesColumn(int columnPosition)
    {
        return (columnBitMap[columnPosition / 8] & (1 << (columnPosition % 8))) != 0;
    }

    public ByteArrayColumnSelector(byte[] columnBitMap)
    {
        this.columnBitMap = columnBitMap;
    }

    private final byte[] columnBitMap;
}
