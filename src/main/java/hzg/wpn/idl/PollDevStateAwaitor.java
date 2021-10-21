/*
 * The main contributor to this project is Institute of Materials Research,
 * Helmholtz-Zentrum Geesthacht,
 * Germany.
 *
 * This project is a contribution of the Helmholtz Association Centres and
 * Technische Universitaet Muenchen to the ESS Design Update Phase.
 *
 * The project's funding reference is FKZ05E11CG1.
 *
 * Copyright (c) 2012. Institute of Materials Research,
 * Helmholtz-Zentrum Geesthacht,
 * Germany.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package hzg.wpn.idl;


import com.google.common.base.Preconditions;
import fr.esrf.Tango.DevState;
import org.tango.client.ez.proxy.NoSuchAttributeException;
import org.tango.client.ez.proxy.TangoProxy;
import org.tango.client.ez.proxy.TangoProxyException;

import java.util.concurrent.TimeoutException;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 06.06.12
 */
public class PollDevStateAwaitor extends TangoDevStateAwaitor {
    public static final long SLEEP_GRANULARITY = 10L;
    public static final long DEFAULT_TIMEOUT = Long.MAX_VALUE;

    public PollDevStateAwaitor(TangoProxy proxy, TangoProxyExceptionHandler handler) {
        super(proxy);
    }

    @Override
    public void waitUntil(DevState targetState, long timeout, long delay) throws Exception {
        Preconditions.checkArgument(timeout > 0L, "timeout can not be less or equal to 0");
        Preconditions.checkArgument(delay > 0L, "delay can not be less or equal to 0");
        long start = System.currentTimeMillis();
        long tooLate = start + timeout;


        while (!Thread.currentThread().isInterrupted()) {
            try {
                pollCrtState();
                //do not wait if state are the same
                if (targetStateReached(targetState)) {
                    return;
                } else if (tooLate < System.currentTimeMillis()) {
                    throw new TimeoutException(String.format("Timeout has exceed! %s did not reach state %s within %d ms", getProxy().getName(), targetState, timeout));
                } else {
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw e;
            }
        }
    }

    @Override
    public void waitUntil(DevState targetState, long delay) throws Exception {
        waitUntil(targetState, DEFAULT_TIMEOUT, delay);
    }

    @Override
    public void waitUntil(DevState targetState) throws Exception {
        waitUntil(targetState, DEFAULT_TIMEOUT, SLEEP_GRANULARITY);
    }

    public void waitUntilNot(DevState targetState, long timeout, long delay) throws Exception {
        Preconditions.checkArgument(timeout > 0L, "timeout can not be less or equal to 0");
        Preconditions.checkArgument(delay > 0L, "delay can not be less or equal to 0");
        long start = System.currentTimeMillis();
        long tooLate = start + timeout;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                pollCrtState();
                //wait if states are the same
                if (tooLate < System.currentTimeMillis()) {
                    throw new TimeoutException(String.format("Timeout has exceed! %s did not change state %s within %d ms", getProxy().getName(), targetState, timeout));
                } else if (targetStateReached(targetState)) {
                    Thread.sleep(delay);
                } else {
                    return;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw e;
            }
        }
    }

    @Override
    public void waitUntilNot(DevState targetState, long timeout) throws Exception {
        waitUntilNot(targetState, timeout, SLEEP_GRANULARITY);
    }

    private void pollCrtState() throws TangoProxyException, NoSuchAttributeException {
        DevState crtState = getProxy().readAttribute(STATE);
        setCrtDevState(crtState);
    }

    @Override
    public void waitUntilNot(DevState targetState) throws Exception {
        waitUntilNot(targetState, DEFAULT_TIMEOUT, SLEEP_GRANULARITY);
    }
}
