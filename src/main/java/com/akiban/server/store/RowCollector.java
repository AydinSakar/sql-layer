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

package com.akiban.server.store;

import java.nio.ByteBuffer;

public interface RowCollector {

    public final int SCAN_FLAGS_DESCENDING = 1 << 0;

    public final int SCAN_FLAGS_START_EXCLUSIVE = 1 << 1;

    public final int SCAN_FLAGS_END_EXCLUSIVE = 1 << 2;

    public final int SCAN_FLAGS_SINGLE_ROW = 1 << 3;

    public final int SCAN_FLAGS_PREFIX = 1 << 4;

    public final int SCAN_FLAGS_START_AT_EDGE = 1 << 5;

    public final int SCAN_FLAGS_END_AT_EDGE = 1 << 6;

    public final int SCAN_FLAGS_DEEP = 1 << 7;

    public boolean collectNextRow(final ByteBuffer payload) throws Exception;

    public boolean hasMore() throws Exception;

    public void close();
    
    public int getDeliveredRows();

    public int getDeliveredBuffers();
    
    public int getRepeatedRows();
    
    public long getDeliveredBytes();
    
    public int getTableId();
    
    public long getId();

}