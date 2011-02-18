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

package com.akiban.admin.state;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import com.akiban.admin.Admin;
import com.akiban.admin.AdminValue;

public class AkServerState
{
    public int version()
    {
        return version;
    }

    public boolean up()
    {
        return up;
    }

    public void up(boolean x)
    {
        up = x;
    }

    public boolean lead()
    {
        return lead;
    }

    public String toPropertiesString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(String.format("state = %s\n", up ? "up" : "down"));
        buffer.append(String.format("lead = %s\n", lead));
        return buffer.toString();
    }

    public AkServerState(boolean up, boolean lead)
    {
        this.up = up;
        this.lead = lead;
    }

    public AkServerState(AdminValue adminValue) throws UnknownHostException
    {
        this.version = adminValue.version();
        Properties properties = adminValue.properties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.equals(STATE)) {
                up = value.equals("up");
            } else if (key.equals(LEAD)) {
                lead = value.equals("true");
            } else {
                throw new Admin.RuntimeException
                    (String.format("Unsupported chunkserver property: %s", key));
            }
        }
    }

    private static String STATE = "state";
    private static String LEAD = "lead";

    private int version;
    private boolean up = false;
    private boolean lead = false;
}