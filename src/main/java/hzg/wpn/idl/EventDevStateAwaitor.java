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
import org.tango.client.ez.proxy.*;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 05.06.12
 */
public class EventDevStateAwaitor extends TangoDevStateAwaitor {

    private final Object internalLock = new Object();
    private volatile Throwable error = null;
    private volatile TangoEventListener<DevState> listener;

    public EventDevStateAwaitor(TangoProxy proxy, TangoProxyExceptionHandler handler) {
        super(proxy, handler);
    }

    @Override
    public void waitUntil(DevState targetState, long timeout) {
        Preconditions.checkArgument(timeout > 0L, "timeout can not be less or equal to 0");
        try {
            DevState crtState = getProxy().readAttribute(STATE);
            setCrtDevState(crtState);

            subscribeToStateChange();
            synchronized (internalLock) {
                while (!targetStateReached(targetState)) {
                    internalLock.wait(timeout);
                }
            }
            listener = null;
        } catch (TangoProxyException devFailed) {
            throw getHandler().handle(devFailed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw getHandler().handle(error != null ? error : e);
        } catch (NoSuchAttributeException e) {
            throw getHandler().handle(e);
        }
    }


    @Override
    public void waitUntil(DevState targetState) {
        waitUntil(targetState, Long.MAX_VALUE);
    }

    @Override
    public void waitUntilNot(DevState targetState, long timeout) {
        Preconditions.checkArgument(timeout > 0L, "timeout can not be less or equal to 0");
        try {
            DevState crtState = getProxy().readAttribute(STATE);
            setCrtDevState(crtState);

            subscribeToStateChange();
            synchronized (internalLock) {
                while (targetStateReached(targetState)) {
                    internalLock.wait(timeout);
                }
            }
            listener = null;
        } catch (TangoProxyException devFailed) {
            throw getHandler().handle(devFailed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw getHandler().handle(error != null ? error : e);
        } catch (NoSuchAttributeException e) {
            throw getHandler().handle(e);
        }
    }

    @Override
    public void waitUntilNot(DevState targetState) {
        waitUntilNot(targetState, Long.MAX_VALUE);
    }

    private void subscribeToStateChange() throws TangoProxyException, NoSuchAttributeException {
        final Thread mainThread = Thread.currentThread();
        getProxy().subscribeToEvent(STATE, TangoEvent.CHANGE);
        getProxy().addEventListener(STATE, TangoEvent.CHANGE, listener = new TangoEventListener<DevState>() {
            @Override
            public void onEvent(EventData<DevState> eventData) {
                DevState crtState = eventData.getValue();
                if (crtState == null) return;
                synchronized (internalLock) {
                    setCrtDevState(crtState);
                    internalLock.notifyAll();
                }
            }

            @Override
            public void onError(Exception e) {
                error = e;
                //does not work in IDL
//                getLog().error(e.getMessage(),e);
                mainThread.interrupt();
            }
        });
    }

}
