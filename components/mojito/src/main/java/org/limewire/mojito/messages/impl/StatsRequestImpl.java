/*
 * Mojito Distributed Hash Table (Mojito DHT)
 * Copyright (C) 2006-2007 LimeWire LLC
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
 
package org.limewire.mojito.messages.impl;

import java.io.IOException;
import java.net.SocketAddress;
import java.security.Signature;
import java.security.SignatureException;

import org.limewire.mojito.Context;
import org.limewire.mojito.io.MessageInputStream;
import org.limewire.mojito.io.MessageOutputStream;
import org.limewire.mojito.messages.MessageID;
import org.limewire.mojito.messages.StatsRequest;
import org.limewire.mojito.routing.Contact;
import org.limewire.mojito.routing.Version;
import org.limewire.security.SecureMessage;


/**
 * An implementation of StatsRequest.
 */
public class StatsRequestImpl extends AbstractRequestMessage
        implements StatsRequest {

    private final StatisticType request;
    
    private final byte[] signature;
    
    private int secureStatus = SecureMessage.INSECURE;
    
    public StatsRequestImpl(Context context, 
            Contact contact, MessageID messageId, StatisticType request) {
        super(context, OpCode.STATS_REQUEST, contact, messageId, Version.ZERO);

        this.request = request;
        this.signature = null;
    }

    public StatsRequestImpl(Context context, SocketAddress src, 
            MessageID messageId, Version msgVersion, MessageInputStream in) throws IOException {
        super(context, OpCode.STATS_REQUEST, src, messageId, msgVersion, in);
        
        this.request = in.readStatisticType();
        this.signature = in.readSignature();
    }
    
    public StatisticType getType() {
        return request;
    }

    public void setSecureStatus(int secureStatus) {
        this.secureStatus = secureStatus;
    }

    public int getSecureStatus() {
        return secureStatus;
    }
    
    public boolean isSecure() {
        return secureStatus == SecureMessage.SECURE;
    }

    public byte[] getSecureSignature() {
        return signature;
    }

    public void updateSignatureWithSecuredBytes(Signature signature) 
            throws SignatureException {
        initSignature(signature);
    }
    
    @Override
    protected void writeBody(MessageOutputStream out) throws IOException {
        out.writeStatisticType(request);
        out.writeSignature(signature);
    }
    
    @Override
    public String toString() {
        return "StatsRequest: " + request;
    }
}
